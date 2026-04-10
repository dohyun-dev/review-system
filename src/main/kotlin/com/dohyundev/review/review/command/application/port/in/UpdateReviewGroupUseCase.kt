package com.dohyundev.review.review.command.application.port.`in`

import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewGroupCommand

interface UpdateReviewGroupUseCase {
    fun update(command: UpdateReviewGroupCommand)
}
