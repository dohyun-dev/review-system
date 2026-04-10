package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextareaReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.TextareaReviewItemView

internal class TextareaReviewItemViewFactory : ReviewItemViewFactory() {
    override fun supports(item: ReviewItem) = item is TextareaReviewItem

    override fun create(item: ReviewItem): ReviewItemView {
        item as TextareaReviewItem
        return TextareaReviewItemView(item)
    }
}
