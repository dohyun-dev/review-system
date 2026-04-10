package com.dohyundev.review.review.query.model

data class TextareaReviewerFormItemView(
    override val id: Long,
    override val question: String,
    override val description: String?,
    override val isRequired: Boolean,
    override val sortOrder: Int,
    val placeholder: String?,
    val maxLength: Int?,
    val answer: String?,
) : ReviewerFormItemView() {
    constructor(item: TextareaReviewItemView, answer: String?) : this(
        id = item.id,
        question = item.question,
        description = item.description,
        isRequired = item.isRequired,
        sortOrder = item.sortOrder,
        placeholder = item.placeholder,
        maxLength = item.maxLength,
        answer = answer,
    )
}
