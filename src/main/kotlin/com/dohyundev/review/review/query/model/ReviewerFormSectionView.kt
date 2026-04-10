package com.dohyundev.review.review.query.model

data class ReviewerFormSectionView(
    val id: Long,
    val title: String,
    val description: String?,
    val sortOrder: Int,
    val items: List<ReviewerFormItemView>,
)
