package com.dohyundev.review.review.command.domain.score

import jakarta.persistence.Embeddable

@Embeddable
data class Adjuster(val employeeId: Long)
