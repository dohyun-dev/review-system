package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.application.port.`in`.AddReviewScoreAdjustmentUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewScoreAdjustmentUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.AddReviewScoreAdjustmentCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewScoreAdjustmentRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import com.dohyundev.review.review.command.domain.score.Adjuster
import com.dohyundev.review.review.command.domain.score.AdjustmentPermissionChecker
import com.dohyundev.review.review.command.domain.score.ReviewScoreAdjustment
import com.dohyundev.review.review.command.domain.score.ReviewScoreAdjustmentNotFoundException
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewScoreAdjustmentCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
    private val reviewTargetRepository: ReviewTargetRepository,
    private val reviewScoreAdjustmentRepository: ReviewScoreAdjustmentRepository,
    private val adjustmentPermissionChecker: AdjustmentPermissionChecker,
) : AddReviewScoreAdjustmentUseCase, RemoveReviewScoreAdjustmentUseCase {

    override fun addReviewScoreAdjustment(command: AddReviewScoreAdjustmentCommand): Long {
        adjustmentPermissionChecker.check(command.adjusterId, command.reviewGroupId)

        val reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository)
        val reviewTarget = reviewTargetRepository.findById(command.reviewTargetId)
            .orElseThrow { ReviewTargetNotFoundException() }

        val adjustment = ReviewScoreAdjustment(
            reviewGroup = reviewGroup,
            reviewTarget = reviewTarget,
            adjuster = Adjuster(command.adjusterId),
            amount = command.amount,
            reason = command.reason,
        )

        return reviewScoreAdjustmentRepository.save(adjustment).id!!
    }

    override fun removeReviewScoreAdjustment(adjustmentId: Long) {
        val adjustment = reviewScoreAdjustmentRepository.findById(adjustmentId)
            .orElseThrow { ReviewScoreAdjustmentNotFoundException() }

        reviewScoreAdjustmentRepository.delete(adjustment)
    }
}
