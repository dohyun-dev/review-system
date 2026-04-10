package com.dohyundev.review.review.command.application.port.`in`.command

data class CreateReviewTargetCommand(
    val reviewGroupId: Long,
    val employeeId: Long,
)