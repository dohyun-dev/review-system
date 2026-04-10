package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.query.factory.ReviewItemViewFactory

sealed class ReviewItemView {
    abstract val id: Long
    abstract val question: String
    abstract val description: String?
    abstract val isRequired: Boolean
    abstract val sortOrder: Int

    companion object {
        fun from(item: ReviewItem): ReviewItemView = ReviewItemViewFactory.create(item)
    }
}
