package com.dohyundev.review.auth.adapter.out.persistence

import org.springframework.data.jpa.repository.JpaRepository

interface RefreshTokenJpaRepository : JpaRepository<RefreshTokenJpaEntity, Long> {
    fun findByRefreshToken(refreshToken: String): RefreshTokenJpaEntity?

    fun deleteByRefreshToken(refreshToken: String)
}
