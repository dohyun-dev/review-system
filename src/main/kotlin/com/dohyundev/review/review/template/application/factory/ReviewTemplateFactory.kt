package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.CreateReviewTemplateCommand
import com.dohyundev.review.review.template.domain.ReviewTemplate
import org.springframework.stereotype.Component

@Component
class ReviewTemplateFactory(
    private val sectionFactory: ReviewTemplateSectionFactory,
) {
    fun create(command: CreateReviewTemplateCommand, sortOrder: Int): ReviewTemplate =
        with(ReviewTemplate.create(name = command.name, description = command.description, sortOrder = sortOrder)) {
            command.sections.forEach { addSection(sectionFactory.create(it)) }
            this
        }
}
