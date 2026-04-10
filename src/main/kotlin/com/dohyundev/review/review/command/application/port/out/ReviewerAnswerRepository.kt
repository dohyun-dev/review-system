package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.answer.ReviewerAnswer
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewerAnswerRepository : JpaRepository<ReviewerAnswer, Long> {
    fun findByReviewerId(reviewerId: Long): ReviewerAnswer?
    fun findAllByReviewerReviewGroupId(reviewGroupId: Long): List<ReviewerAnswer>
}
