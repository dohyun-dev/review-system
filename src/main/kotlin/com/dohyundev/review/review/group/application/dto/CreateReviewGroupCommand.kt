package com.dohyundev.review.review.group.application.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CreateReviewGroupCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val name: String,

    @field:Size(max = 100)
    val description: String? = null,
)
