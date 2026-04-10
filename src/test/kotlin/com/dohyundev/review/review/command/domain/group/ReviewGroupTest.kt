package com.dohyundev.review.review.command.domain.group

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewTargetCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewerCountException
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import java.time.LocalDate
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class ReviewGroupTest {

    @Test
    fun `리뷰 그룹을 생성한다`() {
        val period = DateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31))
        val targetPeriod = DateRange(LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31))
        val group = ReviewGroup(name = "리뷰", description = "설명", period = period, targetPeriod = targetPeriod)
        assertEquals("리뷰", group.name)
        assertEquals("설명", group.description)
        assertEquals(period, group.period)
        assertEquals(targetPeriod, group.targetPeriod)
    }

    @Test
    fun `리뷰그룹 생성 시 상태는 PREPARING이다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertEquals(ReviewGroupStatus.PREPARING, group.status)
    }

    @Test
    fun `이름이 비어있으면 예외가 발생한다`() {
        assertFailsWith<IllegalArgumentException> { ReviewGroup(name = "  ") }
    }

    @Test
    fun `이름이 25자를 초과하면 예외가 발생한다`() {
        assertFailsWith<IllegalArgumentException> { ReviewGroup(name = "a".repeat(26)) }
    }

    @Test
    fun `이름이 25자이면 정상 생성된다`() {
        val group = ReviewGroup(name = "a".repeat(25))
        assertEquals("a".repeat(25), group.name)
    }

    @Test
    fun `설명이 50자를 초과하면 예외가 발생한다`() {
        assertFailsWith<IllegalArgumentException> { ReviewGroup(name = "리뷰", description = "a".repeat(51)) }
    }

    @Test
    fun `설명이 50자이면 정상 생성된다`() {
        val group = ReviewGroup(name = "리뷰", description = "a".repeat(50))
        assertEquals("a".repeat(50), group.description)
    }

    @Test
    fun `준비 중 상태에서 시작하면 IN_PROGRESS로 변경된다`() {
        val group = ReviewGroupFixture.reviewGroup()
        group.start { }
        assertEquals(ReviewGroupStatus.IN_PROGRESS, group.status)
    }

    @Test
    fun `준비 중이 아닌 상태에서 시작하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup().also { it.start { } }
        assertFailsWith<IllegalStateException> { group.start { } }
    }

    @Test
    fun `리뷰가 없으면 시작할 수 없다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertFailsWith<IllegalStateException> { group.start { throw InsufficientReviewCountException() } }
    }

    @Test
    fun `리뷰 대상자가 없으면 시작할 수 없다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertFailsWith<IllegalStateException> { group.start { throw InsufficientReviewTargetCountException() } }
    }

    @Test
    fun `리뷰어가 없으면 시작할 수 없다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertFailsWith<IllegalStateException> { group.start { throw InsufficientReviewerCountException() } }
    }

    @Test
    fun `진행 중 상태에서 마감하면 CLOSED로 변경된다`() {
        val group = ReviewGroupFixture.reviewGroup().also { it.start { } }
        group.close()
        assertEquals(ReviewGroupStatus.CLOSED, group.status)
    }

    @Test
    fun `진행 중이 아닌 상태에서 마감하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertFailsWith<IllegalStateException> { group.close() }
    }

    @Test
    fun `마감 상태에서 재개하면 IN_PROGRESS로 변경된다`() {
        val group = ReviewGroupFixture.reviewGroup().also { it.start { }; it.close() }
        group.reopen()
        assertEquals(ReviewGroupStatus.IN_PROGRESS, group.status)
    }

    @Test
    fun `마감 상태가 아닌 상태에서 재개하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        assertFailsWith<IllegalStateException> { group.reopen() }
    }

    @Test
    fun `준비 중이 아닌 상태에서 수정하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup().also { it.start { } }
        assertFailsWith<IllegalStateException> { group.update("새 이름", null, null, null) }
    }

    @Test
    fun `이름과 설명을 수정한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        group.update("새 이름", "새 설명", null, null)
        assertEquals("새 이름", group.name)
        assertEquals("새 설명", group.description)
    }

    @Test
    fun `기간을 수정한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val period = DateRange(LocalDate.of(2025, 1, 1), LocalDate.of(2025, 12, 31))
        group.update(group.name, null, period, null)
        assertEquals(period, group.period)
    }

    @Test
    fun `마감하면 ClosedReviewGroupEvent가 등록된다`() {
        val group = ReviewGroupFixture.reviewGroup(id = 1L).also { it.start { } }
        group.close()
        assertTrue(group.events().any { it is ReviewGroupClosedEvent && it.reviewGroup === group })
    }

    @Test
    fun `재개하면 ReopenedReviewGroupEvent가 등록된다`() {
        val group = ReviewGroupFixture.reviewGroup(id = 1L).also { it.start { }; it.close() }
        group.reopen()
        assertTrue(group.events().any { it is ReviewGroupReopenedEvent && it.reviewGroup === group })
    }
}
