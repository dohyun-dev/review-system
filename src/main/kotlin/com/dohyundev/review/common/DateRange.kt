package com.dohyundev.review.common

import jakarta.persistence.Embeddable
import java.time.LocalDate

@Embeddable
data class DateRange(
    val from: LocalDate,
    val to: LocalDate = LocalDate.MAX,
) {
    init {
        require(!from.isAfter(to))
            { "유효하지 않은 날짜 범위입니다. 시작일은 종료일보다 이전이거나 같아야 합니다. 시작일=$from, 종료일=$to" }
    }

    fun contains(date: LocalDate): Boolean {
        return !date.isBefore(from) && !date.isAfter(to)
    }
}
