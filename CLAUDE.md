# Claude Instructions

## 커밋 메시지 규칙

- 타입: `feat`, `refactor`, `fix`, `test`, `chore` 등 사용
- 형식: 한 줄 요약만 작성 (예: `feat: 사원 등록 기능 구현`)

## 테스트 전략

- domain 계층: 단위 테스트로 구현
- application 계층 이상: 통합 테스트로 구현

## 아키텍처 원칙

- 계층 간 또는 패키지 도메인 간 순환참조 금지, 단방향 참조로 구현
