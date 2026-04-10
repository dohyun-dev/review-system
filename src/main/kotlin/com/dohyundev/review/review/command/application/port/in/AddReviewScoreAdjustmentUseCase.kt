package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.AddReviewScoreAdjustmentCommand

interface AddReviewScoreAdjustmentUseCase {
    fun addReviewScoreAdjustment(command: AddReviewScoreAdjustmentCommand): Long
}
