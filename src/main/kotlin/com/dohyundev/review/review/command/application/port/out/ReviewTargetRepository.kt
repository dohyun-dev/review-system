package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.target.ReviewTarget
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ReviewTargetRepository : JpaRepository<ReviewTarget, Long> {
    fun findByReviewGroupIdAndEmployeeId(reviewGroupId: Long, employeeId: Long): Optional<ReviewTarget>
    fun findAllByReviewGroupId(reviewGroupId: Long): List<ReviewTarget>
    fun countByReviewGroupId(reviewGroupId: Long): Int
    fun deleteAllByReviewGroupId(reviewGroupId: Long)
}
