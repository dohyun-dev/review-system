package com.dohyundev.review.review.command.domain.review

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceReviewItem(
    id: Long? = null,
    question: String,
    description: String? = null,
    isRequired: Boolean = false,
    sortOrder: Int = 0,
    options: MutableList<ScoreChoiceReviewItemOption> = mutableListOf(),
) : BaseChoiceReviewItem(
    id = id,
    question = question,
    description = description,
    isRequired = isRequired,
    sortOrder = sortOrder,
    options = options,
)
