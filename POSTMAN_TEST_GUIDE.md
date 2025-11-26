# Postman API 테스트 가이드

## 사전 준비

1. **애플리케이션 실행**
```bash
./gradlew bootRun
```
서버가 `http://localhost:8080`에서 실행됩니다.

2. **H2 콘솔 확인** (선택사항)
- URL: http://localhost:8080/h2-console
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (비어있음)

3. **환경 변수 설정**
```text
OPENAI_API_KEY=your-openai-api-key
YOUTUBE_API_KEY=your-youtube-api-key
JWT_SECRET=your-jwt-secret
```

---

## 1️⃣ 회원가입 (Sign Up)

### Request
```
POST http://localhost:8080/api/auth/signup
Content-Type: application/json
```

### Body (JSON)
```json
{
  "nickname": "김철수",
  "email": "chulsoo@example.com",
  "password": "password123"
}
```

### Expected Response (201 Created)
```json
{
  "success": true,
  "message": "User registered successfully",
  "data": {
    "id": 1,
    "nickname": "김철수",
    "email": "chulsoo@example.com",
    "createdAt": "2025-11-12T16:50:00"
  }
}
```

### 추가 테스트 데이터
```json
// 사용자 2
{
  "nickname": "이영희",
  "email": "younghee@example.com",
  "password": "password456"
}

// 사용자 3
{
  "nickname": "박민수",
  "email": "minsu@example.com",
  "password": "password789"
}
```

---

## 2️⃣ 로그인 (Login)

### Request
```
POST http://localhost:8080/api/auth/login
Content-Type: application/json
```

### Body (JSON)
```json
{
  "email": "chulsoo@example.com",
  "password": "password123"
}
```

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Login successful",
  "data": {
    "accessToken": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjaHVsc29vQGV4YW1wbGUuY29tIiwiaWF0IjoxNjk5ODc2NTQzLCJleHAiOjE2OTk5NjI5NDN9.xxxxx",
    "tokenType": "Bearer",
    "expiresIn": 86400000,
    "userId": 1,
    "nickname": "김철수",
    "email": "chulsoo@example.com"
  }
}
```

### ⚠️ 중요: JWT 토큰 저장
로그인 후 받은 `accessToken`을 복사하여 이후 모든 요청에 사용합니다.

**Postman 설정 방법:**
1. 로그인 응답에서 `accessToken` 복사
2. Collection 또는 Environment에 변수 설정:
   - Variable: `jwt_token`
   - Value: (복사한 토큰)
3. 이후 요청의 Authorization 탭에서:
   - Type: `Bearer Token`
   - Token: `{{jwt_token}}`

---

## 3️⃣ 현재 사용자 정보 조회 (Get Current User)

### Request
```
GET http://localhost:8080/api/auth/me
Authorization: Bearer {{jwt_token}}
```

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "nickname": "김철수",
    "email": "chulsoo@example.com",
    "createdAt": "2025-11-12T16:50:00"
  }
}
```

---

## 4️⃣ 꿈 등록 (Create Dream)

### Request
```
POST http://localhost:8080/api/dreams
Authorization: Bearer {{jwt_token}}
Content-Type: application/json
```

### Body (JSON)
```json
{
  "title": "하늘을 나는 꿈",
  "content": "오늘 꿈에서 새처럼 하늘을 자유롭게 날아다녔습니다. 구름 사이를 지나가며 아름다운 풍경을 보았고, 매우 상쾌하고 자유로운 기분이었습니다."
}
```

