package com.dohyundev.review.auth.domain

import java.time.LocalDateTime

class RefreshToken(
    val token: String,
    val employeeId: Long,
    val expiresAt: LocalDateTime,
) {
    fun isExpired(): Boolean = LocalDateTime.now().isAfter(expiresAt)
}
