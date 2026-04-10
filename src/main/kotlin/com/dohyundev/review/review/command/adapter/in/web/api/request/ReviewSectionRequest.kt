package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class ReviewSectionRequest(
    @field:NotBlank(message = "섹션 제목은 비어 있을 수 없습니다.")
    val title: String = "",
    val description: String? = null,
    @field:Valid
    @field:NotEmpty(message = "섹션에 항목이 하나 이상 있어야 합니다.")
    val items: List<ReviewItemRequest> = emptyList(),
) {
    fun toCommand() = ReviewSectionCommand(
        title = title,
        description = description,
        items = items.map { it.toCommand() },
    )
}
