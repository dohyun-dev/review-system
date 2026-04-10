package com.dohyundev.review.auth.domain

import java.time.LocalDateTime

data class TokenSet(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: LocalDateTime,
    val refreshTokenExpiresAt: LocalDateTime,
)