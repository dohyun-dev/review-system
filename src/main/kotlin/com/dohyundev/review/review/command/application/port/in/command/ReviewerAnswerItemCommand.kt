package com.dohyundev.review.review.command.application.port.`in`.command

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem

sealed class ReviewerAnswerItemCommand {
    abstract val reviewItemId: Long
    abstract fun toDomain(): ReviewAnswerItem
}
