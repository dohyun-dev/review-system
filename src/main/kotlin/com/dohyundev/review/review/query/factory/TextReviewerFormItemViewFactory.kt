package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.TextReviewAnswerItem
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.ReviewerFormItemView
import com.dohyundev.review.review.query.model.TextReviewItemView
import com.dohyundev.review.review.query.model.TextReviewerFormItemView

internal class TextReviewerFormItemViewFactory : ReviewerFormItemViewFactory() {
    override fun supports(item: ReviewItem) = item is TextReviewItem

    override fun create(item: ReviewItem, answer: ReviewAnswerItem?): ReviewerFormItemView {
        item as TextReviewItem
        return TextReviewerFormItemView(
            item = ReviewItemView.from(item) as TextReviewItemView,
            answer = (answer as? TextReviewAnswerItem)?.answer,
        )
    }
}
