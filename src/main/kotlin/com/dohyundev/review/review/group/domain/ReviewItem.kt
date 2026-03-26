package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.SortOrderable
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
@DiscriminatorColumn(name = "item_type")
abstract class ReviewItem(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var question: String,

    var description: String? = null,

    @Column(nullable = false)
    override var sortOrder: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_section_id", nullable = false)
    var section: ReviewSection? = null,
) : BaseEntity(), SortOrderable<ReviewItem>
