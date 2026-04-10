package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewCommand

interface UpdateReviewUseCase {
    fun updateReview(command: UpdateReviewCommand)
}
