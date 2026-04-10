package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.answer.ScoreChoiceReviewAnswerItem

data class ScoreChoiceReviewerAnswerItemCommand(
    override val reviewItemId: Long,
    val selectedOptionId: Long,
) : ReviewerAnswerItemCommand() {
    override fun toDomain() = ScoreChoiceReviewAnswerItem(reviewItemId = reviewItemId, selectedOptionId = selectedOptionId)
}
