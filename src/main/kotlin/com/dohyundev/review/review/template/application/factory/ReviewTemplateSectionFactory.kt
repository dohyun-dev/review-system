package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.ReviewTemplateSectionCommand
import com.dohyundev.review.review.template.domain.ReviewTemplateSection
import org.springframework.stereotype.Component

@Component
class ReviewTemplateSectionFactory(
    private val itemFactoryProvider: ReviewTemplateItemFactoryProvider,
) {
    fun create(command: ReviewTemplateSectionCommand): ReviewTemplateSection =
        with(ReviewTemplateSection.create(name = command.name)) {
            command.items.forEach { addItem(itemFactoryProvider.get(it).create(it)) }
            this
        }
}
