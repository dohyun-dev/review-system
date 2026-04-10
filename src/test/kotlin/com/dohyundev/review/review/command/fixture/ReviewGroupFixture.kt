package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.domain.group.ReviewGroup

object ReviewGroupFixture {

    fun inProgressReviewGroup(
        id: Long? = null,
        name: String = "리뷰 그룹",
    ): ReviewGroup {
        val group = reviewGroup(id = id, name = name)
        group.start { }
        return group
    }

    fun reviewGroup(
        id: Long? = null,
        name: String = "리뷰 그룹",
        description: String? = null,
        period: DateRange? = null,
        targetPeriod: DateRange? = null,
    ) = ReviewGroup(
        id = id,
        name = name,
        description = description,
        period = period,
        targetPeriod = targetPeriod,
    )
}
