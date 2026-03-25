package com.dohyundev.review.duty.application.port.`in`

import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.duty.application.port.dto.CreateDutyCommand

interface CreateDutyUseCase {
    fun create(command: CreateDutyCommand): Duty
}
