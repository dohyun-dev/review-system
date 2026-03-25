package com.dohyundev.review.duty.fixture

import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.duty.domain.DutyStatus

object DutyFixture {
    fun create(
        name: String = "팀장",
        sortOrder: Int = 1,
        status: DutyStatus = DutyStatus.ACTIVE,
    ): Duty {
        return Duty(
            name = name,
            sortOrder = sortOrder,
            status = status,
        )
    }
}
