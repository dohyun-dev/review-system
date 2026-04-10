package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.common.exception.ForbiddenException
import com.dohyundev.review.review.command.application.port.`in`.CancelReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.DraftReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.SubmitReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.CancelReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.DraftReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.SubmitReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerAnswerRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.application.service.factory.ReviewerAnswerItemsFactory
import com.dohyundev.review.review.command.application.service.helper.ReviewGroupHelper
import com.dohyundev.review.review.command.application.service.helper.ReviewerHelper
import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.ReviewerAnswer
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class ReviewerAnswerCommandService(
    private val reviewGroupRepository: ReviewGroupRepository,
    private val reviewerRepository: ReviewerRepository,
    private val reviewerAnswerRepository: ReviewerAnswerRepository,
    private val reviewerAnswerItemsFactory: ReviewerAnswerItemsFactory,
) : DraftReviewerAnswerUseCase, SubmitReviewerAnswerUseCase, CancelReviewerAnswerUseCase {

    override fun draft(command: DraftReviewerAnswerCommand) {
        val reviewer = ReviewerHelper.findById(command.reviewerId, reviewerRepository)
        checkOwner(reviewer, command.employeeId)

        val answerItems = reviewerAnswerItemsFactory.create(command.answerItems)

        val reviewerAnswer = ReviewerAnswer(
            reviewer = reviewer,
            items = answerItems.toMutableList(),
        )
        reviewerAnswer.validateForDraft()

        saveOrReplaceAnswer(reviewer, answerItems)

        reviewer.markDraft()
    }

    override fun submit(command: SubmitReviewerAnswerCommand) {
        val reviewer = ReviewerHelper.findById(command.reviewerId, reviewerRepository)
        checkOwner(reviewer, command.employeeId)

        val answerItems = reviewerAnswerItemsFactory.create(command.answerItems)

        val reviewerAnswer = ReviewerAnswer(
            reviewer = reviewer,
            items = answerItems.toMutableList(),
        )
        reviewerAnswer.validateForSubmit()

        saveOrReplaceAnswer(reviewer, answerItems)

        reviewer.markSubmitted()
    }

    override fun cancel(command: CancelReviewerAnswerCommand) {
        val reviewer = ReviewerHelper.findById(command.reviewerId, reviewerRepository)
        checkOwner(reviewer, command.employeeId)
        val reviewGroup = ReviewGroupHelper.findById(command.reviewGroupId, reviewGroupRepository)
        check(reviewGroup.isInProgress()) { "진행 중인 리뷰 그룹에만 답변할 수 있습니다." }

        reviewer.cancelSubmission()
    }

    private fun checkOwner(reviewer: Reviewer, employeeId: Long) {
        if (reviewer.employeeId != employeeId) throw ForbiddenException()
    }

    private fun saveOrReplaceAnswer(reviewer: Reviewer, answerItems: List<ReviewAnswerItem>) {
        val existing = reviewerAnswerRepository.findByReviewerId(reviewer.id!!)
        if (existing != null) {
            existing.changeAnswers(answerItems)
        } else {
            reviewerAnswerRepository.save(
                ReviewerAnswer(
                    reviewer = reviewer,
                    items = answerItems.toMutableList(),
                )
            )
        }
    }
}
