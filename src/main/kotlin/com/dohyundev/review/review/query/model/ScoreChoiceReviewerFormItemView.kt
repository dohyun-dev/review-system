package com.dohyundev.review.review.query.model

data class ScoreChoiceReviewerFormItemView(
    override val id: Long,
    override val question: String,
    override val description: String?,
    override val isRequired: Boolean,
    override val sortOrder: Int,
    val options: List<ReviewItemOptionView>,
    val selectedOptionId: Long?
) : ReviewerFormItemView() {
    constructor(item: ScoreChoiceReviewItemView, selectedOptionId: Long?) : this(
        id = item.id,
        question = item.question,
        description = item.description,
        isRequired = item.isRequired,
        sortOrder = item.sortOrder,
        options = item.options,
        selectedOptionId = selectedOptionId,
    )
}
