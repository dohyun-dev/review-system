package com.dohyundev.review.review.template.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXTAREA")
class TextareaItemTemplate(
    question: String,
    description: String? = null,
) : ReviewTemplateItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): TextareaItemTemplate {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return TextareaItemTemplate(question = question, description = description)
        }
    }
}
