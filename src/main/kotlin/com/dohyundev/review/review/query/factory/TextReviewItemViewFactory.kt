package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.TextReviewItemView

internal class TextReviewItemViewFactory : ReviewItemViewFactory() {
    override fun supports(item: ReviewItem) = item is TextReviewItem

    override fun create(item: ReviewItem): ReviewItemView {
        item as TextReviewItem
        return TextReviewItemView(item)
    }
}
