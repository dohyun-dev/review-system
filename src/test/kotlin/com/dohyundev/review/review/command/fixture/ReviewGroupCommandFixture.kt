package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.common.DateRange
import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewGroupCommand
import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewGroupCommand

object ReviewGroupCommandFixture {

    fun createCommand(
        name: String = "리뷰 그룹",
        description: String? = null,
        period: DateRange? = null,
        targetPeriod: DateRange? = null,
    ) = CreateReviewGroupCommand(
        name = name,
        description = description,
        period = period,
        targetPeriod = targetPeriod,
    )

    fun updateCommand(
        reviewGroupId: Long,
        name: String = "수정된 이름",
        description: String? = null,
        period: DateRange? = null,
        targetPeriod: DateRange? = null,
    ) = UpdateReviewGroupCommand(
        reviewGroupId = reviewGroupId,
        name = name,
        description = description,
        period = period,
        targetPeriod = targetPeriod,
    )
}
