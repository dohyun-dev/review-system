package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.answer.TextareaReviewAnswerItem

data class TextareaReviewerAnswerItemCommand(
    override val reviewItemId: Long,
    val answer: String?,
) : ReviewerAnswerItemCommand() {
    override fun toDomain() = TextareaReviewAnswerItem(reviewItemId = reviewItemId, answer = answer)
}
