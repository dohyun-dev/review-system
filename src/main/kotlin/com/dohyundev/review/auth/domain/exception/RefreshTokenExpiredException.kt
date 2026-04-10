package com.dohyundev.review.auth.domain.exception

class RefreshTokenExpiredException : InvalidTokenException("리프레시 토큰이 만료되었습니다.")
