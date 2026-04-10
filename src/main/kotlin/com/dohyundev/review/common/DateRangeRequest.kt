package com.dohyundev.review.common

import jakarta.validation.constraints.AssertTrue
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class DateRangeRequest(
    @field:NotNull(message = "시작일은 필수입니다.")
    var from: LocalDate,
    @field:NotNull(message = "종료일은 필수입니다.")
    var to: LocalDate,
) {
    @AssertTrue(message = "종료일은 시작일보다 이후이거나 같아야 합니다.")
    fun isValidRange(): Boolean = !from.isAfter(to)

    fun toDateRange(): DateRange = DateRange(from, to)
}
