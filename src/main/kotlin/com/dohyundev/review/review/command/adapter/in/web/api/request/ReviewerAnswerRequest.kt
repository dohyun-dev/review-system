package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.application.port.`in`.command.ReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.TextReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.TextareaReviewerAnswerItemCommand
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.Valid
import jakarta.validation.constraints.NotNull

data class ReviewerAnswerRequest(
    @field:Valid
    @Schema(description = "항목별 답변 목록")
    val answers: List<ReviewAnswerItemRequest> = emptyList(),
)

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = TextReviewAnswerItemRequest::class, name = "TEXT"),
    JsonSubTypes.Type(value = TextareaReviewAnswerItemRequest::class, name = "TEXTAREA"),
    JsonSubTypes.Type(value = ScoreChoiceReviewAnswerItemRequest::class, name = "SCORE_CHOICE"),
)
sealed class ReviewAnswerItemRequest {
    abstract val reviewItemId: Long
    abstract fun toCommand(): ReviewerAnswerItemCommand
}

data class TextReviewAnswerItemRequest(
    @field:NotNull(message = "reviewItemId는 필수입니다.")
    override val reviewItemId: Long,
    val answer: String? = null,
) : ReviewAnswerItemRequest() {
    override fun toCommand() = TextReviewerAnswerItemCommand(reviewItemId = reviewItemId, answer = answer)
}

data class TextareaReviewAnswerItemRequest(
    @field:NotNull(message = "reviewItemId는 필수입니다.")
    override val reviewItemId: Long,
    val answer: String? = null,
) : ReviewAnswerItemRequest() {
    override fun toCommand() = TextareaReviewerAnswerItemCommand(reviewItemId = reviewItemId, answer = answer)
}

data class ScoreChoiceReviewAnswerItemRequest(
    @field:NotNull(message = "reviewItemId는 필수입니다.")
    override val reviewItemId: Long,
    @field:NotNull(message = "selectedOptionId는 필수입니다.")
    val selectedOptionId: Long,
) : ReviewAnswerItemRequest() {
    override fun toCommand() = ScoreChoiceReviewerAnswerItemCommand(
        reviewItemId = reviewItemId,
        selectedOptionId = selectedOptionId,
    )
}
