package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.DiscriminatorColumn
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.Inheritance
import jakarta.persistence.InheritanceType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "answer_type")
abstract class ReviewerAnswer(
    @Id @Tsid
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    var reviewer: Reviewer? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_item_id", nullable = false)
    val reviewItem: ReviewItem,
) : BaseEntity() {
    abstract fun validate()
}
