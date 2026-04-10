package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.employee.command.application.EmployeeHelper
import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.review.command.domain.reviewer.DuplicateReviewerException
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewerCommand
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewerUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewerUseCase
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import com.dohyundev.review.review.command.application.service.helper.ReviewHelper
import com.dohyundev.review.review.command.application.service.helper.ReviewTargetHelper
import com.dohyundev.review.review.command.application.service.helper.ReviewerHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewerCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
    private val reviewRepository: ReviewRepository,
    private val reviewTargetRepository: ReviewTargetRepository,
    private val reviewerRepository: ReviewerRepository,
    private val employeeRepository: EmployeeRepository,
) : AppendReviewerUseCase, RemoveReviewerUseCase {

    override fun appendReviewer(command: AppendReviewerCommand): Long {
        val reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository)
        val review = ReviewHelper.findById(command.reviewId, reviewRepository)
        val target = ReviewTargetHelper.findById(command.reviewTargetId, reviewTargetRepository)
        EmployeeHelper.findById(command.employeeId, employeeRepository)

        checkDuplicateReviewer(review.id!!, target.id!!, command.employeeId)

        val reviewer = Reviewer(
            reviewGroup = reviewGroup,
            review = review,
            reviewTarget = target,
            employeeId = command.employeeId,
        )

        return reviewerRepository.save(reviewer).id!!
    }

    private fun checkDuplicateReviewer(reviewId: Long, reviewTargetId: Long, employeeId: Long) {
        reviewerRepository.findByReviewIdAndReviewTargetIdAndEmployeeId(reviewId, reviewTargetId, employeeId)
            .ifPresent { throw DuplicateReviewerException() }
    }

    override fun removeReviewer(reviewGroupId: Long, reviewerId: Long) {
        val reviewer = ReviewerHelper.findById(reviewerId, reviewerRepository)
        reviewerRepository.delete(reviewer)
    }
}
