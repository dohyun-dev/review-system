package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.domain.review.command.ScoreChoiceReviewItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceOptionCommand
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty

data class ScoreChoiceReviewItemRequest(
    @field:NotBlank(message = "질문은 비어 있을 수 없습니다.")
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    @field:Valid
    @field:NotEmpty(message = "점수 선택 항목에는 선택지가 하나 이상 있어야 합니다.")
    val options: List<ScoreOptionRequest>,
) : ReviewItemRequest() {
    override fun toCommand() = ScoreChoiceReviewItemCommand(
        question = question,
        description = description,
        isRequired = isRequired,
        options = options.map { ScoreChoiceOptionCommand(label = it.label, description = it.description, score = it.score) },
    )
}
