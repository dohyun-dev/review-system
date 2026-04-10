package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.answer.TextReviewAnswerItem

data class TextReviewerAnswerItemCommand(
    override val reviewItemId: Long,
    val answer: String?,
) : ReviewerAnswerItemCommand() {
    override fun toDomain() = TextReviewAnswerItem(reviewItemId = reviewItemId, answer = answer)
}
