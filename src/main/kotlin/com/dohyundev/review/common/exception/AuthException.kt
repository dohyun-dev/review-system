package com.dohyundev.review.common.exception

open class AuthException(
    message: String = "인증에 실패했습니다.",
    cause: Throwable? = null,
) : RuntimeException(message, cause)
