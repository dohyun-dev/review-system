# Claude Instructions

## 커밋 규칙

- 타입: `feat`, `refactor`, `fix`, `test`, `chore` 등 사용
- 형식: 한 줄 요약만 작성 (예: `feat: 사원 등록 기능 구현`)
- 의존관계를 고려한 작은 단위로 커밋 (예: 도메인 모델 → 도메인 테스트 → 애플리케이션 계층 → 통합 테스트)

## 테스트 전략

- domain 계층: 단위 테스트로 구현
- application 계층 이상: 통합 테스트로 구현

## 아키텍처 원칙

- 계층 간 또는 패키지 도메인 간 순환참조 금지, 단방향 참조로 구현
- Command는 헥사고날 아키텍처, Query는 전통적인 레이어드 아키텍처 적용

### Query 패키지 구조 (전통적인 레이어드)
```
review/query/
  controller/   - REST 컨트롤러
  service/      - 비즈니스 로직
  repository/   - 인터페이스 + 구현체 (QueryDSL)
  model/        - 조회 전용 모델 (Projection DTO)
```

### QueryDSL 사용 규칙
- Q클래스는 static import 사용 (예: `import com.dohyundev.review.review.command.domain.group.QReviewGroup.reviewGroup`)
- 의존성: `io.github.openfeign.querydsl` 7.0 사용

## Query & API 응답 규칙

### Query 모델
- 조회 전용 모델(Query Model)은 Projection으로 사용한다
- Query 모델은 API 응답으로 그대로 반환할 수 있다
- Query 모델에는 비즈니스 로직을 포함하지 않는다

### API 응답
모든 API 응답은 반드시 ResponseEntity<T>로 감싼다