package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewGroupCommand

interface CreateReviewGroupUseCase {
    fun create(command: CreateReviewGroupCommand): Long
}
