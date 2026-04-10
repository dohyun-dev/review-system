package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.query.model.ReviewerFormItemView

abstract class ReviewerFormItemViewFactory {
    abstract fun supports(item: ReviewItem): Boolean
    abstract fun create(item: ReviewItem, answer: ReviewAnswerItem?): ReviewerFormItemView

    companion object {
        private val factories: List<ReviewerFormItemViewFactory> = listOf(
            ScoreChoiceReviewerFormItemViewFactory(),
            TextReviewerFormItemViewFactory(),
            TextareaReviewerFormItemViewFactory(),
        )

        fun create(item: ReviewItem, answer: ReviewAnswerItem?): ReviewerFormItemView =
            factories.first { it.supports(item) }.create(item, answer)
    }
}
