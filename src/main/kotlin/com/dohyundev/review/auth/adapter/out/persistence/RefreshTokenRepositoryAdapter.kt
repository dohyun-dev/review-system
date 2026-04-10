package com.dohyundev.review.auth.adapter.out.persistence

import com.dohyundev.review.auth.application.port.out.RefreshTokenRepository
import com.dohyundev.review.auth.domain.RefreshToken
import org.springframework.stereotype.Component

@Component
class RefreshTokenRepositoryAdapter(
    private val refreshTokenJpaRepository: RefreshTokenJpaRepository,
) : RefreshTokenRepository {

    override fun save(refreshToken: RefreshToken) {
        refreshTokenJpaRepository.save(
            RefreshTokenJpaEntity(
                refreshToken = refreshToken.token,
                employeeId = refreshToken.employeeId,
                expiresAt = refreshToken.expiresAt,
            )
        )
    }

    override fun findByToken(token: String): RefreshToken? {
        return refreshTokenJpaRepository.findByRefreshToken(token)
            ?.let { entity ->
                RefreshToken(
                    token = entity.refreshToken,
                    employeeId = entity.employeeId,
                    expiresAt = entity.expiresAt,
                )
            }
    }

    override fun deleteByToken(token: String) {
        refreshTokenJpaRepository.deleteByRefreshToken(token)
        refreshTokenJpaRepository.flush()
    }
}
