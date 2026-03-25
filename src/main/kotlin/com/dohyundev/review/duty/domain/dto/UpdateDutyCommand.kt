package com.dohyundev.review.duty.domain.dto

import com.dohyundev.review.duty.domain.DutyStatus

data class UpdateDutyCommand(
    val name: String,
    val sortOrder: Int,
    val status: DutyStatus,
)
