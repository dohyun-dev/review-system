package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.port.dto.UpdateReviewTemplateCommand
import com.dohyundev.review.review.template.domain.ReviewTemplate

interface UpdateReviewTemplateUseCase {
    fun update(id: Long, command: UpdateReviewTemplateCommand): ReviewTemplate
}
