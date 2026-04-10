package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.exception.ReviewNotFoundException
import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewCommand
import com.dohyundev.review.review.command.domain.review.exception.DuplicateReviewTypeException
import com.dohyundev.review.review.command.domain.review.service.ReviewFactory
import com.dohyundev.review.review.command.domain.review.service.ReviewSectionFactory
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.UpdateReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewCommand
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewCommandService(
    private val reviewRepository: ReviewRepository,
    private val reviewerRepository: ReviewerRepository,
    private val reviewFactory: ReviewFactory,
    private val reviewSectionFactory: ReviewSectionFactory,
) : AppendReviewUseCase, UpdateReviewUseCase, RemoveReviewUseCase {

    override fun appendReview(command: AppendReviewCommand): Long {
        checkDuplicate(command.reviewGroupId, command.type, null)

        val review = reviewFactory.create(command)

        return reviewRepository.save(review).id!!
    }

    override fun updateReview(command: UpdateReviewCommand) {
        val review = reviewRepository.findById(command.reviewId)
            .orElseThrow { ReviewNotFoundException() }

        checkDuplicate(command.reviewGroupId, command.type, review)

        val sections = reviewSectionFactory.createAll(command.sections)

        review.update(command.type, sections)
    }

    private fun checkDuplicate(
        reviewGroupId: Long,
        type: ReviewType,
        review: Review?
    ) {
        val existing = reviewRepository.findByReviewGroupIdAndType(reviewGroupId, type)

        if (existing.isPresent && existing.get().id != review?.id) {
            throw DuplicateReviewTypeException()
        }
    }

    override fun removeReview(reviewId: Long) {
        val review = reviewRepository.findById(reviewId).orElseThrow { ReviewNotFoundException() }

        review.reviewGroup.checkIsPreparing()

        reviewerRepository.deleteAll(reviewerRepository.findAllByReviewId(reviewId))

        reviewRepository.deleteById(reviewId)
    }
}
