package com.dohyundev.review.review.query.model

sealed class ReviewItemOptionView {
    abstract val id: Long
    abstract val label: String
    abstract val description: String?
    abstract val sortOrder: Int
}

