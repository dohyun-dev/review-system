package com.dohyundev.review.common

import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class DateRangeTest {

    @Test
    fun `시작일이 종료일보다 늦으면 예외가 발생한다`() {
        // when
        // then
        assertFailsWith<IllegalArgumentException> {
            DateRange(
                from = LocalDate.of(2025, 12, 31),
                to = LocalDate.of(2025, 1, 1),
            )
        }
    }

    @Test
    fun `시작일과 종료일이 같으면 정상 생성된다`() {
        // given
        val date = LocalDate.of(2025, 6, 1)

        // when
        val range = DateRange(from = date, to = date)

        // then
        assertTrue(range.contains(date))
    }

    @Test
    fun `범위 내 날짜를 포함한다`() {
        // given
        val range = DateRange(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )

        // when
        // then
        assertTrue(range.contains(LocalDate.of(2025, 6, 15)))
    }

    @Test
    fun `시작일을 포함한다`() {
        // given
        val range = DateRange(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )

        // when
        // then
        assertTrue(range.contains(LocalDate.of(2025, 1, 1)))
    }

    @Test
    fun `종료일을 포함한다`() {
        // given
        val range = DateRange(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )

        // when
        // then
        assertTrue(range.contains(LocalDate.of(2025, 12, 31)))
    }

    @Test
    fun `범위 이전 날짜는 포함하지 않는다`() {
        // given
        val range = DateRange(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )

        // when
        // then
        assertFalse(range.contains(LocalDate.of(2024, 12, 31)))
    }

    @Test
    fun `범위 이후 날짜는 포함하지 않는다`() {
        // given
        val range = DateRange(
            from = LocalDate.of(2025, 1, 1),
            to = LocalDate.of(2025, 12, 31),
        )

        // when
        // then
        assertFalse(range.contains(LocalDate.of(2026, 1, 1)))
    }
}
