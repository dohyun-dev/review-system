package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.CancelReviewerAnswerCommand

interface CancelReviewerAnswerUseCase {
    fun cancel(command: CancelReviewerAnswerCommand)
}
