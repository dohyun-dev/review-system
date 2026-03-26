package com.dohyundev.review.review.group.domain

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceReviewerAnswer(
    reviewItem: ScoreChoiceReviewItem,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "selected_option_id", nullable = false)
    var selectedOption: ReviewItemOption,
) : ReviewerAnswer(reviewItem = reviewItem) {

    override fun validate() {
        val item = reviewItem as ScoreChoiceReviewItem
        require(item.options.contains(selectedOption)) { "선택지가 해당 항목에 속하지 않습니다" }
    }
}
