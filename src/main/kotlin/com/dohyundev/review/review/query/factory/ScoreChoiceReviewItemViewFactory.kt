package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.ScoreChoiceReviewItemView

internal class ScoreChoiceReviewItemViewFactory : ReviewItemViewFactory() {
    override fun supports(item: ReviewItem) = item is ScoreChoiceReviewItem

    override fun create(item: ReviewItem): ReviewItemView {
        item as ScoreChoiceReviewItem
        return ScoreChoiceReviewItemView(item)
    }
}
