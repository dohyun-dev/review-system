package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewTargetCommand

interface AppendReviewTargetUseCase {
    fun appendReviewTarget(command: CreateReviewTargetCommand): Long
}
