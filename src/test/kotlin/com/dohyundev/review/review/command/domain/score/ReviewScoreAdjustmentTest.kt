package com.dohyundev.review.review.command.domain.score

import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotClosedException
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertFailsWith

class ReviewScoreAdjustmentTest {

    private fun closedGroup() = ReviewGroupFixture.reviewGroup().also {
        val target = ReviewTargetFixture.target(reviewGroup = it)
        it.start { }
        it.close()
    }

    @Test
    fun `CLOSED 상태의 리뷰 그룹에 점수 가감을 생성할 수 있다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)
        group.start { }
        group.close()

        ReviewScoreAdjustment(
            reviewGroup = group,
            reviewTarget = target,
            adjuster = Adjuster(1L),
            amount = BigDecimal("5.0"),
        )
    }

    @Test
    fun `IN_PROGRESS 상태의 리뷰 그룹에 점수 가감을 생성하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)
        group.start { }

        assertFailsWith<ReviewGroupNotClosedException> {
            ReviewScoreAdjustment(
                reviewGroup = group,
                reviewTarget = target,
                adjuster = Adjuster(1L),
                amount = BigDecimal("5.0"),
            )
        }
    }

    @Test
    fun `PREPARING 상태의 리뷰 그룹에 점수 가감을 생성하면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)

        assertFailsWith<ReviewGroupNotClosedException> {
            ReviewScoreAdjustment(
                reviewGroup = group,
                reviewTarget = target,
                adjuster = Adjuster(1L),
                amount = BigDecimal("5.0"),
            )
        }
    }

    @Test
    fun `사유가 150자 이상이면 예외가 발생한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)
        group.start { }
        group.close()

        assertFailsWith<IllegalArgumentException> {
            ReviewScoreAdjustment(
                reviewGroup = group,
                reviewTarget = target,
                adjuster = Adjuster(1L),
                amount = BigDecimal("5.0"),
                reason = "a".repeat(150),
            )
        }
    }

    @Test
    fun `사유가 149자이면 생성에 성공한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)
        group.start { }
        group.close()

        ReviewScoreAdjustment(
            reviewGroup = group,
            reviewTarget = target,
            adjuster = Adjuster(1L),
            amount = BigDecimal("5.0"),
            reason = "a".repeat(149),
        )
    }

    @Test
    fun `사유가 null이면 생성에 성공한다`() {
        val group = ReviewGroupFixture.reviewGroup()
        val target = ReviewTargetFixture.target(reviewGroup = group)
        group.start { }
        group.close()

        ReviewScoreAdjustment(
            reviewGroup = group,
            reviewTarget = target,
            adjuster = Adjuster(1L),
            amount = BigDecimal("5.0"),
            reason = null,
        )
    }
}
