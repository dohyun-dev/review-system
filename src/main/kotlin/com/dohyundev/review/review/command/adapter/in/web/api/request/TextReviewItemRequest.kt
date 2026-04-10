package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.domain.review.command.TextReviewItemCommand
import jakarta.validation.constraints.NotBlank

data class TextReviewItemRequest(
    @field:NotBlank(message = "질문은 비어 있을 수 없습니다.")
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    val placeholder: String? = null,
    val maxLength: Int? = null,
) : ReviewItemRequest() {
    override fun toCommand() = TextReviewItemCommand(
        question = question,
        description = description,
        isRequired = isRequired,
        placeholder = placeholder,
        maxLength = maxLength,
    )
}
