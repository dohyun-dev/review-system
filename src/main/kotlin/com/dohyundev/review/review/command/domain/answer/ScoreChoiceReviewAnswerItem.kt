package com.dohyundev.review.review.command.domain.answer

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import java.math.BigDecimal
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceReviewAnswerItem(
    id: Long? = null,
    reviewItemId: Long,
    val selectedOptionId: Long,
) : ReviewAnswerItem(id = id, reviewItemId = reviewItemId) {
    override fun calculateScore(reviewItem: ReviewItem): BigDecimal {
        if (reviewItem !is ScoreChoiceReviewItem) return BigDecimal.ZERO
        return (reviewItem.findByOptionId(selectedOptionId) as? ScoreChoiceReviewItemOption)?.score?.toBigDecimal() ?: BigDecimal.ZERO
    }

    override fun validate(reviewItem: ReviewItem) {
        if (reviewItem !is ScoreChoiceReviewItem) return
        check(reviewItem.findByOptionId(selectedOptionId) != null) {
            "'${reviewItem.question}' 항목에 유효하지 않은 선택지입니다."
        }
    }
}
