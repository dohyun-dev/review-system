package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewerCommand

interface AppendReviewerUseCase {
    fun appendReviewer(command: AppendReviewerCommand): Long
}