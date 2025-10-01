# 🎟️ Etickette

<img width="400" height="300" alt="Image" src="https://github.com/user-attachments/assets/4af4bc7b-86c1-4194-960b-d18c9d0d4b51" />
<br>

### 🎩 Etiquette + 🎫 Ticket = Etickette
### 매너있는 티켓팅 문화를 제공하는 서비스

<br>

## 🖥️ 티켓팅 실행 화면

![Image](https://github.com/user-attachments/assets/71dee4ea-3a39-4264-bbc2-4af9c120a031)

## 👨🏻‍💻 담당 역할

- 사용자 회원 가입/ 로그인
    - JWT 기반 인증으로 서버의 부하를 감소시켰습니다.
- 콘서트 좌석 예매 시스템
- 핵심 도메인 로직 단위 테스트 및 통합 테스트 작성
    - Line Coverage 약 75%,  Method Coverage 80% 달성
- 토스 페이먼츠 API를 연동하여 외부 결제 기능 구현
- nGrinder를 사용하여 부하 테스트를 진행 → 병목 지점을 발견하고 성능을 개선
- React 기반 프론트엔드와 REST API를 통해 데이터 연동

## ⚒️ 사용 기술

- Spring Security + JWT : 서버 부하를 줄이기 위해 JWT 기반 인증/인가 방식을 도입했습니다. 보안을 위해 Refresh/Access 구조로 설계했습니다.
- OAuth2 인증 : 소셜 로그인 연동을 위해 OAuth2 기반 인증 방식을 도입했습니다.
- MySQL + JPA : 주요 도메인의 CRUD 및 연관관계 매핑을 위해 사용했습니다.
- AWS S3: 정적 파일을 서버와 분리해 확장성과 안정성을 확보하기 위해 사용했습니다.


## 📖 문서
### ERD
<details>
  <summary>ERD 구조 확인하기</summary>
  <img width="1277" height="729" alt="Image" src="https://github.com/user-attachments/assets/7a4fba9d-23be-434b-af88-c4d062ddf5e1" />
</details>

### API 명세서

[API 명세서 바로가기](https://documenter.getpostman.com/view/30139408/2sB3BHm9GK)

