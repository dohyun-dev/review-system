package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.port.dto.ReviewTemplateItemCommand
import com.dohyundev.review.review.template.domain.ReviewTemplateItem

interface ReviewTemplateItemFactory {
    fun supports(command: ReviewTemplateItemCommand): Boolean
    fun create(command: ReviewTemplateItemCommand): ReviewTemplateItem
}
