package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class Review(
    @Id @Tsid
    var id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_group_id", nullable = false)
    var reviewGroup: ReviewGroup? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "review")
    val sections: MutableList<ReviewSection> = mutableListOf(),
) : BaseEntity() {

    companion object {
        fun create(): Review = Review()
    }

    fun addSection(section: ReviewSection) {
        section.changeSortOrder((sections.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        sections.add(section)
        section.review = this
    }

    fun removeSection(section: ReviewSection) {
        sections.remove(section)
        section.review = null
    }
}
