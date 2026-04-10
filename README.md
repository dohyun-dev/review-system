# Review System — 사내 인사 평가 백엔드

---

## 프로젝트 개요

실무를 진행하며 개발한 리뷰 시스템 백엔드를 재구성한 프로젝트입니다.

실제 프로젝트에서는 next.js로 프론트엔드 개발도 함께 진행하였습니다.

DDD / Clean / Hexagonal을 엄격하게 따르기보다, 왜 필요한지 이해하고 필요한 만큼만 적용하는 실용적 관점을 택했습니다.

---

## 프로젝트 설명

```
[관리자] 리뷰 그룹 생성 → 양식 설계 → 대상자·리뷰어 배정 → 리뷰 시작
                                                                │
[리뷰어]                                    임시 저장 ⇄ 제출  ◀──┘
                                                                │
[관리자] 리뷰 종료 ──▶ 점수 자동 계산 ──▶ (필요 시) 점수 가감 조정
                │
                └── 재개 가능 → 재종료 시 기존 점수 삭제 후 재계산
```

---

## 주요 기능

**관리자**
- 리뷰 그룹 생성 / 수정 / 삭제 / 시작 / 종료 / 재개
- 리뷰 양식 생성 / 수정 / 삭제 (항목 타입별 속성 제어)
- 리뷰 대상자 및 리뷰어 배정 / 해제
- 종료 후 점수 가감 조정

**리뷰어**
- 답변 임시 저장 / 제출 / 제출 취소

**시스템**
- 리뷰 그룹 종료 시 이벤트 기반으로 점수 자동 계산
- 재개 후 재종료 시 기존 점수 삭제 후 재계산

---

## 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Kotlin 2.2.21 (JDK 24 toolchain) |
| Framework | Spring Boot 4.0.4 |
| ORM | Spring Data JPA + Hibernate 7 (Hypersistence Utils 3.14.1) |
| Dynamic Query | QueryDSL 7.0 (openfeign fork) |
| Security | Spring Security + jjwt 0.12.5 |
| API Docs | SpringDoc OpenAPI 3.0.2 |
| DB | MySQL (운영) / H2 (테스트) |
| Build | Gradle (Kotlin DSL) |
| Test | JUnit 5, MockK 1.14.0 |

---

## 설계에서 고민했던 것들

### 1. Command는 헥사고날, Query는 레이어드 (CQRS)

조회 쿼리 작성하다 보면 여러 도메인을 한 번에 엮어야 하는 경우가 많습니다.
이걸 Command 모델 안에서 처리하려고 하면, 도메인 간 양방향 참조가 생기고 결국 단방향 의존이 깨지게 됩니다.

그래서 Query는 아예 별도 패키지로 분리했습니다. 별도의 쿼리용 모델을 따로 구성하였습니다.

- **Command** — 상태 변경, 검증, 생성/수정/삭제, 점수 계산 트리거. **헥사고날 아키텍처** 적용. `port/in`(UseCase)과 `port/out`(Repository) 인터페이스를 두고, 어댑터(웹, JPA)는 그 바깥에서 구현합니다.
- **Query** — 화면/목록/상세 조회. **전통적인 레이어드 아키텍처**(Controller → Service → Repository) 적용. QueryDSL 기반 커스텀 리포지토리와 Projection DTO로 필요한 데이터만 조회합니다.

```text
review/
├── command/
│   ├── adapter/in/web/ 
│   ├── application/
│   │   ├── port/in/   
│   │   └── port/out/  
│   ├── application/service/ 
│   └── domain/ 
└── query/
    ├── controller/ 
    ├── repository/
    └── model/
```

### 2. 리뷰 항목 타입별 제약을 다형성으로 풀어냈습니다.

리뷰 항목은 타입마다 들고 있는 필드랑 검증 규칙이 다 다릅니다. 텍스트형은 placeholder, maxLength 같은 게 있고, 선택형은 options 목록에 “최소 1개 이상 선택” 같은 제약이 붙고, 점수형은 거기에 점수까지 포함됩니다.

각 항목을 추가할때마다 분기 로직등 도메인 로직이 변경됩니다.

