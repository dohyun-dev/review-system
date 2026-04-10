package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.common.DateRange

data class UpdateReviewGroupCommand(
    val reviewGroupId: Long,
    val name: String,
    val description: String? = null,
    val period: DateRange? = null,
    val targetPeriod: DateRange? = null,
)
