package com.dohyundev.review.review.command.adapter.`in`.web.api.request

import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand
import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ScoreChoiceReviewItemRequest::class, name = "SCORE_CHOICE"),
    JsonSubTypes.Type(value = TextReviewItemRequest::class, name = "TEXT"),
    JsonSubTypes.Type(value = TextareaReviewItemRequest::class, name = "TEXTAREA"),
)
sealed class ReviewItemRequest {
    abstract val question: String
    abstract val description: String?
    abstract val isRequired: Boolean
    abstract fun toCommand(): ReviewItemCommand
}
