package com.dohyundev.review.review.template.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXT")
class TextItemTemplate(
    question: String,
    description: String? = null,
) : ReviewTemplateItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): TextItemTemplate {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return TextItemTemplate(question = question, description = description)
        }
    }
}
