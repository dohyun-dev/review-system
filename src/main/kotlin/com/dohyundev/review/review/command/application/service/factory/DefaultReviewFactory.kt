package com.dohyundev.review.review.command.application.service.factory

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.service.ReviewFactory
import com.dohyundev.review.review.command.domain.review.service.ReviewSectionFactory
import com.dohyundev.review.review.command.domain.review.command.CreateReviewCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import org.springframework.stereotype.Component

@Component
class DefaultReviewFactory(
    private val reviewSectionFactory: ReviewSectionFactory,
    private val reviewGroupRepository: ReviewGroupRepository,
) : ReviewFactory {
    override fun create(command: CreateReviewCommand): Review =
        Review(
            type = command.type,
            sections = reviewSectionFactory.createAll(command.sections).toMutableList(),
            reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository),
        )
}
