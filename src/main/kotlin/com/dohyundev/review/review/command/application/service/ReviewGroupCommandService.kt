package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.application.port.`in`.*
import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewGroupCommand
import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewGroupCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.ReviewGroupStartableChecker
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewGroupCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewTargetRepository: ReviewTargetRepository,
    private val reviewerRepository: ReviewerRepository,
    private val startableChecker: ReviewGroupStartableChecker,
) : CreateReviewGroupUseCase, UpdateReviewGroupUseCase, DeleteReviewGroupUseCase,
    StartReviewGroupUseCase, CloseReviewGroupUseCase, ReopenReviewGroupUseCase {

    override fun create(command: CreateReviewGroupCommand): Long {
        val reviewGroup = ReviewGroup(
            name = command.name,
            description = command.description,
            period = command.period,
            targetPeriod = command.targetPeriod,
        )
        return reviewGroupRepository.save(reviewGroup).id!!
    }

    override fun update(command: UpdateReviewGroupCommand) {
        val reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository)

        reviewGroup.update(
            name = command.name,
            description = command.description,
            period = command.period,
            targetPeriod = command.targetPeriod
        )
    }

    override fun delete(reviewGroupId: Long) {
        val reviewGroup = ReviewGroupHelper.findById(reviewGroupId, reviewGroupRepository)

        reviewGroup.checkIsPreparing()

        reviewerRepository.deleteAll(reviewerRepository.findAllByReviewGroupId(reviewGroupId))
        reviewTargetRepository.deleteAll(reviewTargetRepository.findAllByReviewGroupId(reviewGroupId))
        reviewRepository.deleteAll(reviewRepository.findAllByReviewGroupId(reviewGroupId))
        reviewGroupRepository.delete(reviewGroup)
    }

    override fun start(reviewGroupId: Long) {
        val reviewGroup = ReviewGroupHelper.findById(reviewGroupId, reviewGroupRepository)

        reviewGroup.start(startableChecker)
    }

    override fun close(reviewGroupId: Long) {
        val reviewGroup = ReviewGroupHelper.findById(reviewGroupId, reviewGroupRepository)

        reviewGroup.close()

        reviewGroupRepository.save(reviewGroup)
    }

    override fun reopen(reviewGroupId: Long) {
        val reviewGroup = ReviewGroupHelper.findById(reviewGroupId, reviewGroupRepository)

        reviewGroup.reopen()

        reviewGroupRepository.save(reviewGroup)
    }
}
