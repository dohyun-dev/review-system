package com.dohyundev.review.review.command.application.port.`in`.command

data class SubmitReviewerAnswerCommand(
    val reviewGroupId: Long,
    val reviewerId: Long,
    val employeeId: Long,
    val answerItems: List<ReviewerAnswerItemCommand>,
)