**상속 기반 다형성**으로 `ReviewItem`을 해결하였습니다.

```kotlin
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
abstract class ReviewItem(
    val id: Long? = null,
    question: String,
    description: String? = null,
    val isRequired: Boolean = false,
    override var sortOrder: Int,
) : SortOrderable<ReviewItem> {
    val question: String = question.also {
        require(it.isNotBlank()) { "질문은 필수입니다." }
        require(it.length < 50) { "질문은 50자 이내로 작성해주세요." }
    }
}

@Entity
@DiscriminatorValue("TEXT")
class TextReviewItem(
    ...,
    placeholder: String? = null,
    maxLength: Int? = null,
) : ReviewItem(...)

@Entity
abstract class BaseChoiceReviewItem(..., options: List<ReviewItemOption>) : ReviewItem(...) {
    init {
        require(options.isNotEmpty()) { "선택지는 최소 1개 이상이어야 합니다." }
    }
    fun findByOptionId(optionId: Long) = options.find { it.id == optionId }
}

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceReviewItem(...) : BaseChoiceReviewItem(...)
```

커맨드 → 도메인 변환은 **전략 패턴**으로 풀었습니다.

```kotlin
interface ReviewItemFactory {
    fun supports(command: ReviewItemCommand): Boolean
    fun create(command: ReviewItemCommand): ReviewItem
}
```

`TextReviewItemFactory`, `ScoreChoiceReviewItemFactory` 등 각 구현체가 자신이 담당하는 커맨드만 `supports()`하고 변환합니다.

### 3. 도메인 모델을 "POJO + 엔티티 분리" 대신 JPA Entity로 직접 사용했다

정석적인 DDD에서는 POJO 도메인 모델과 JPA 엔티티를 분리하지만 이 프로젝트에서는 그런 구조보다는, 생산성 측면에서 JPA를 적극적으로 활용하는 방향을 선택했습니다.

### 4. 이벤트 기반

리뷰 그룹이 종료되면 ReviewGroupClosedEvent가 발행되고, 점수 계산 서비스가 이를 받아 자동으로 점수를 기록합니다.
재개 후 다시 종료되는 경우에는 기존 점수를 먼저 삭제한 뒤 다시 계산하도록 해서, 같은 이벤트가 여러 번 발생해도 결과가 달라지지 않도록 멱등성을 보장했습니다.

```kotlin
@TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
@Transactional(propagation = Propagation.MANDATORY)
fun onReviewGroupClosed(event: ReviewGroupClosedEvent) {
    val reviewGroupId = event.reviewGroup.id!!
    reviewerScoreRepository.deleteAllByReviewerReviewGroupId(reviewGroupId)

    val scores = reviewerAnswerRepository.findAllByReviewerReviewGroupId(reviewGroupId)
        .map(ReviewerScore::from)
    reviewerScoreRepository.saveAll(scores)
}
```

`AFTER_COMMIT`이 아니라 `BEFORE_COMMIT`으로 선택한 이유는, "리뷰 그룹 종료"와 "점수 저장"이 같은 트랜잭션 안에서 원자적으로 확정되어야 하기 때문입니다. 하나가 성공하고 다른 하나가 실패해 정합성이 깨지는 구간을 만들고 싶지 않았습니다.

---

## 도메인 구조

```text
auth/               # JWT 인증/인가
employee/           # 사원 관리
review/
  ├── group/        # 리뷰 그룹 (PREPARING → IN_PROGRESS → CLOSED)
  ├── review/       # 리뷰 양식 (Text / Textarea / ScoreChoice 다형성 계층)
  ├── reviewer/     # 리뷰어 배정 (WAIT / NOT_SUBMITTED / DRAFT / SUBMITTED)
  ├── answer/       # 답변 작성 / 제출 / 취소
  ├── score/        # 점수 계산 및 조정
  └── target/       # 리뷰 대상자
```

---

## 테스트 전략

- **Domain 계층** — 단위 테스트
- **Application 계층 이상** — 통합 테스트

---

## 실행 방법

```bash
./gradlew build
./gradlew test
./gradlew bootRun
```
