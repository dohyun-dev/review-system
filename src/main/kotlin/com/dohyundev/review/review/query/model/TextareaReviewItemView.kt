package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.TextareaReviewItem

data class TextareaReviewItemView(
    override val id: Long,
    override val question: String,
    override val description: String?,
    override val isRequired: Boolean,
    override val sortOrder: Int,
    val placeholder: String?,
    val maxLength: Int?,
) : ReviewItemView() {
    constructor(item: TextareaReviewItem) : this(
        id = item.id!!,
        question = item.question,
        description = item.description,
        isRequired = item.isRequired,
        sortOrder = item.sortOrder,
        placeholder = item.placeholder,
        maxLength = item.maxLength,
    )
}
