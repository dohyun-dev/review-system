package com.dohyundev.review.review.command.domain.review

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("SCORE_CHOICE_OPTION")
class ScoreChoiceReviewItemOption(
    id: Long? = null,
    label: String = "",
    description: String? = null,
    sortOrder: Int = 0,
    val score: Double,
) : ReviewItemOption(
    id = id,
    label = label,
    description = description,
    sortOrder = sortOrder,
)
