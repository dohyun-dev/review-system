package com.dohyundev.review.review.template.domain

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
class ReviewTemplateSection(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var name: String,

    @Column(nullable = false)
    override var sortOrder: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_template_id", nullable = false)
    var reviewTemplate: ReviewTemplate? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "section")
    val items: MutableList<ReviewTemplateItem> = mutableListOf(),
) : BaseEntity(), SortOrderable<ReviewTemplateSection> {

    companion object {
        fun create(name: String): ReviewTemplateSection {
            require(name.isNotBlank()) { "섹션 이름은 공백일 수 없습니다" }
            return ReviewTemplateSection(name = name)
        }
    }

    fun addItem(item: ReviewTemplateItem) {
        item.changeSortOrder((items.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        items.add(item)
        item.section = this
    }

    fun removeItem(item: ReviewTemplateItem) {
        items.remove(item)
        item.section = null
    }
}
