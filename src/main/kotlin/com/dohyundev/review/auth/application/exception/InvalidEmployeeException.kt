package com.dohyundev.review.auth.application.exception

import com.dohyundev.review.common.exception.AuthException

class InvalidEmployeeException : AuthException("유효하지 않은 사원입니다.")
