package com.dohyundev.review.review.template.application.port.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class ScoreSelectOptionCommand(
    @field:NotBlank
    @field:Size(max = 50)
    val content: String,

    @field:Size(max = 100)
    val description: String? = null,

    val score: Double,
)
