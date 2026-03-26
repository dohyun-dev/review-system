package com.dohyundev.review.review.template.application.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotEmpty

data class UpdateReviewTemplateFormCommand(
    @field:NotEmpty
    @field:Valid
    val sections: List<ReviewTemplateSectionCommand>,
)
