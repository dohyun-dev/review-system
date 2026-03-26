package com.dohyundev.review.review.group.domain

import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXT")
class TextReviewerAnswer(
    reviewItem: TextReviewItem,

    @Column(nullable = false)
    var answer: String,
) : ReviewerAnswer(reviewItem = reviewItem) {

    override fun validate() {
        require(answer.isNotBlank()) { "텍스트 답변은 공백일 수 없습니다" }
    }
}
