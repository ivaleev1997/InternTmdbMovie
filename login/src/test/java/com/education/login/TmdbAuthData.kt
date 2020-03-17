package com.education.login

const val requestTokenResponseBody = """{
    "success": true,
    "expires_at": "2020-03-17 18:58:53 UTC",
    "request_token": "f6dfeab156c7317faa24aee64360f9194b34018d"
  }"""

const val createSessionWithLoginResponseBody = """{
  "success": true,
  "expires_at": "2020-03-17 18:58:53 UTC",
  "request_token": "f6dfeab156c7317faa24aee64360f9194b34018d"
}"""

const val createSessionIdResponseBody = """{
  "success": true,
  "session_id": "f7a0c6e5f837f5c805581b4397c02b4f0b5f89cd"
}"""

const val createSessionIdFalseResponseBoolean = """{
  "success": false,
  "session_id": ""
}"""