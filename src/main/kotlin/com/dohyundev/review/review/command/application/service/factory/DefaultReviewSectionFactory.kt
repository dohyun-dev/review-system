package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.command.ReviewItemCommand
import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand
import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.domain.review.service.ReviewItemFactory
import com.dohyundev.review.review.command.domain.review.service.ReviewSectionFactory
import org.springframework.stereotype.Component

@Component
class DefaultReviewSectionFactory(
    private val reviewItemFactories: List<ReviewItemFactory>,
) : ReviewSectionFactory {

    override fun createAll(commands: List<ReviewSectionCommand>): List<ReviewSection> =
        commands.mapIndexed { index, command -> create(command, index + 1) }

    private fun create(command: ReviewSectionCommand, sortOrder: Int): ReviewSection {
        val section = ReviewSection(
            title = command.title,
            description = command.description,
            sortOrder = sortOrder,
            items = command.items.mapIndexed { index, item -> toItem(item, index + 1) }.toMutableList()
        )
        return section
    }

    private fun toItem(command: ReviewItemCommand, sortOrder: Int): ReviewItem =
        reviewItemFactories.first { it.supports(command) }.create(command)
            .also { it.sortOrder = sortOrder }
}
