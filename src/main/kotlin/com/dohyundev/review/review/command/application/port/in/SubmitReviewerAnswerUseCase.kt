package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.SubmitReviewerAnswerCommand

interface SubmitReviewerAnswerUseCase {
    fun submit(command: SubmitReviewerAnswerCommand)
}
