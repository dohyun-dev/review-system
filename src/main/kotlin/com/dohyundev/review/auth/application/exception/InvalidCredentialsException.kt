package com.dohyundev.review.auth.application.exception

import com.dohyundev.review.common.exception.AuthException

class InvalidCredentialsException : AuthException("아이디 또는 비밀번호가 올바르지 않습니다.")
