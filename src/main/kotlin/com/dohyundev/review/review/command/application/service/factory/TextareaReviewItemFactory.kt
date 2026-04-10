package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.TextareaReviewItemCommand
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.TextareaReviewItem
import com.dohyundev.review.review.command.domain.review.service.ReviewItemFactory
import org.springframework.stereotype.Component

@Component
class TextareaReviewItemFactory : ReviewItemFactory {
    override fun supports(command: ReviewItemCommand): Boolean = command is TextareaReviewItemCommand
    override fun create(command: ReviewItemCommand): ReviewItem {
        command as TextareaReviewItemCommand
        return TextareaReviewItem(
            question = command.question,
            description = command.description,
            isRequired = command.isRequired,
            placeholder = command.placeholder,
            maxLength = command.maxLength,
        )
    }
}
