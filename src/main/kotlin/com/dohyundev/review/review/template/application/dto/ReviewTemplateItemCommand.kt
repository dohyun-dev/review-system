package com.dohyundev.review.review.template.application.dto

import com.fasterxml.jackson.annotation.JsonSubTypes
import com.fasterxml.jackson.annotation.JsonTypeInfo
import jakarta.validation.Valid
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes(
    JsonSubTypes.Type(value = ReviewTemplateItemCommand.ScoreChoice::class, name = "SCORE_CHOICE"),
    JsonSubTypes.Type(value = ReviewTemplateItemCommand.Text::class, name = "TEXT"),
    JsonSubTypes.Type(value = ReviewTemplateItemCommand.Textarea::class, name = "TEXTAREA"),
)
sealed class ReviewTemplateItemCommand {
    abstract val question: String
    abstract val description: String?

    data class ScoreChoice(
        @field:NotBlank
        @field:Size(max = 50)
        override val question: String,

        @field:Size(max = 100)
        override val description: String? = null,

        @field:Valid
        val options: List<ScoreSelectOptionCommand>,
    ) : ReviewTemplateItemCommand()

    data class Text(
        @field:NotBlank
        @field:Size(max = 50)
        override val question: String,

        @field:Size(max = 100)
        override val description: String? = null,
    ) : ReviewTemplateItemCommand()

    data class Textarea(
        @field:NotBlank
        @field:Size(max = 50)
        override val question: String,

        @field:Size(max = 100)
        override val description: String? = null,
    ) : ReviewTemplateItemCommand()
}
