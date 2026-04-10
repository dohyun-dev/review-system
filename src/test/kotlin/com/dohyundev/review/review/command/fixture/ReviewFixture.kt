package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.domain.review.ReviewType

object ReviewFixture {

    fun review(
        id: Long? = null,
        reviewGroup: ReviewGroup = ReviewGroupFixture.reviewGroup(),
        type: ReviewType = ReviewType.SELF,
        sections: MutableList<ReviewSection> = mutableListOf(ReviewSectionFixture.section()),
    ) = Review(
        id = id,
        reviewGroup = reviewGroup,
        type = type,
        sections = sections,
    )
}
