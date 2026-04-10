package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.target.ReviewTarget

object ReviewTargetFixture {

    fun target(
        id: Long? = null,
        reviewGroup: ReviewGroup,
        employeeId: Long = 1L,
    ) = ReviewTarget(
        id = id,
        reviewGroup = reviewGroup,
        employeeId = employeeId,
    )
}
