package com.dohyundev.review.review.group.domain

import com.dohyundev.review.review.group.fixture.ReviewGroupFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewGroupTest {

    @Test
    fun `create - 이름으로 리뷰 그룹을 생성한다`() {
        val group = ReviewGroup.create(name = "2026 상반기 리뷰")

        assertEquals("2026 상반기 리뷰", group.name)
        assertEquals(ReviewGroupStatus.PREPARING, group.status)
        assertTrue(group.reviews.isEmpty())
        assertTrue(group.targets.isEmpty())
        assertTrue(group.reviewers.isEmpty())
    }

    @Test
    fun `create - 이름이 공백이면 예외가 발생한다`() {
        assertThrows<IllegalArgumentException> {
            ReviewGroup.create(name = " ")
        }
    }

    @Test
    fun `start - PREPARING 상태에서 IN_PROGRESS로 전환된다`() {
        val group = ReviewGroupFixture.createReviewGroup()

        group.start()

        assertEquals(ReviewGroupStatus.IN_PROGRESS, group.status)
    }

    @Test
    fun `start - PREPARING이 아닌 상태에서 시작하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        group.start()

        assertThrows<IllegalArgumentException> { group.start() }
    }

    @Test
    fun `close - IN_PROGRESS 상태에서 CLOSED로 전환된다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        group.start()

        group.close()

        assertEquals(ReviewGroupStatus.CLOSED, group.status)
    }

    @Test
    fun `close - IN_PROGRESS가 아닌 상태에서 마감하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.createReviewGroup()

        assertThrows<IllegalArgumentException> { group.close() }
    }

    @Test
    fun `end - CLOSED 상태에서 ENDED로 전환된다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        group.start()
        group.close()

        group.end()

        assertEquals(ReviewGroupStatus.ENDED, group.status)
    }

    @Test
    fun `end - CLOSED가 아닌 상태에서 종료하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        group.start()

        assertThrows<IllegalArgumentException> { group.end() }
    }

    @Test
    fun `addReview - 리뷰를 추가한다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        val review = ReviewGroupFixture.createReview()

        group.addReview(review)

        assertEquals(1, group.reviews.size)
        assertEquals(group, review.reviewGroup)
    }

    @Test
    fun `addTarget - 리뷰 대상자를 추가한다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        val target = ReviewGroupFixture.createReviewTarget()

        group.addTarget(target)

        assertEquals(1, group.targets.size)
        assertEquals(group, target.reviewGroup)
    }

    @Test
    fun `addReviewer - 리뷰어를 추가한다`() {
        val group = ReviewGroupFixture.createReviewGroup()
        val reviewer = ReviewGroupFixture.createReviewer()

        group.addReviewer(reviewer)

        assertEquals(1, group.reviewers.size)
        assertEquals(group, reviewer.reviewGroup)
    }
}
