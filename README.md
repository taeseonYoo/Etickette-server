# Etickette

<img width="400" height="300" alt="Image" src="https://github.com/user-attachments/assets/4af4bc7b-86c1-4194-960b-d18c9d0d4b51" />
<br>

### Etiquette + Ticket = Etickette
> 콘서트 티켓을 안정적으로 예매할 수 있는 서비스

## 목차
- [소개](#소개)
- [담당 역할](#담당-역할)
- [기능](#기능)
- [실행 화면](#실행-화면)
- [기술 스택](#기술-스택)
- [문서](#문서)

## 소개
- 콘서트 티켓 예매는 **짧은 시간에 대규모 사용자가 몰리는 서비스**로, 서버 과부하와 좌석 중복 예약 같은 문제가 자주 발생합니다.
- 이 프로젝트는 이러한 문제를 해결하고, **안정적이고 빠른 티켓 예매 경험**을 제공하기 위해 개발되었습니다.

## 담당 역할
개인 프로젝트로, 기획부터 개발·배포까지 전 과정을 직접 수행했습니다.

### Backend
- Spring Boot 기반 REST API 설계 및 구현
- JPA + MySQL로 좌석 예매 트랜잭션 처리
- 부하테스트(nGrinder)로 조회 성능 측정 및 개선
    - **쿼리 최적화**로 평균 응답 시간을 `1,728.59ms`→`1,555.07ms`로 약 10% 감소
    - **인덱스 적용**으로 평균 응답 시간을 `1,555.07ms`→`335.32ms`로 약 78% 감소
    - 쿼리에서 불필요한 DISTINCT 명령어를 제거하여 쿼리 실행 시간을 `1.6ms` -> `0.9ms`로 약 44% 감소
- 테스트 코드 작성
    - Line Coverage 약 75%,  Method Coverage 80% 달성
 ### Frontend
- React 기반 좌석 선택 UI, API 연동

### 기획 및 설계
- DB ERD 설계
- ErrorCode 문서(REST Docs) 작성
- Github README, API 문서(Postman) 작성

## 기능
- [x] **회원가입 / 로그인** – JWT 기반 인증 및 토큰 갱신
- [x] **공연 생성 및 관리** - 공연장별 공연 등록 시스템
- [x] **좌석 선택 및 예약** – Pessimistic Lock으로 중복 예약 방지
- [x] **결제 연동** – 토스 페이먼츠 API 연동
- [ ] (예정) **대기열 시스템** – 트래픽 급증 시 사용자 순차 처리



## 실행 화면

|  |  |
| :---: | :---: |
| <img width="500" height="400" src="https://github.com/user-attachments/assets/6c90cafb-b617-4a79-87f5-c7dabe6f38be"/><br>메인 화면 | <img width="500" height="400" src="https://github.com/user-attachments/assets/71dee4ea-3a39-4264-bbc2-4af9c120a031"/><br>예매 |




## 기술 스택

| 분야            | 기술                 |
|----------------|---------------------|
| Backend        | Spring Boot, JPA, Redis |
| Frontend       | React, TailwindCSS  |
| Database       | MySQL, Redis        |
| Infra          | AWS EC2, S3, Docker |

---

## 문서
### ERD
<details>
  <summary>ERD 구조 확인하기</summary>
  <img width="1277" height="729" alt="Image" src="https://github.com/user-attachments/assets/7a4fba9d-23be-434b-af88-c4d062ddf5e1" />
</details>

### API 명세서

[API 명세서 바로가기](https://documenter.getpostman.com/view/30139408/2sB3BHm9GK)

