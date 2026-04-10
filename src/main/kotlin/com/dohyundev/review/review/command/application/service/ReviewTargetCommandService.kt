package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.review.command.domain.target.DuplicateReviewTargetException
import com.dohyundev.review.review.command.domain.target.ReviewTargetNotFoundException
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewTargetUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewTargetUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.CreateReviewTargetCommand
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewTargetCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
    private val reviewTargetRepository: ReviewTargetRepository,
    private val reviewerRepository: ReviewerRepository,
    private val employeeRepository: EmployeeRepository,
) : AppendReviewTargetUseCase, RemoveReviewTargetUseCase {

    override fun appendReviewTarget(command: CreateReviewTargetCommand): Long {
        val reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository)

        checkDuplicateTarget(command)

        checkExistingEmployee(command.employeeId)

        val target = ReviewTarget(reviewGroup = reviewGroup, employeeId = command.employeeId)

        return reviewTargetRepository.save(target).id!!
    }

    private fun checkDuplicateTarget(command: CreateReviewTargetCommand) {
        check(reviewTargetRepository.findByReviewGroupIdAndEmployeeId(command.reviewGroupId, command.employeeId).isEmpty)
            { throw DuplicateReviewTargetException() }
    }

    private fun checkExistingEmployee(employeeId: Long) {
        employeeRepository.findById(employeeId)
            .orElseThrow { EmployeeNotFoundException() }
    }

    override fun removeReviewTarget(reviewTargetId: Long) {
        val target = reviewTargetRepository.findById(reviewTargetId)
            .orElseThrow { ReviewTargetNotFoundException() }

        target.reviewGroup.checkIsPreparing()

        reviewerRepository.deleteAll(reviewerRepository.findAllByReviewTargetId(reviewTargetId))
        reviewTargetRepository.delete(target)
    }
}
