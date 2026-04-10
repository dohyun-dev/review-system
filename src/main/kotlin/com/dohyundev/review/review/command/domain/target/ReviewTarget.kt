package com.dohyundev.review.review.command.domain.target

import com.dohyundev.review.review.command.domain.group.ReviewGroup
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ReviewTarget(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    val reviewGroup: ReviewGroup,

    @Column(nullable = false)
    val employeeId: Long,
) {
    init {
        reviewGroup.checkIsPreparing()
    }
}
