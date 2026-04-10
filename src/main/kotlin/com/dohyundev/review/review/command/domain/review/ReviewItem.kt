package com.dohyundev.review.review.command.domain.review

import com.dohyundev.review.common.SortOrderable
import jakarta.persistence.*

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "type")
abstract class ReviewItem(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    question: String,

    description: String? = null,

    val isRequired: Boolean = false,

    override var sortOrder: Int,
): SortOrderable<ReviewItem> {
    @Column(length = 100)
    val question: String = question.also {
        require(it.isNotBlank()) { "질문은 필수입니다." }
        require(it.length < 50) { "질문은 50자 이내로 작성해주세요." }
    }

    val description: String? = description.also {
        require(it.isNullOrBlank() || it.length < 100) { "설명은 100자 이내로 작성해주세요." }
    }
}
