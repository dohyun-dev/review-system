package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewCommand

interface AppendReviewUseCase {
    fun appendReview(command: AppendReviewCommand): Long
}
