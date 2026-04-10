package com.dohyundev.review.review.command.application.port.`in`.command

import java.math.BigDecimal

data class AddReviewScoreAdjustmentCommand(
    val reviewGroupId: Long,
    val reviewTargetId: Long,
    val adjusterId: Long,
    val amount: BigDecimal,
    val reason: String? = null,
)
