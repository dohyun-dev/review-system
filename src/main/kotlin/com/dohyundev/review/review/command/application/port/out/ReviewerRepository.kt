package com.dohyundev.review.review.command.application.port.out

import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface ReviewerRepository : JpaRepository<Reviewer, Long> {
    fun findAllByReviewGroupId(reviewGroupId: Long): List<Reviewer>
    fun findAllByReviewId(reviewId: Long): List<Reviewer>
    fun findByReviewIdAndReviewTargetIdAndEmployeeId(reviewId: Long, reviewTargetId: Long, employeeId: Long): Optional<Reviewer>
    fun countByReviewGroupId(reviewGroupId: Long): Int
    fun findAllByReviewTargetId(reviewTargetId: Long): List<Reviewer>
}
