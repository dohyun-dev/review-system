package com.dohyundev.review.review.template.application.dto

import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class UpdateReviewTemplateCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val name: String,

    @field:Size(max = 100)
    val description: String? = null,

    @field:Min(1)
    val sortOrder: Int,
)
