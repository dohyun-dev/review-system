package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.ScoreChoiceReviewItemCommand
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import com.dohyundev.review.review.command.domain.review.service.ReviewItemFactory
import org.springframework.stereotype.Component

@Component
class ScoreChoiceReviewItemFactory : ReviewItemFactory {
    override fun supports(command: ReviewItemCommand): Boolean = command is ScoreChoiceReviewItemCommand
    override fun create(command: ReviewItemCommand): ReviewItem {
        command as ScoreChoiceReviewItemCommand
        val item = ScoreChoiceReviewItem(
            question = command.question,
            description = command.description,
            isRequired = command.isRequired,
            options = command.options.mapIndexed { index, opt ->
                ScoreChoiceReviewItemOption(
                    label = opt.label,
                    description = opt.description,
                    sortOrder = index + 1,
                    score = opt.score,
                )
            }.toMutableList()
        )
        return item
    }
}
