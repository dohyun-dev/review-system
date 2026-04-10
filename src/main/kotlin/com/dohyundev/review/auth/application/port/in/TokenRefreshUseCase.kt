package com.dohyundev.review.auth.application.port.`in`

import com.dohyundev.review.auth.domain.TokenSet

interface TokenRefreshUseCase {
    fun refresh(refreshToken: String): TokenSet
}