package com.dohyundev.review.auth.application.port.out

import com.dohyundev.review.auth.domain.RefreshToken

interface RefreshTokenRepository {
    fun save(refreshToken: RefreshToken)
    fun findByToken(token: String): RefreshToken?
    fun deleteByToken(token: String)
}
