package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.TextReviewItemCommand
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.command.domain.review.service.ReviewItemFactory
import org.springframework.stereotype.Component

@Component
class TextReviewItemFactory : ReviewItemFactory {
    override fun supports(command: ReviewItemCommand): Boolean = command is TextReviewItemCommand
    override fun create(command: ReviewItemCommand): ReviewItem {
        command as TextReviewItemCommand
        return TextReviewItem(
            question = command.question,
            description = command.description,
            isRequired = command.isRequired,
            placeholder = command.placeholder,
            maxLength = command.maxLength,
        )
    }
}