### Expected Response (201 Created)
```json
{
  "success": true,
  "message": "Dream created successfully",
  "data": {
    "id": 1,
    "title": "하늘을 나는 꿈",
    "content": "오늘 꿈에서 새처럼 하늘을 자유롭게 날아다녔습니다. 구름 사이를 지나가며 아름다운 풍경을 보았고, 매우 상쾌하고 자유로운 기분이었습니다.",
    "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와 연동되면 자동으로 분석됩니다. 현재는 임시 해몽 메시지가 표시됩니다. 꿈의 내용을 바탕으로 AI가 상징적 의미와 심리적 해석을 제공할 예정입니다.",
    "emotionCategory": "PEACEFUL",
    "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다. AI 서버와 연동되면 더 상세한 감정 분석이 제공됩니다. 꿈의 내용과 맥락을 바탕으로 감정의 원인과 의미를 분석할 예정입니다.",
    "recommendedSongName": "Moonlight Sonata",
    "recommendedArtist": "Ludwig van Beethoven",
    "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
    "createdAt": "2025-11-12T17:00:00",
    "updatedAt": "2025-11-12T17:00:00"
  }
}
```

### 추가 테스트 데이터
```json
// 꿈 2
{
  "title": "바다에서 수영하는 꿈",
  "content": "맑고 푸른 바다에서 물고기들과 함께 수영을 했습니다. 물속이 너무 편안하고 숨쉬기도 자유로웠습니다."
}

// 꿈 3
{
  "title": "시험을 보는 꿈",
  "content": "중요한 시험을 보는데 문제를 전혀 풀 수 없었습니다. 시간은 계속 지나가고 너무 불안했습니다."
}

// 꿈 4
{
  "title": "옛 친구를 만나는 꿈",
  "content": "오랜만에 초등학교 친구를 만났습니다. 함께 웃으며 이야기를 나누고 즐거운 시간을 보냈습니다."
}
```

---

## 5️⃣ 내 꿈 목록 조회 (Get My Dreams)

### Request
```
GET http://localhost:8080/api/dreams
Authorization: Bearer {{jwt_token}}
```

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Success",
  "data": [
    {
      "id": 4,
      "title": "옛 친구를 만나는 꿈",
      "content": "오랜만에 초등학교 친구를 만났습니다. 함께 웃으며 이야기를 나누고 즐거운 시간을 보냈습니다.",
      "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와 연동되면 자동으로 분석됩니다...",
      "emotionCategory": "PEACEFUL",
      "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다...",
      "recommendedSongName": "Moonlight Sonata",
      "recommendedArtist": "Ludwig van Beethoven",
      "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
      "createdAt": "2025-11-12T17:05:00",
      "updatedAt": "2025-11-12T17:05:00"
    },
    {
      "id": 3,
      "title": "시험을 보는 꿈",
      "content": "중요한 시험을 보는데 문제를 전혀 풀 수 없었습니다...",
      "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와 연동되면 자동으로 분석됩니다...",
      "emotionCategory": "PEACEFUL",
      "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다...",
      "recommendedSongName": "Moonlight Sonata",
      "recommendedArtist": "Ludwig van Beethoven",
      "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
      "createdAt": "2025-11-12T17:03:00",
      "updatedAt": "2025-11-12T17:03:00"
    }
  ]
}
```

> 💡 **Note**: 최신 순으로 정렬되어 반환됩니다.

---

## 6️⃣ 꿈 상세 조회 (Get Dream Detail)

### Request
```
GET http://localhost:8080/api/dreams/1
Authorization: Bearer {{jwt_token}}
```

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "title": "하늘을 나는 꿈",
    "content": "오늘 꿈에서 새처럼 하늘을 자유롭게 날아다녔습니다. 구름 사이를 지나가며 아름다운 풍경을 보았고, 매우 상쾌하고 자유로운 기분이었습니다.",
    "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와 연동되면 자동으로 분석됩니다. 현재는 임시 해몽 메시지가 표시됩니다. 꿈의 내용을 바탕으로 AI가 상징적 의미와 심리적 해석을 제공할 예정입니다.",
    "emotionCategory": "PEACEFUL",
    "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다. AI 서버와 연동되면 더 상세한 감정 분석이 제공됩니다. 꿈의 내용과 맥락을 바탕으로 감정의 원인과 의미를 분석할 예정입니다.",
    "recommendedSongName": "Moonlight Sonata",
    "recommendedArtist": "Ludwig van Beethoven",
    "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
    "createdAt": "2025-11-12T17:00:00",
    "updatedAt": "2025-11-12T17:00:00",
    "user": {
      "id": 1,
      "nickname": "김철수",
      "email": "chulsoo@example.com",
      "createdAt": "2025-11-12T16:50:00"
    }
  }
}
```

