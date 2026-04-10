package com.dohyundev.review.review.command.domain.review

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.JoinColumn
import jakarta.persistence.OneToMany

@Entity
abstract class BaseChoiceReviewItem(
    id: Long? = null,
    question: String,
    description: String? = null,
    isRequired: Boolean = false,
    sortOrder: Int = 0,
    options: List<ReviewItemOption> = mutableListOf()
) : ReviewItem(
    id = id,
    question = question,
    description = description,
    isRequired = isRequired,
    sortOrder = sortOrder,
) {
    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "review_item_id", nullable = false)
    var options: List<ReviewItemOption> = options
        protected set

    init {
        require(options.isNotEmpty()) { "선택지는 최소 1개 이상이어야 합니다." }
    }

    fun findByOptionId(optionId: Long): ReviewItemOption? = options.find { it.id == optionId }
}
