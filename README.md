# 몽음 (Mong-eum) 🌙

> AI 기반 꿈 일기 및 해몽 서비스 백엔드 API

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.oracle.com/java/)
[![Gradle](https://img.shields.io/badge/Gradle-8.x-blue.svg)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

## 📋 목차

- [프로젝트 소개](#-프로젝트-소개)
- [주요 기능](#-주요-기능)
- [기술 스택](#-기술-스택)
- [시스템 아키텍처](#-시스템-아키텍처)
- [시작하기](#-시작하기)
- [API 문서](#-api-문서)
- [프로젝트 구조](#-프로젝트-구조)
- [개발 가이드](#-개발-가이드)
- [테스트](#-테스트)
- [배포](#-배포)
- [트러블슈팅](#-트러블슈팅)
- [로드맵](#-로드맵)
- [기여하기](#-기여하기)
- [라이센스](#-라이센스)
- [팀](#-팀)

## 🌟 프로젝트 소개

**몽음(Mong-eum)** 은 사용자의 꿈을 기록하고 AI를 활용해 해몽과 감정 분석을 제공하는 혁신적인 서비스입니다.

### 왜 몽음인가?

- 📝 **체계적인 꿈 관리**: 꿈을 기록하고 언제든지 돌아볼 수 있습니다
- 🤖 **AI 기반 해몽**: 꿈의 상징과 의미를 자동으로 분석합니다
- 💭 **감정 분석**: 꿈에서 느낀 감정을 7가지 카테고리로 분류합니다
- 🎵 **음악 추천**: 꿈의 감정에 맞는 음악을 추천받을 수 있습니다
- 🔐 **안전한 개인 공간**: JWT 기반 인증으로 개인정보를 안전하게 보호합니다

## ✨ 주요 기능

### 현재 구현된 기능

#### 1. 사용자 인증 및 관리
- ✅ 회원가입 및 로그인
- ✅ JWT 기반 토큰 인증
- ✅ 사용자 프로필 조회
- ✅ BCrypt 비밀번호 암호화

#### 2. 꿈 일기 관리
- ✅ 꿈 등록 (제목, 내용)
- ✅ 내 꿈 목록 조회 (최신순 정렬)
- ✅ 꿈 상세 조회
- ✅ 꿈 삭제
- ✅ 본인 꿈만 접근 가능한 권한 제어

#### 3. AI 분석 (현재 Mock 데이터)
- ✅ 자동 해몽 (interpretation)
- ✅ 감정 카테고리 분류 (HAPPY, SAD, ANXIOUS, PEACEFUL, EXCITED, NOSTALGIC, FEARFUL)
- ✅ 감정 분석 설명 (emotionalAnalysis)
- ✅ 음악 추천 (곡명, 아티스트, URL)

### 감정 카테고리

| 카테고리 | 설명 |
|---------|------|
| 😊 HAPPY | 행복하고 즐거운 꿈 |
| 😢 SAD | 슬프고 우울한 꿈 |
| 😰 ANXIOUS | 불안하고 걱정스러운 꿈 |
| 😌 PEACEFUL | 평화롭고 안정적인 꿈 |
| 😆 EXCITED | 흥분되고 자극적인 꿈 |
| 🥺 NOSTALGIC | 그리움과 향수를 느끼는 꿈 |
| 😱 FEARFUL | 두렵고 무서운 꿈 |

## 🛠 기술 스택

### Backend
- **Framework**: Spring Boot 3.5.7
- **Language**: Java 17
- **Build Tool**: Gradle 8.x
- **Security**: Spring Security + JWT (jjwt 0.12.3)

### Database
- **Development**: H2 Database (in-memory)
- **Production**: PostgreSQL (planned)
- **ORM**: Spring Data JPA + Hibernate

### Libraries & Tools
- **Lombok**: 보일러플레이트 코드 감소
- **Validation**: Jakarta Validation API
- **DevTools**: Spring Boot DevTools (hot reload)

## 🏗 시스템 아키텍처

### Layered Architecture

```
┌─────────────────────────────────────┐
│         Controller Layer            │  - REST API 엔드포인트
│    (AuthController, DreamController)│  - 요청/응답 처리
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│          Service Layer              │  - 비즈니스 로직
│    (AuthService, DreamService)      │  - 트랜잭션 관리
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│        Repository Layer             │  - 데이터 접근
│  (UserRepository, DreamRepository)  │  - JPA 쿼리
└─────────────────────────────────────┘
                 ↓
┌─────────────────────────────────────┐
│          Database                   │  - H2 / PostgreSQL
│      (users, dreams tables)         │
└─────────────────────────────────────┘
```

### Security Flow

```
1. Client → POST /api/auth/login
2. Server → 인증 확인 → JWT 토큰 발급
3. Client → Authorization: Bearer {token}
4. JwtAuthenticationFilter → 토큰 검증
5. SecurityContext → 사용자 정보 저장
6. Controller → 비즈니스 로직 실행
```

### Entity Relationship

```
┌─────────────┐         ┌──────────────┐
│    User     │ 1     N │    Dream     │
│─────────────│◄────────│──────────────│
│ id          │         │ id           │
│ nickname    │         │ title        │
│ email       │         │ content      │
│ password    │         │ interpretation│
│ role        │         │ emotionCategory│
│ createdAt   │         │ emotionalAnalysis│
└─────────────┘         │ recommendedSong*│
                        │ user_id (FK) │
                        │ createdAt    │
                        │ updatedAt    │
                        └──────────────┘
```

## 🚀 시작하기

### Prerequisites

시작하기 전에 다음 소프트웨어가 설치되어 있는지 확인하세요:

- **Java 17 이상**
  ```bash
  java -version
  # openjdk version "17.0.x" or higher
  ```

- **Gradle 8.x** (Gradle Wrapper 포함되어 있어 별도 설치 불필요)

- **Git**
  ```bash
  git --version
  ```

### Installation

1. **레포지토리 클론**
   ```bash
   git clone https://github.com/your-username/oop-backend.git
   cd oop-backend
   ```

2. **의존성 설치**
   ```bash
   ./gradlew build
   ```

### Configuration

`src/main/resources/application.yaml` 파일에서 기본 설정을 확인할 수 있습니다:

```yaml
spring:
  profiles:
    active: local  # local, dev, prod

jwt:
  secret: your-secret-key-here  # ⚠️ 운영 환경에서는 반드시 변경
  expiration: 86400000  # 24시간
```

**환경별 설정 파일:**
- `application-local.yaml` - 로컬 개발 (H2 in-memory)
- `application-dev.yaml` - 개발 서버
- `application-prod.yaml` - 운영 서버 (PostgreSQL)

### Running

#### 1. 로컬 개발 모드 (H2 Database)

```bash
./gradlew bootRun
```

서버가 `http://localhost:8080`에서 실행됩니다.

#### 2. H2 Console 접속 (선택사항)

브라우저에서 `http://localhost:8080/h2-console` 접속

- **JDBC URL**: `jdbc:h2:mem:testdb`
- **Username**: `sa`
- **Password**: (비워두기)

#### 3. 특정 프로필로 실행

```bash
# 개발 환경
./gradlew bootRun --args='--spring.profiles.active=dev'

# 운영 환경
./gradlew bootRun --args='--spring.profiles.active=prod'
```

## 📚 API 문서

### 빠른 시작 가이드

상세한 API 문서는 다음 파일들을 참고하세요:

- **Docs/API명세서.md** - 전체 API 명세 및 예제
- **[POSTMAN_TEST_GUIDE.md](POSTMAN_TEST_GUIDE.md)** - Postman 테스트 가이드

### API 엔드포인트 요약

#### 인증 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/api/auth/signup` | 회원가입 | ❌ |
| POST | `/api/auth/login` | 로그인 | ❌ |
| GET | `/api/auth/me` | 현재 사용자 정보 | ✅ |

#### 꿈 관리 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| POST | `/api/dreams` | 꿈 등록 | ✅ |
| GET | `/api/dreams` | 내 꿈 목록 조회 | ✅ |
| GET | `/api/dreams/{id}` | 꿈 상세 조회 | ✅ |
| DELETE | `/api/dreams/{id}` | 꿈 삭제 | ✅ |

#### 사용자 API

| Method | Endpoint | 설명 | 인증 |
|--------|----------|------|------|
| GET | `/api/users/{userId}` | 사용자 프로필 조회 | ❌ |

### 예제 요청

#### 회원가입
```bash
curl -X POST http://localhost:8080/api/auth/signup \
  -H "Content-Type: application/json" \
  -d '{
    "nickname": "김철수",
    "email": "chulsoo@example.com",
    "password": "password123"
  }'
```

#### 로그인
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "chulsoo@example.com",
    "password": "password123"
  }'
```

#### 꿈 등록 (인증 필요)
```bash
curl -X POST http://localhost:8080/api/dreams \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer {YOUR_JWT_TOKEN}" \
  -d '{
    "title": "하늘을 나는 꿈",
    "content": "오늘 꿈에서 새처럼 하늘을 자유롭게 날아다녔습니다."
  }'
```

## 📁 프로젝트 구조

```
backend/
├── src/
│   ├── main/
│   │   ├── java/com/smu/oop/
│   │   │   ├── BackendApplication.java       # 메인 애플리케이션
│   │   │   ├── config/                       # 설정 클래스
│   │   │   │   ├── JpaConfig.java           # JPA Auditing 설정
│   │   │   │   ├── SecurityConfig.java      # Spring Security 설정
│   │   │   │   ├── JwtAuthenticationFilter.java
│   │   │   │   └── WebConfig.java           # CORS 설정
│   │   │   ├── controller/                  # REST 컨트롤러
│   │   │   │   ├── AuthController.java      # 인증 API
│   │   │   │   ├── DreamController.java     # 꿈 관리 API
│   │   │   │   └── UserController.java      # 사용자 API
│   │   │   ├── dto/                         # 데이터 전송 객체
│   │   │   │   ├── ApiResponse.java         # 공통 응답 형식
│   │   │   │   ├── ErrorResponse.java       # 에러 응답 형식
│   │   │   │   ├── *Request.java            # 요청 DTO
│   │   │   │   └── *Response.java           # 응답 DTO
│   │   │   ├── entity/                      # JPA 엔티티
│   │   │   │   ├── BaseEntity.java          # 공통 필드 (id, createdAt, updatedAt)
│   │   │   │   ├── User.java                # 사용자 엔티티
│   │   │   │   ├── Dream.java               # 꿈 엔티티
│   │   │   │   └── EmotionCategory.java     # 감정 카테고리 Enum
│   │   │   ├── repository/                  # JPA Repository
│   │   │   ├── service/                     # 비즈니스 로직
│   │   │   ├── security/                    # JWT 관련
│   │   │   │   ├── JwtTokenProvider.java    # JWT 생성/검증
│   │   │   │   └── CustomUserDetailsService.java
│   │   │   └── exception/                   # 예외 처리
│   │   │       ├── ErrorCode.java           # 에러 코드 정의
│   │   │       ├── BusinessException.java   # 커스텀 예외
│   │   │       └── GlobalExceptionHandler.java
│   │   └── resources/
│   │       ├── application.yaml              # 기본 설정
│   │       ├── application-local.yaml        # 로컬 환경 설정
│   │       ├── application-dev.yaml          # 개발 환경 설정
│   │       └── application-prod.yaml         # 운영 환경 설정
│   └── test/
│       └── java/com/smu/oop/
│           └── BackendApplicationTests.java  # 통합 테스트
├── build.gradle                              # Gradle 빌드 스크립트
├── settings.gradle
├── CLAUDE.md                                 # 개발 가이드
├── POSTMAN_TEST_GUIDE.md                    # Postman 테스트 가이드
└── README.md                                 # 프로젝트 문서
```

## 💻 개발 가이드

### 코드 스타일

#### Entity 작성 규칙
- 모든 엔티티는 `BaseEntity`를 상속받습니다
- `@SuperBuilder`를 사용하여 빌더 패턴을 구현합니다
- 생성자는 `protected` 접근 제한자를 사용합니다

```java
@Entity
@Table(name = "dreams")
@Getter
@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Dream extends BaseEntity {
    // fields
}
```

#### 예외 처리
- 비즈니스 예외는 `BusinessException`을 사용합니다
- `ErrorCode` enum에 에러 코드를 정의합니다

```java
throw new BusinessException(ErrorCode.USER_NOT_FOUND, "ID: " + userId);
```

#### DTO 변환
- Entity → DTO 변환은 정적 팩토리 메서드 `from()`을 사용합니다

```java
public static UserResponse from(User user) {
    return UserResponse.builder()
        .id(user.getId())
        .nickname(user.getNickname())
        // ...
        .build();
}
```

### 브랜치 전략

- `main` - 운영 배포 브랜치
- `dev` - 개발 통합 브랜치
- `feature/{feature-name}` - 기능 개발 브랜치
- `hotfix/{issue-name}` - 긴급 수정 브랜치

### 커밋 메시지 규칙

```
feat: 새로운 기능 추가
fix: 버그 수정
docs: 문서 수정
refactor: 코드 리팩토링
test: 테스트 코드
```

**예시:**
```
feat: Add emotion analysis feature for dreams
fix: Fix JWT token validation error
docs: Update API documentation
```

## 🧪 테스트

### 테스트 실행

```bash
# 전체 테스트 실행
./gradlew test

# 상세 로그 포함
./gradlew test --info

# 특정 테스트 클래스 실행
./gradlew test --tests "com.smu.oop.BackendApplicationTests"
```

### API 테스트 (Postman)

상세한 테스트 가이드는 [POSTMAN_TEST_GUIDE.md](POSTMAN_TEST_GUIDE.md)를 참고하세요.

**기본 테스트 시나리오:**
1. 회원가입 → 로그인 → JWT 토큰 획득
2. 꿈 등록
3. 내 꿈 목록 조회
4. 특정 꿈 상세 조회
5. 꿈 삭제
6. 다른 사용자의 꿈 접근 시도 (403 에러 확인)

## 🚢 배포

### 빌드

```bash
# 프로젝트 빌드
./gradlew clean build

# JAR 파일 위치
# build/libs/backend-0.0.1-SNAPSHOT.jar
```

### 운영 환경 실행

```bash
java -jar build/libs/backend-0.0.1-SNAPSHOT.jar \
  --spring.profiles.active=prod \
  --jwt.secret=YOUR_PRODUCTION_SECRET_KEY
```

### 환경 변수 설정

운영 환경에서는 민감한 정보를 환경 변수로 관리합니다:

```bash
export JWT_SECRET=your-production-secret-key
export DB_URL=jdbc:postgresql://localhost:5432/mongeum
export DB_USERNAME=postgres
export DB_PASSWORD=your-db-password
```

## 🔧 트러블슈팅

### 자주 발생하는 문제

#### 1. H2 Console 접속 불가

**문제**: `http://localhost:8080/h2-console` 접속 시 404 에러

**해결**:
- `application.yaml`에서 `spring.profiles.active: local` 확인
- H2 Console이 활성화되어 있는지 확인

#### 2. JWT 토큰 에러

**문제**: `Invalid or expired token` 에러

**해결**:
- 토큰이 24시간 이내인지 확인
- Authorization 헤더 형식 확인: `Bearer {token}`
- JWT Secret Key가 올바른지 확인

#### 3. CORS 에러

**문제**: 프론트엔드에서 API 호출 시 CORS 에러

**해결**:
- `WebConfig.java`에서 허용된 origin 확인
- 개발 중이라면 `allowedOrigins("*")` 설정

#### 4. Gradle 빌드 실패

**문제**: `Could not resolve dependencies`

**해결**:
```bash
# Gradle 캐시 삭제
./gradlew clean --refresh-dependencies

# Gradle Wrapper 재설치
./gradlew wrapper --gradle-version 8.5
```

## 🗺 로드맵

### v1.0
- [x] 사용자 인증 (회원가입, 로그인)
- [x] JWT 기반 인증
- [x] 꿈 CRUD 기능
- [x] 감정 분석 (Mock)
- [x] 음악 추천 (Mock)

### v1.1
- [ ] AI 서버 연동
  - [ ] 실시간 꿈 해몽
  - [ ] 감정 분석 자동화
  - [ ] 음악 추천 고도화
- [ ] 검색 기능
  - [ ] 키워드 기반 꿈 검색
  - [ ] 감정 카테고리별 필터링
- [ ] 통계 기능
  - [ ] 월별 꿈 통계
  - [ ] 감정 분포 차트

[//]: # (### v2.0 &#40;계획&#41;)

[//]: # (- [ ] 소셜 기능)

[//]: # (  - [ ] 꿈 공유)

[//]: # (  - [ ] 댓글 기능)

[//]: # (  - [ ] 좋아요 기능)

[//]: # (- [ ] 알림 기능)

[//]: # (  - [ ] 꿈 기록 리마인더)

[//]: # (  - [ ] 주간 리포트)

[//]: # (- [ ] 데이터 내보내기)

[//]: # (  - [ ] PDF 내보내기)

[//]: # (  - [ ] CSV 내보내기)

[//]: # (## 🤝 기여하기)

[//]: # ()
[//]: # (프로젝트에 기여해주셔서 감사합니다!)

[//]: # ()
[//]: # (### 기여 방법)

[//]: # ()
[//]: # (1. 이 레포지토리를 Fork 합니다)

[//]: # (2. 새 브랜치를 생성합니다 &#40;`git checkout -b feature/AmazingFeature`&#41;)

[//]: # (3. 변경사항을 커밋합니다 &#40;`git commit -m 'feat: Add some AmazingFeature'`&#41;)

[//]: # (4. 브랜치에 Push 합니다 &#40;`git push origin feature/AmazingFeature`&#41;)

[//]: # (5. Pull Request를 생성합니다)

[//]: # ()
[//]: # (### 코드 리뷰 기준)

[//]: # ()
[//]: # (- [ ] 코드가 프로젝트의 코딩 스타일을 따르는가?)

[//]: # (- [ ] 모든 테스트가 통과하는가?)

[//]: # (- [ ] 새로운 기능에 대한 테스트가 추가되었는가?)

[//]: # (- [ ] 문서가 업데이트되었는가?)

[//]: # ()
[//]: # (### SMU OOP Team Project)

[//]: # ()
[//]: # (이 프로젝트는 상명대학교&#40;SMU&#41; 객체지향프로그래밍 팀 프로젝트의 일환으로 개발되었습니다.)

---

*Last Updated: 2025-11-21*
