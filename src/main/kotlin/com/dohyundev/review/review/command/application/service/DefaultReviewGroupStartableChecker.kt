package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewTargetCountException
import com.dohyundev.review.review.command.domain.group.exception.InsufficientReviewerCountException
import com.dohyundev.review.review.command.domain.group.ReviewGroupStartableChecker
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import org.springframework.stereotype.Component

@Component
class DefaultReviewGroupStartableChecker(
    private val reviewRepository: ReviewRepository,
    private val reviewTargetRepository: ReviewTargetRepository,
    private val reviewerRepository: ReviewerRepository,
) : ReviewGroupStartableChecker {

    override fun checkStartable(reviewGroup: ReviewGroup) {
        val id = reviewGroup.id!!
        if (reviewRepository.countByReviewGroupId(id) <= 0) throw InsufficientReviewCountException()
        if (reviewTargetRepository.countByReviewGroupId(id) <= 0) throw InsufficientReviewTargetCountException()
        if (reviewerRepository.countByReviewGroupId(id) <= 0) throw InsufficientReviewerCountException()
    }
}
