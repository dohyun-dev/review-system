package com.dohyundev.review.review.command.domain.reviewer

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class Reviewer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    val reviewGroup: ReviewGroup,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_target_id", nullable = false)
    val reviewTarget: ReviewTarget,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    val review: Review,

    val employeeId: Long,
) {
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReviewerStatus = ReviewerStatus.WAIT
        protected set

    init {
        reviewGroup.checkIsPreparing()

        if (review.type == ReviewType.SELF) {
            check(reviewTarget.employeeId == employeeId) { "셀프리뷰의 리뷰어는 리뷰 대상자와 같은 사원이어야 합니다." }
        }
    }

    fun enableReview() {
        check(status == ReviewerStatus.WAIT) { "대기 상태에서만 미제출 상태로 변경할 수 있습니다." }
        status = ReviewerStatus.NOT_SUBMITTED
    }

    fun markDraft() {
        check(status != ReviewerStatus.SUBMITTED) { "이미 제출되었습니다." }
        status = ReviewerStatus.DRAFT
    }

    fun markSubmitted() {
        check(status != ReviewerStatus.SUBMITTED) { "이미 제출되었습니다." }
        status = ReviewerStatus.SUBMITTED
    }

    fun cancelSubmission() {
        check(status == ReviewerStatus.SUBMITTED) { "제출 상태가 아닙니다." }
        status = ReviewerStatus.NOT_SUBMITTED
    }
}
