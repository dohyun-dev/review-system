package com.dohyundev.review.review.template.application.factory

import com.dohyundev.review.review.template.application.dto.ReviewTemplateItemCommand
import org.springframework.stereotype.Component

@Component
class ReviewTemplateItemFactoryProvider(
    private val factories: List<ReviewTemplateItemFactory>,
) {
    fun get(command: ReviewTemplateItemCommand): ReviewTemplateItemFactory =
        factories.firstOrNull { it.supports(command) }
            ?: throw IllegalArgumentException("지원하지 않는 항목 유형입니다: ${command::class.simpleName}")
}
