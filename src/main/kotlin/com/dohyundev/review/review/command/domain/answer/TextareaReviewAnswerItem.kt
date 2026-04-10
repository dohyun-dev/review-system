package com.dohyundev.review.review.command.domain.answer

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextareaReviewItem
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXTAREA")
class TextareaReviewAnswerItem(
    id: Long? = null,
    reviewItemId: Long,
    val answer: String? = null,
) : ReviewAnswerItem(id = id, reviewItemId = reviewItemId) {
    override fun hasValue(): Boolean = answer != null

    override fun validate(reviewItem: ReviewItem) {
        val value = answer ?: return
        if (reviewItem !is TextareaReviewItem) return
        val maxLength = reviewItem.maxLength ?: return
        check(value.length <= maxLength) {
            "'${reviewItem.question}' 항목은 최대 ${maxLength}자까지 입력 가능합니다."
        }
    }
}
