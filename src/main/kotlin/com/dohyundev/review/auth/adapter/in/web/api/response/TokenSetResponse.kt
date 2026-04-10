package com.dohyundev.review.auth.adapter.`in`.web.api.response

import java.time.LocalDateTime

data class TokenSetResponse(
    val accessToken: String,
    val refreshToken: String,
    val accessTokenExpiresAt: LocalDateTime,
    val refreshTokenExpiresAt: LocalDateTime,
)
