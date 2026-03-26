package com.dohyundev.review.review.template.application.port.`in`

import com.dohyundev.review.review.template.application.dto.UpdateReviewTemplateFormCommand
import com.dohyundev.review.review.template.domain.ReviewTemplate

interface UpdateFormReviewTemplateUseCase {
    fun updateForm(id: Long, command: UpdateReviewTemplateFormCommand): ReviewTemplate
}
