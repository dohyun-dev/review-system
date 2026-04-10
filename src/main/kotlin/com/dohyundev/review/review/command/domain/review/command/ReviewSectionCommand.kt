package com.dohyundev.review.review.command.domain.review.command

data class ReviewSectionCommand(
    val title: String,
    val description: String? = null,
    val items: List<ReviewItemCommand>,
)