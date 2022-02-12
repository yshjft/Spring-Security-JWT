# Spring Security with JWT

## 기술 스택
* Spring Boot
* Spring Security
* Spring Data Jpa
* h2
* redis

## 설명
Spring Security와 JWT를 이용하여 간단한 인증 api를 구현해보았습니다. 기능 보다는 Spring Security와 JWT에 대한 이해에 초점을 두었습니다.

* 로그인을 하면 access token과 refresh token을 발급합니다.
  * access token은 30분, refresh token은 7주일로 만료 시간을 설정합니다.
  * refresh token은 redis에 저장됩니다. 장동 제거 시간은 토큰의 만료시간과 동일하게 설정합니다.
  * access token은 request body로 refresh token은 Set-Cookie 헤더로 전송합니다.


* access token이 Authorization 헤더에 넣어 요청과 함께 서버로 보냅니다.
  * 서버는 access token이 유효한지 확인합니다
  * access token이 없는 요청은 인증할 수 없음(401)을 읍답합니다.
  * access token이 만료된 경우 token을 갱신하도록 응답(401)을 보냅니다.
  * 이외 access token에 문제가 있는 경우 다시 로그인 하도록 응답(401)을 보냅니다.


* access token 갱신을 위해 access token과 refresh token을 서버로 전송합니다.
  * 이전과 동일하게 access token과 refresh token 모두 request header에 담아 전송합니다.
  * 만약 이때 access token에 문제가 있다면 다시 로그인 하도록 응답(401)을 보냅니다.
  * refresh token이 존재하지 않거나 동일하지 않다면 다시 로그인 하도록 응답(401)을 보냅니다.
  * refresh token이 존재하며 전송된 토큰과 저장된 토큰이 동일하다면 access token을 재발급합니다.


* 사용자가 로그아웃을 합니다
  * 이전과 동일하게 access token과 refresh token 모두 request header에 담아 전송합니다.
  * 문제가 없다면 정상적으로 로그아웃이 진행됩니다.
    * redis에 저장된 refresh token 제거
    * access token은 남은 유효 시간 만큼 redis에 저장합니다.(black list) → 이를 통해 로그아웃된 사용자의 access token이 사용되는 것을 막을 수 있습니다.


## API
* Sign Up   
[POST] /api/user/signUp
    * REQUEST
    ```
    {
        "name": "j",
        "email": "j@test.com",
        "password": "jjjj",
        "roles": ["ROLE_USER","ROLE_ADMIN"]
    }
    ```
    * RESPONSE(SUCCESS)
    ```
    {
        "status": 201,
        "message": "sign up success",
        "result": {
            "name": "j",
            "email": "j@test.com",
            "roles": [
                "ROLE_ADMIN",
                "ROLE_USER"
            ]
        }
    }    
    ```
    * RESPONSE(FAIL)
    ```
    {
        "status": 400,
        "message": "Bad Request",
        "code": "40001",
        "error": {
            "detail": "duplicate user"
        }
    }  
    ```


* Sign In
[POST] /api/auth/signIn
    * REQUEST
    ```
    {
        "email": "jerry@test.com",
        "password": "jerry"
    }
    ```
    * RESPONSE
    ```
    {
        "status": 200,
        "message": "OK",
        "result": {
            "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3MzcwNCwiZXhwIjoxNjQ0NTc3MzA0fQ.Q9tHXygfcCssInkTsytH7PBNbtOZrfGpmoPXgaH1BuoFd6rFAMD7A-p540nWRN8ducmDQQMoKNJvOHs3OqSsFw"
        }
    }
    ```

* refresh token    
    * REQUEST
    ```
    [Request Header]
    Cookie : refreshToken = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3MzcwNCwiZXhwIjoxNjQ0NTc3MzA0fQ.Q9tHXygfcCssInkTsytH7PBNbtOZrfGpmoPXgaH1BuoFd6rFAMD7A-p540nWRN8ducmDQQMoKNJvOHs3OqSsFw 
    Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3MzcwNCwiZXhwIjoxNjQ0NTc3MzA0fQ.Q9tHXygfcCssInkTsytH7PBNbtOZrfGpmoPXgaH1BuoFd6rFAMD7A-p540nWRN8ducmDQQMoKNJvOHs3OqSsFw
    ```
  
    * RESPONSE
    ```
    {
        "status": 200,
        "message": "OK",
        "result": {
            "accessToken": "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3NTMyMywiZXhwIjoxNjQ0NTc4OTIzfQ.Oinp6aHgTimk3v0Cmt6ILJhnMDs1HHwdinxJuuDo93xEhGrIw_UhVSGdLqYYnZ2boOa0B8dq_WgighGcRNgR_g"
        }
    }
    ```

* sign out
    * REQUEST
    ```
    [Request Header]
    Cookie : refreshToken = eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3MzcwNCwiZXhwIjoxNjQ0NTc3MzA0fQ.Q9tHXygfcCssInkTsytH7PBNbtOZrfGpmoPXgaH1BuoFd6rFAMD7A-p540nWRN8ducmDQQMoKNJvOHs3OqSsFw 
    Authorization: Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJBQ0NFU1NfVE9LRU4iLCJVU0VSX0VNQUlMIjoiamVycnlAdGVzdC5jb20iLCJBVVRIT1JJVElFUyI6IlJPTEVfVVNFUiIsImlhdCI6MTY0NDU3NTMyMywiZXhwIjoxNjQ0NTc4OTIzfQ.Oinp6aHgTimk3v0Cmt6ILJhnMDs1HHwdinxJuuDo93xEhGrIw_UhVSGdLqYYnZ2boOa0B8dq_WgighGcRNgR_g
    ```
  
    * RESPONSE
    ```
    {
        "status": 200,
        "message": "logout success"
    }
    ```
  

