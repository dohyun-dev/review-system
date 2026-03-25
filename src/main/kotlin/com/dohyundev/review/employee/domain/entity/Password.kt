package com.dohyundev.review.employee.domain.entity

import jakarta.persistence.Column
import jakarta.persistence.Embeddable

@Embeddable
class Password(
    @Column(name = "password_hash", nullable = false)
    val value: String
)