### 권한 체크 테스트
다른 사용자의 꿈에 접근 시도:
```
GET http://localhost:8080/api/dreams/999
Authorization: Bearer {{jwt_token}}
```

**Expected Response (403 Forbidden)**
```json
{
  "timestamp": "2025-11-12T17:10:00",
  "status": 403,
  "error": "D002",
  "message": "Access denied to this dream: Dream ID: 999",
  "path": "/api/dreams/999"
}
```

---

## 7️⃣ 꿈 삭제 (Delete Dream)

### Request
```
DELETE http://localhost:8080/api/dreams/1
Authorization: Bearer {{jwt_token}}
```

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Dream deleted successfully",
  "data": null
}
```

### 권한 체크 테스트
다른 사용자의 꿈 삭제 시도 시 403 에러 발생

---

## 8️⃣ 사용자 상세 정보 조회 (Get User Detail with Dreams)

### Request
```
GET http://localhost:8080/api/users/1
```

> ⚠️ **Note**: 이 API는 인증 없이도 접근 가능 (공개 프로필)

### Expected Response (200 OK)
```json
{
  "success": true,
  "message": "Success",
  "data": {
    "id": 1,
    "nickname": "김철수",
    "email": "chulsoo@example.com",
    "createdAt": "2025-11-12T16:50:00",
    "dreams": [
      {
        "id": 4,
        "title": "옛 친구를 만나는 꿈",
        "content": "오랜만에 초등학교 친구를 만났습니다...",
        "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와...",
        "emotionCategory": "PEACEFUL",
        "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다...",
        "recommendedSongName": "Moonlight Sonata",
        "recommendedArtist": "Ludwig van Beethoven",
        "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
        "createdAt": "2025-11-12T17:05:00",
        "updatedAt": "2025-11-12T17:05:00"
      },
      {
        "id": 3,
        "title": "시험을 보는 꿈",
        "content": "중요한 시험을 보는데 문제를 전혀 풀 수 없었습니다...",
        "interpretation": "해몽 결과: 이 꿈은 AI 해몽 서버와...",
        "emotionCategory": "PEACEFUL",
        "emotionalAnalysis": "감정 분석 결과: 이 꿈은 '평온'한 감정을 나타냅니다...",
        "recommendedSongName": "Moonlight Sonata",
        "recommendedArtist": "Ludwig van Beethoven",
        "recommendedSongUrl": "https://www.youtube.com/watch?v=4Tr0otuiQuU",
        "createdAt": "2025-11-12T17:03:00",
        "updatedAt": "2025-11-12T17:03:00"
      }
    ]
  }
}
```

---

## 🧪 에러 케이스 테스트

### 1. 유효성 검증 실패
**Request**: 회원가입 시 잘못된 이메일 형식
```json
{
  "nickname": "테스트",
  "email": "invalid-email",
  "password": "123"
}
```

**Response (400 Bad Request)**
```json
{
  "success": false,
  "message": "Validation failed: {password=Password must be at least 6 characters, email=Email should be valid}",
  "data": null
}
```

### 2. 이메일 중복
**Request**: 이미 존재하는 이메일로 회원가입
```json
{
  "nickname": "중복테스트",
  "email": "chulsoo@example.com",
  "password": "password123"
}
```

**Response (409 Conflict)**
```json
{
  "timestamp": "2025-11-12T17:15:00",
  "status": 409,
  "error": "U002",
  "message": "User already exists with this email: chulsoo@example.com",
  "path": "/api/auth/signup"
}
```

### 3. 로그인 실패
**Request**: 잘못된 비밀번호
```json
{
  "email": "chulsoo@example.com",
  "password": "wrongpassword"
}
```

**Response (401 Unauthorized)**
```json
{
  "timestamp": "2025-11-12T17:16:00",
  "status": 401,
  "error": "U003",
  "message": "Invalid email or password",
  "path": "/api/auth/login"
}
```

### 4. 인증 없이 보호된 API 접근
**Request**: JWT 토큰 없이 꿈 목록 조회
```
GET http://localhost:8080/api/dreams
(No Authorization Header)
```

**Response (403 Forbidden)**

### 5. 존재하지 않는 리소스
**Request**: 존재하지 않는 꿈 조회
```
GET http://localhost:8080/api/dreams/999
Authorization: Bearer {{jwt_token}}
```

**Response (404 Not Found)**
```json
{
  "timestamp": "2025-11-12T17:18:00",
  "status": 404,
  "error": "D001",
  "message": "Dream not found: ID: 999",
  "path": "/api/dreams/999"
}
```

---

## 📝 테스트 시나리오 (전체 플로우)

### 시나리오 1: 신규 사용자 가입부터 꿈 관리까지

1. **회원가입** → 사용자 생성
2. **로그인** → JWT 토큰 획득
3. **현재 사용자 정보 조회** → 토큰 검증
4. **꿈 등록 (3개)** → 꿈 데이터 생성
5. **내 꿈 목록 조회** → 등록한 꿈 확인
6. **특정 꿈 상세 조회** → 상세 정보 확인
7. **사용자 상세 정보 조회** → 프로필 + 꿈 목록 확인
8. **꿈 삭제** → 특정 꿈 삭제
9. **내 꿈 목록 조회** → 삭제 확인

### 시나리오 2: 권한 체크 테스트

1. **사용자 A 회원가입 및 로그인**
2. **사용자 A가 꿈 등록**
3. **사용자 B 회원가입 및 로그인**
4. **사용자 B가 사용자 A의 꿈 조회 시도** → 403 에러 확인
5. **사용자 B가 사용자 A의 꿈 삭제 시도** → 403 에러 확인

---

## 🔧 Postman Collection 설정 팁

### Environment Variables 설정
```
baseUrl: http://localhost:8080
jwt_token: (로그인 후 자동 설정)
```

### Tests 스크립트 예시 (로그인 API)
로그인 후 자동으로 jwt_token을 환경변수에 저장:

```javascript
// Tests 탭에 추가
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("jwt_token", jsonData.data.accessToken);
    console.log("JWT Token saved:", jsonData.data.accessToken);
}
```

---

## ✅ 체크리스트

- [ ] 서버 실행 확인
- [ ] 회원가입 성공
- [ ] 로그인 및 JWT 토큰 획득
- [ ] 인증이 필요한 API에 토큰 설정
- [ ] 꿈 등록 (최소 2-3개)
- [ ] 꿈 목록 조회
- [ ] 꿈 상세 조회
- [ ] 사용자 상세 정보 조회
- [ ] 꿈 삭제
- [ ] 에러 케이스 테스트 (권한 없음, 리소스 없음 등)

---

## 🎯 추가 기능 테스트 (향후)

현재는 임시 메시지가 표시되지만, AI 서버 연동 후:
- 실시간 AI 해몽 결과 확인
- 감정 분석 결과 및 카테고리 자동 분류 (HAPPY, SAD, ANXIOUS, PEACEFUL, EXCITED, NOSTALGIC, FEARFUL)
- 감정 기반 맞춤 음악 추천 (곡명, 아티스트, URL)
- 해몽 품질 평가
- 키워드 기반 꿈 검색
- 날짜 범위별 꿈 조회
- 감정 카테고리별 꿈 필터링

---

**Happy Testing! 🚀**
