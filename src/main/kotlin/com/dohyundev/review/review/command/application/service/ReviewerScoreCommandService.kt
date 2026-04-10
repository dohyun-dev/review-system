package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.application.port.out.ReviewerAnswerRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerScoreRepository
import com.dohyundev.review.review.command.domain.group.ReviewGroupClosedEvent
import com.dohyundev.review.review.command.domain.score.ReviewerScore
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Service
class ReviewerScoreCommandService(
    private val reviewerAnswerRepository: ReviewerAnswerRepository,
    private val reviewerScoreRepository: ReviewerScoreRepository,
) {
    @TransactionalEventListener(phase = TransactionPhase.BEFORE_COMMIT)
    @Transactional(propagation = Propagation.MANDATORY)
    fun onReviewGroupClosed(event: ReviewGroupClosedEvent) {
        val reviewGroupId = event.reviewGroup.id!!

        reviewerScoreRepository.deleteAllByReviewerReviewGroupId(reviewGroupId)

        val scores = reviewerAnswerRepository.findAllByReviewerReviewGroupId(reviewGroupId)
            .map(ReviewerScore::from)

        reviewerScoreRepository.saveAll(scores)
    }
}
