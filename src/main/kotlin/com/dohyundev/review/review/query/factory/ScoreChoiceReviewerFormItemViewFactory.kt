package com.dohyundev.review.review.query.factory

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.ScoreChoiceReviewAnswerItem
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.query.model.ReviewItemView
import com.dohyundev.review.review.query.model.ReviewerFormItemView
import com.dohyundev.review.review.query.model.ScoreChoiceReviewItemView
import com.dohyundev.review.review.query.model.ScoreChoiceReviewerFormItemView

internal class ScoreChoiceReviewerFormItemViewFactory : ReviewerFormItemViewFactory() {
    override fun supports(item: ReviewItem) = item is ScoreChoiceReviewItem

    override fun create(item: ReviewItem, answer: ReviewAnswerItem?): ReviewerFormItemView {
        item as ScoreChoiceReviewItem
        return ScoreChoiceReviewerFormItemView(
            item = ReviewItemView.from(item) as ScoreChoiceReviewItemView,
            selectedOptionId = (answer as? ScoreChoiceReviewAnswerItem)?.selectedOptionId,
        )
    }
}
