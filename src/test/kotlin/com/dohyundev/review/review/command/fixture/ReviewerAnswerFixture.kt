package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.ReviewerAnswer
import com.dohyundev.review.review.command.domain.reviewer.Reviewer

object ReviewerAnswerFixture {

    fun reviewerAnswer(
        id: Long? = null,
        reviewer: Reviewer = ReviewerFixture.inProgressReviewer(),
        items: MutableList<ReviewAnswerItem> = mutableListOf(),
    ) = ReviewerAnswer(
        id = id,
        reviewer = reviewer,
        items = items,
    )
}
