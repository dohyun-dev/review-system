package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.application.port.`in`.command.ReviewerAnswerItemCommand
import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import org.springframework.stereotype.Component

@Component
class ReviewerAnswerItemsFactory {
    fun create(commands: List<ReviewerAnswerItemCommand>): List<ReviewAnswerItem> =
        commands.map { it.toDomain() }
}
