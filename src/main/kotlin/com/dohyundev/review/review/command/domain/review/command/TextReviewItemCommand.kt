package com.dohyundev.review.review.command.domain.review.command

data class TextReviewItemCommand(
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    val placeholder: String? = null,
    val maxLength: Int? = null,
) : ReviewItemCommand()
