package com.dohyundev.review.auth.domain.exception

import com.dohyundev.review.common.exception.AuthException

open class InvalidTokenException(
    message: String = "유효하지 않은 토큰입니다.",
    cause: Throwable? = null,
) : AuthException(message, cause)
