package com.dohyundev.review.auth.adapter.out.persistence

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.hibernate.annotations.NaturalId
import org.hibernate.annotations.NaturalIdCache
import java.time.LocalDateTime

@Entity
@Table(name = "refresh_token")
@NaturalIdCache
class RefreshTokenJpaEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @NaturalId
    @Column(nullable = false, updatable = false)
    val refreshToken: String,

    @Column(nullable = false)
    val employeeId: Long,

    @Column(nullable = false)
    val expiresAt: LocalDateTime,
)