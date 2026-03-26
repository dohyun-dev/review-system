package com.dohyundev.review.review.template.application.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class CreateReviewTemplateCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val name: String,

    @field:Size(max = 100)
    val description: String? = null,

    @field:NotEmpty
    @field:Valid
    val sections: List<ReviewTemplateSectionCommand> = emptyList(),
)
