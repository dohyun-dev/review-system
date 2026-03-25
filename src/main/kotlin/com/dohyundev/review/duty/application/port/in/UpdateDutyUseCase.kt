package com.dohyundev.review.duty.application.port.`in`

import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.duty.domain.dto.UpdateDutyCommand

interface UpdateDutyUseCase {
    fun update(id: Long, command: UpdateDutyCommand): Duty
}
