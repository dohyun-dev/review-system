package com.dohyundev.review.review.group.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXT")
class TextReviewItem(
    question: String,
    description: String? = null,
    var answer: String? = null,
) : ReviewItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): TextReviewItem {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return TextReviewItem(question = question, description = description)
        }
    }
}
