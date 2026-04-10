package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.DraftReviewerAnswerCommand

interface DraftReviewerAnswerUseCase {
    fun draft(command: DraftReviewerAnswerCommand)
}
