package com.dohyundev.review.review.group.application.port.`in`

import com.dohyundev.review.review.group.application.dto.CreateReviewGroupCommand
import com.dohyundev.review.review.group.domain.ReviewGroup

interface CreateReviewGroupUseCase {
    fun create(command: CreateReviewGroupCommand): ReviewGroup
}
