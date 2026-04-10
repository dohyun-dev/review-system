package com.dohyundev.review.review.command.application.port.`in`.command

data class CancelReviewerAnswerCommand(
    val reviewGroupId: Long,
    val reviewerId: Long,
    val employeeId: Long,
)
