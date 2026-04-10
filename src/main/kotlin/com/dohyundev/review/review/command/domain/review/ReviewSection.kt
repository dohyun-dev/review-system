package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.common.SortOrderable
import jakarta.persistence.CascadeType
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
class ReviewSection(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    title: String,

    description: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "review_section_id", nullable = false)
    val items: MutableList<ReviewItem> = mutableListOf(),

    override var sortOrder: Int,
) : SortOrderable<ReviewSection> {
    @Column(length = 100, nullable = false)
    var title: String = title
        protected set(value) {
            require(value.isNotBlank()) { "섹션명은 필수입니다." }
            require(value.length < 50) { "섹션명은 50자 이내로 작성해주세요." }
            field = value
        }

    var description: String? = description
        protected set(value) {
            require(value.isNullOrBlank() || value.length < 100) { "설명은 100자 이내로 작성해주세요." }
            field = value
        }

    init {
        this.title = title
        this.description = description
        require(items.isNotEmpty()) { "아이템은 최소 1개 이상이어야 합니다." }
    }
}
