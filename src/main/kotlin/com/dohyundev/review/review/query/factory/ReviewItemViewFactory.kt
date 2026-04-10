package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView

abstract class ReviewItemViewFactory {
    abstract fun supports(item: ReviewItem): Boolean
    abstract fun create(item: ReviewItem): ReviewItemView

    companion object {
        private val factories: List<ReviewItemViewFactory> = listOf(
            ScoreChoiceReviewItemViewFactory(),
            TextReviewItemViewFactory(),
            TextareaReviewItemViewFactory(),
        )

        fun create(item: ReviewItem): ReviewItemView =
            factories.first { it.supports(item) }.create(item)
    }
}
