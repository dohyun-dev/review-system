package com.dohyundev.review.review.template.application.port.dto

import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.Size

data class ReviewTemplateSectionCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val name: String,

    @field:NotEmpty
    @field:Valid
    val items: List<ReviewTemplateItemCommand>,
)
