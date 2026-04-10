package com.dohyundev.review.review.command.domain.review

import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity

@Entity
@DiscriminatorValue("TEXTAREA")
class TextareaReviewItem(
    id: Long? = null,
    question: String,
    description: String? = null,
    isRequired: Boolean = false,
    sortOrder: Int = 0,
    placeholder: String? = null,
    maxLength: Int? = null,
) : ReviewItem(
    id = id,
    question = question,
    description = description,
    isRequired = isRequired,
    sortOrder = sortOrder,
) {
    val placeholder: String? = placeholder

    val maxLength: Int? = maxLength
}
