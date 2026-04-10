package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ReviewRepository : JpaRepository<Review, Long> {
    fun findAllByReviewGroupId(reviewGroupId: Long): List<Review>
    fun findByReviewGroupIdAndType(reviewGroupId: Long, type: ReviewType): Optional<Review>
    fun countByReviewGroupId(reviewGroupId: Long): Int
}
