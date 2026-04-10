package com.dohyundev.review.auth.domain.exception

class ExpiredTokenException(cause: Throwable? = null) : InvalidTokenException("토큰이 만료되었습니다.", cause)
