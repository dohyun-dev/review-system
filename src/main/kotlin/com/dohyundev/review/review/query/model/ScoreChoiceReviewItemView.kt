package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption

data class ScoreChoiceReviewItemView(
    override val id: Long,
    override val question: String,
    override val description: String?,
    override val isRequired: Boolean,
    override val sortOrder: Int,
    val options: List<ReviewItemOptionView>,
) : ReviewItemView() {
    constructor(item: ScoreChoiceReviewItem) : this(
        id = item.id!!,
        question = item.question,
        description = item.description,
        isRequired = item.isRequired,
        sortOrder = item.sortOrder,
        options = item.options.sortedBy { it.sortOrder }.map { opt ->
            ScoreChoiceOptionView(
                id = opt.id!!,
                label = opt.label,
                description = opt.description,
                sortOrder = opt.sortOrder,
                score = (opt as ScoreChoiceReviewItemOption).score,
            )
        },
    )
}
