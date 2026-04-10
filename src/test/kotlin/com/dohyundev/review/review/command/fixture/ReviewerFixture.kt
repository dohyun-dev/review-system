package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import com.dohyundev.review.review.command.domain.target.ReviewTarget

object ReviewerFixture {

    fun inProgressReviewer(
        id: Long? = null,
        review: Review = ReviewFixture.review(),
        employeeId: Long = 1L,
    ): Reviewer {
        val reviewGroup = ReviewGroupFixture.reviewGroup()
        val reviewTarget = ReviewTargetFixture.target(reviewGroup = reviewGroup)
        val reviewer = reviewer(id = id, reviewGroup = reviewGroup, review = review, reviewTarget = reviewTarget, employeeId = employeeId)
        reviewGroup.start { }
        return reviewer
    }

    fun reviewer(
        id: Long? = null,
        reviewGroup: ReviewGroup = ReviewGroupFixture.reviewGroup(),
        review: Review = ReviewFixture.review(),
        reviewTarget: ReviewTarget = ReviewTargetFixture.target(reviewGroup = reviewGroup),
        employeeId: Long = 1L,
    ) = Reviewer(
        id = id,
        reviewGroup = reviewGroup,
        review = review,
        reviewTarget = reviewTarget,
        employeeId = employeeId,
    )
}
