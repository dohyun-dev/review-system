package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import jakarta.validation.constraints.NotBlank

data class ScoreOptionRequest(
    @field:NotBlank(message = "선택지 레이블은 비어 있을 수 없습니다.")
    val label: String,
    val description: String? = null,
    val score: Double,
)
