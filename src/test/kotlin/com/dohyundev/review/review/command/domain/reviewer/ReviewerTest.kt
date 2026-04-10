package com.dohyundev.review.review.command.domain.reviewer

import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReviewerTest {

    @Test
    fun `리뷰어를 생성하면 초기 상태는 WAIT이다`() {
        val reviewer = ReviewerFixture.reviewer()
        assertEquals(ReviewerStatus.WAIT, reviewer.status)
    }

    @Test
    fun `셀프리뷰에서 리뷰어와 대상자가 같은 사원이면 생성된다`() {
        val reviewGroup = ReviewGroupFixture.reviewGroup()
        val reviewTarget = ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 1L)
        val review = ReviewFixture.review(reviewGroup = reviewGroup)
        val reviewer = ReviewerFixture.reviewer(reviewGroup = reviewGroup, review = review, reviewTarget = reviewTarget, employeeId = 1L)
        assertEquals(ReviewerStatus.WAIT, reviewer.status)
    }

    @Test
    fun `셀프리뷰에서 리뷰어와 대상자가 다른 사원이면 예외가 발생한다`() {
        val reviewGroup = ReviewGroupFixture.reviewGroup()
        val reviewTarget = ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 1L)
        val review = ReviewFixture.review(reviewGroup = reviewGroup)
        assertFailsWith<IllegalStateException> {
            ReviewerFixture.reviewer(reviewGroup = reviewGroup, review = review, reviewTarget = reviewTarget, employeeId = 2L)
        }
    }

    @Test
    fun `WAIT 상태에서 enableReview를 호출하면 NOT_SUBMITTED로 변경된다`() {
        val reviewer = ReviewerFixture.reviewer()
        reviewer.enableReview()
        assertEquals(ReviewerStatus.NOT_SUBMITTED, reviewer.status)
    }

    @Test
    fun `WAIT이 아닌 상태에서 enableReview를 호출하면 예외가 발생한다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }
        assertFailsWith<IllegalStateException> { reviewer.enableReview() }
    }

    @Test
    fun `NOT_SUBMITTED 상태에서 markDraft를 호출하면 DRAFT로 변경된다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }
        reviewer.markDraft()
        assertEquals(ReviewerStatus.DRAFT, reviewer.status)
    }

    @Test
    fun `DRAFT 상태에서 markDraft를 호출하면 DRAFT로 유지된다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }.also { it.markDraft() }
        reviewer.markDraft()
        assertEquals(ReviewerStatus.DRAFT, reviewer.status)
    }

    @Test
    fun `SUBMITTED 상태에서 markDraft를 호출하면 예외가 발생한다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }.also { it.markSubmitted() }
        assertFailsWith<IllegalStateException> { reviewer.markDraft() }
    }

    @Test
    fun `NOT_SUBMITTED 상태에서 markSubmitted를 호출하면 SUBMITTED로 변경된다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }
        reviewer.markSubmitted()
        assertEquals(ReviewerStatus.SUBMITTED, reviewer.status)
    }

    @Test
    fun `SUBMITTED 상태에서 markSubmitted를 호출하면 예외가 발생한다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }.also { it.markSubmitted() }
        assertFailsWith<IllegalStateException> { reviewer.markSubmitted() }
    }

    @Test
    fun `SUBMITTED 상태에서 cancelSubmission을 호출하면 NOT_SUBMITTED로 변경된다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }.also { it.markSubmitted() }
        reviewer.cancelSubmission()
        assertEquals(ReviewerStatus.NOT_SUBMITTED, reviewer.status)
    }

    @Test
    fun `SUBMITTED가 아닌 상태에서 cancelSubmission을 호출하면 예외가 발생한다`() {
        val reviewer = ReviewerFixture.reviewer().also { it.enableReview() }
        assertFailsWith<IllegalStateException> { reviewer.cancelSubmission() }
    }
}
