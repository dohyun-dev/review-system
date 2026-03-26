package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.domain.ReviewTemplateItem
import com.dohyundev.review.review.template.domain.TextItemTemplate
import org.springframework.stereotype.Component

@Component
class TextItemFactory : ReviewTemplateItemFactory {

    override fun supports(command: ReviewTemplateItemCommand) =
        command is ReviewTemplateItemCommand.Text

    override fun create(command: ReviewTemplateItemCommand): ReviewTemplateItem {
        command as ReviewTemplateItemCommand.Text
        return TextItemTemplate.create(
            question = command.question,
            description = command.description,
        )
    }
}
