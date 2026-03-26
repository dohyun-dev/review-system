package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.domain.ReviewTemplateItem
import com.dohyundev.review.review.template.domain.ScoreChoiceItemOptionTemplate
import com.dohyundev.review.review.template.domain.ScoreChoiceItemTemplate
import org.springframework.stereotype.Component

@Component
class ScoreChoiceItemFactory : ReviewTemplateItemFactory {

    override fun supports(command: ReviewTemplateItemCommand) =
        command is ReviewTemplateItemCommand.ScoreChoice

    override fun create(command: ReviewTemplateItemCommand): ReviewTemplateItem {
        command as ReviewTemplateItemCommand.ScoreChoice
        val item = ScoreChoiceItemTemplate.create(
            question = command.question,
            description = command.description,
        )
        command.options.forEach { opt ->
            item.addOption(ScoreChoiceItemOptionTemplate.create(
                content = opt.content,
                description = opt.description,
                score = opt.score,
            ))
        }
        return item
    }
}
