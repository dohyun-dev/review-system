package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.TextareaReviewAnswerItem
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextareaReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.ReviewerFormItemView
import com.dohyundev.review.review.query.model.TextareaReviewItemView
import com.dohyundev.review.review.query.model.TextareaReviewerFormItemView

internal class TextareaReviewerFormItemViewFactory : ReviewerFormItemViewFactory() {
    override fun supports(item: ReviewItem) = item is TextareaReviewItem

    override fun create(item: ReviewItem, answer: ReviewAnswerItem?): ReviewerFormItemView {
        item as TextareaReviewItem
        return TextareaReviewerFormItemView(
            item = ReviewItemView.from(item) as TextareaReviewItemView,
            answer = (answer as? TextareaReviewAnswerItem)?.answer,
        )
    }
}
