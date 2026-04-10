package com.dohyundev.review.review.command.application.port.`in`.command

data class AppendReviewerCommand(
    val reviewGroupId: Long,
    val reviewId: Long,
    val reviewTargetId: Long,
    val employeeId: Long,
)