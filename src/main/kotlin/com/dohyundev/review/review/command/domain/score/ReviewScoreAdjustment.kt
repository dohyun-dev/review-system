package com.dohyundev.review.review.command.domain.score

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.group.exception.ReviewGroupNotClosedException
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import java.math.BigDecimal

@Entity
class ReviewScoreAdjustment(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    val reviewGroup: ReviewGroup,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_target_id", nullable = false)
    val reviewTarget: ReviewTarget,

    @Embedded
    @AttributeOverride(name = "employeeId", column = Column(name = "adjuster_employee_id"))
    val adjuster: Adjuster,

    @Column(length = 300)
    val reason: String? = null,

    val amount: BigDecimal,
) {
    init {
        if (!reviewGroup.isClosed())
            throw ReviewGroupNotClosedException()
        require(reason?.length?.let { it < 150 } != false) { "사유는 150자 미만이어야 합니다." }
    }
}
