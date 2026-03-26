package com.dohyundev.review.review.template.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceItemOptionTemplate(
    content: String,
    description: String? = null,
    var score: Double? = null,
) : ReviewTemplateItemOption(content = content, description = description) {

    companion object {
        fun create(content: String, score: Double, description: String? = null): ScoreChoiceItemOptionTemplate {
            require(content.isNotBlank()) { "선택지 내용은 공백일 수 없습니다" }
            return ScoreChoiceItemOptionTemplate(content = content, description = description, score = score)
        }
    }
}
