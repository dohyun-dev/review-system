package com.dohyundev.review.review.command.domain.target

import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ReviewTargetTest {

    @Test
    fun `리뷰 대상자를 생성한다`() {
        val target = ReviewTarget(reviewGroup = ReviewGroupFixture.reviewGroup(), employeeId = 1L)
        assertEquals(1L, target.employeeId)
    }

    @Test
    fun `진행 중인 리뷰 그룹에 대상자를 생성하면 예외가 발생한다`() {
        val reviewGroup = ReviewGroupFixture.reviewGroup()
        reviewGroup.start { }
        assertFailsWith<IllegalStateException> {
            ReviewTarget(reviewGroup = reviewGroup, employeeId = 1L)
        }
    }
}
