package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.domain.ReviewTemplateItem
import com.dohyundev.review.review.template.domain.TextareaItemTemplate
import org.springframework.stereotype.Component

@Component
class TextareaItemFactory : ReviewTemplateItemFactory {

    override fun supports(command: ReviewTemplateItemCommand) =
        command is ReviewTemplateItemCommand.Textarea

    override fun create(command: ReviewTemplateItemCommand): ReviewTemplateItem {
        command as ReviewTemplateItemCommand.Textarea
        return TextareaItemTemplate.create(
            question = command.question,
            description = command.description,
        )
    }
}
