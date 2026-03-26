package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.SortOrderable
import com.dohyundev.review.common.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class ReviewSection(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    override var sortOrder: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false)
    var review: Review? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "section")
    val items: MutableList<ReviewItem> = mutableListOf(),
) : BaseEntity(), SortOrderable<ReviewSection> {

    companion object {
        fun create(name: String): ReviewSection {
            require(name.isNotBlank()) { "섹션 이름은 공백일 수 없습니다" }
            return ReviewSection(name = name)
        }
    }

    fun addItem(item: ReviewItem) {
        item.changeSortOrder((items.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        items.add(item)
        item.section = this
    }

    fun removeItem(item: ReviewItem) {
        items.remove(item)
        item.section = null
    }
}
