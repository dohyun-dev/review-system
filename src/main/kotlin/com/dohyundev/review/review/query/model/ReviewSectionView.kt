package com.dohyundev.review.review.query.model

import com.dohyundev.review.review.command.domain.review.ReviewSection

data class ReviewSectionView(
    val id: Long,
    val title: String,
    val description: String?,
    val sortOrder: Int,
    val items: List<ReviewItemView>,
) {
    constructor(section: ReviewSection) : this(
        id = section.id!!,
        title = section.title,
        description = section.description,
        sortOrder = section.sortOrder,
        items = section.items.sortedBy { it.sortOrder }.map { ReviewItemView.from(it) },
    )
}
