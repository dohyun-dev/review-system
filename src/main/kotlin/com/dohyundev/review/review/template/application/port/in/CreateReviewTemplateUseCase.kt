package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.port.dto.CreateReviewTemplateCommand
import com.dohyundev.review.review.template.domain.ReviewTemplate

interface CreateReviewTemplateUseCase {
    fun create(command: CreateReviewTemplateCommand): ReviewTemplate
}
