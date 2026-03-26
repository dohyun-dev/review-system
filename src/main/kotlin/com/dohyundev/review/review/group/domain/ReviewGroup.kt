package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.entity.BaseAggregateRootEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.OneToMany

@Entity
class ReviewGroup(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    var description: String? = null,

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: ReviewGroupStatus = ReviewGroupStatus.PREPARING,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewGroup")
    val reviews: MutableList<Review> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewGroup")
    val targets: MutableList<ReviewTarget> = mutableListOf(),

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewGroup")
    val reviewers: MutableList<Reviewer> = mutableListOf(),
) : BaseAggregateRootEntity<ReviewGroup>() {

    companion object {
        fun create(name: String, description: String? = null): ReviewGroup {
            require(name.isNotBlank()) { "리뷰 그룹 이름은 공백일 수 없습니다" }
            return ReviewGroup(name = name, description = description)
        }
    }

    fun start() {
        require(status == ReviewGroupStatus.PREPARING) { "준비 중 상태에서만 시작할 수 있습니다" }
        status = ReviewGroupStatus.IN_PROGRESS
    }

    fun close() {
        require(status == ReviewGroupStatus.IN_PROGRESS) { "진행 중 상태에서만 마감할 수 있습니다" }
        status = ReviewGroupStatus.CLOSED
    }

    fun end() {
        require(status == ReviewGroupStatus.CLOSED) { "마감 상태에서만 종료할 수 있습니다" }
        status = ReviewGroupStatus.ENDED
    }

    fun addReview(review: Review) {
        reviews.add(review)
        review.reviewGroup = this
    }

    fun removeReview(review: Review) {
        reviews.remove(review)
        review.reviewGroup = null
    }

    fun addTarget(target: ReviewTarget) {
        targets.add(target)
        target.reviewGroup = this
    }

    fun removeTarget(target: ReviewTarget) {
        targets.remove(target)
        target.reviewGroup = null
    }

    fun addReviewer(reviewer: Reviewer) {
        reviewers.add(reviewer)
        reviewer.reviewGroup = this
    }

    fun removeReviewer(reviewer: Reviewer) {
        reviewers.remove(reviewer)
        reviewer.reviewGroup = null
    }
}
