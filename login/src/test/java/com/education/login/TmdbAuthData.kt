package com.education.login

const val request_token = "f6dfeab156c7317faa24aee64360f9194b34018d"
const val expires_at = "2020-03-14 21:58:53 UTC"
const val session_id = "f7a0c6e5f837f5c805581b4397c02b4f0b5f89cd"
const val requestTokenResponseBody = """{
    "success": true,
    "expires_at": $expires_at,
    "request_token": $request_token
  }"""

const val createSessionWithLoginResponseBody = """{
  "success": true,
  "expires_at": $expires_at,
  "request_token": $request_token
}"""

const val createSessionIdResponseBody = """{
  "success": true,
  "session_id": $session_id
}"""

const val createSessionIdFalseResponseBoolean = """{
  "success": false,
  "session_id": ""
}"""

const val convertedTimeToLong = 1584223133000 // 2020-03-14 21:58:53 UTC
