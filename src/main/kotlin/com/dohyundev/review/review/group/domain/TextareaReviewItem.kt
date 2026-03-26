package com.dohyundev.review.review.group.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXTAREA")
class TextareaReviewItem(
    question: String,
    description: String? = null,
    var answer: String? = null,
) : ReviewItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): TextareaReviewItem {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return TextareaReviewItem(question = question, description = description)
        }
    }
}
