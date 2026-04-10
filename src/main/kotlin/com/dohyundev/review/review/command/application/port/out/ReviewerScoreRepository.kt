package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.score.ReviewerScore
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewerScoreRepository : JpaRepository<ReviewerScore, Long> {
    fun findAllByReviewerReviewGroupId(reviewGroupId: Long): List<ReviewerScore>
    fun deleteAllByReviewerReviewGroupId(reviewGroupId: Long)
}
