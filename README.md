# BD-Assignment1

### 개발 요구사항
- 회원가입, 로그인
- 게시글 CRUD
- 게시글 카테고리
- 게시글 검색
- 대댓글(1 depth)
    - 대댓글 pagination
- 게시글 읽힘 수
    - 같은 User가 게시글을 읽는 경우 count 수 증가하면 안 됨
- Rest API 설계
- Unit Test
- 1000만건 이상의 데이터를 넣고 성능테스트 진행 결과 필요

### 회원가입, 로그인

- Spring Security의 PasswordEncoder를 이용해서 비밀번호를 암호화하여 DB에 저장

### JWT

- jwt 인증
  - aceessToken과 refreshToken을 이용해서 구현
  - 토큰에는 토큰의 유효 기간, 유저의 정보 저장
  - 로그인 시 accessToken과 refreshToken을 발급하고 refreshToken은 DB에 저장
  - 유저가 accessToken을 헤더에 담아 API를 요청
  - 토큰 양식과 유효기간이 유효하면 → API 응답
  - 토큰 양식이 유효하지 않으면 → 유효하지 않은 토큰 오류
  - 토큰 양식은 유효하나 유효기간이 지났으면 → refreshToken 요구
    - 프론트가 accessToken과 refreshToken으로 다시 API 요청
    - 프론트가 전송한 refreshToken과 DB에 저장된 refreshToken을 비교
      - 같으면 → accessToken과 refreshToken 재발급
      - 다르면 → 재로그인 요청
- jwt 인증을 검사하는 위치
  - 필터 : 비즈니스 로직으로의 접근 자체가 불필요한 요청(ex.보안을 위한 처리)
  - **인터셉터** : 요청에 대해 비즈니스 로직으로 적절한 처리가 필요
    - Jwt 로직을 처리할 JwtService 구현
    - HandlerInterceptor 인터페이스를 구현한 인터셉터에서 preHandle() 토큰 유효성을 검사하는 로직 추가
    - WebMvcConfigurer 인터페이스를 구현한 클래스에서 addInterceptors()로 인터셉터 추가
  - aop : 가장 세부적인 로직에서의 처리
- jwt에서 유저 정보를 가져와서 사용할 수 있음

### 게시글 읽힘 수

- 게시글 → 유저 1:N 단방향 연관관계(readers 테이블) 설정
- 게시글 조회 API 발생 시 해당 게시글을 읽은 사용자인지 판단해서 readers에 추가

### 게시글 검색

- Jpa에서 제공하는 contain 메소드로 키워드 검색 구현
  - sql의 like 연산자
- 검색 성능 개선 필요

### 댓글 대댓글

- 대댓글 : 댓글 테이블을 셀프 조인하여 외래키로 부모 댓글id를 가짐
- 댓글, 대댓글 페이징
  - 페이징은 Pageable을 이용해서 구현
  - 부모 댓글은 한 페이지에 5개, 각 부모 댓글은 처음에 최대 5개의 대댓글을 표시할 수 있다고 가정, 더보기 버튼을 눌러 이후 대댓글 조회
    1. 한방 쿼리를 구현해 DB를 한번만 조회해서 첫 화면에서 제공되어야 할 부모 댓글 5개와 각 부모 댓글의 대댓글 5개씩 조회

       → 1번의 DB 접근 발생

    2. **부모 댓글 5개를 조회, 각 부모 댓글의 id를 이용해 부모 댓글id가 일치하는 대댓글 5개를 별도로 조회**

       → 부모 댓글 조회(1번) + 각 부모 댓글 당 대댓글 조회(5번) = 6번의 DB 접근 발생

- 조회 성능 개선 필요
  - 게시글id에 인덱스 생성

### API 응답 및 예외 처리

- 기본적으로 ResponseEntity를 이용해 응답
- ResponseEntityExceptionHandler를 상속받은 ExceptionHandler를 구현해서 @ControllerAdvice로 등록
- @ExceptionHandler를 이용해서 예외 발생 시 에러 메시지를 API 응답으로 제공