package com.dohyundev.review.review.group.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXTAREA")
class TextareaReviewerAnswer(
    reviewItem: TextareaReviewItem,

    @Column(nullable = false)
    var answer: String,
) : ReviewerAnswer(reviewItem = reviewItem) {

    override fun validate() {
        require(answer.isNotBlank()) { "서술형 답변은 공백일 수 없습니다" }
    }
}
