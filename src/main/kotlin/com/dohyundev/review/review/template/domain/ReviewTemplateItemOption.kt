package com.dohyundev.review.review.template.domain

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
@DiscriminatorColumn(name = "type")
abstract class ReviewTemplateItemOption(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var content: String,

    var description: String? = null,

    @Column(nullable = false)
    override var sortOrder: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_template_item_id", nullable = false)
    var reviewTemplateItem: ReviewTemplateItem? = null,
) : BaseEntity(), SortOrderable<ReviewTemplateItemOption>
