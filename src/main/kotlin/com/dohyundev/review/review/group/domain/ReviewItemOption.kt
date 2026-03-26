package com.dohyundev.review.review.group.domain

import com.dohyundev.review.common.SortOrderable
import com.dohyundev.review.common.entity.BaseEntity
import io.hypersistence.utils.hibernate.id.Tsid
import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne

@Entity
class ReviewItemOption(
    @Id @Tsid
    var id: Long? = null,

    @Column(nullable = false)
    var content: String,

    var description: String? = null,

    var score: Double? = null,

    @Column(nullable = false)
    override var sortOrder: Int = 0,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_item_id", nullable = false)
    var reviewItem: ReviewItem? = null,
) : BaseEntity(), SortOrderable<ReviewItemOption> {

    companion object {
        fun create(content: String, score: Double, description: String? = null): ReviewItemOption {
            require(content.isNotBlank()) { "선택지 내용은 공백일 수 없습니다" }
            return ReviewItemOption(content = content, score = score, description = description)
        }
    }
}
