package com.dohyundev.review.review.command.domain.score

import com.dohyundev.review.review.command.domain.answer.ReviewerAnswer
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class ReviewerScore(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    val reviewer: Reviewer,

    val score: BigDecimal,
) {
    companion object {
        fun from(reviewerAnswer: ReviewerAnswer): ReviewerScore {
            return ReviewerScore(
                reviewer = reviewerAnswer.reviewer,
                score = reviewerAnswer.calculateScore(),
            )
        }
    }
}