package com.dohyundev.review.review.query.repository

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.query.model.ReviewDetailView
import jakarta.persistence.EntityManager
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
class ReviewDetailQueryRepository(
    private val entityManager: EntityManager,
) {
    fun findDetail(reviewId: Long): ReviewDetailView? {
        val review = entityManager.createQuery(
            "SELECT r FROM Review r WHERE r.id = :reviewId",
            Review::class.java,
        ).setParameter("reviewId", reviewId)
            .resultList
            .firstOrNull() ?: return null

        return ReviewDetailView(review)
    }
}
