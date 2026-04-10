package com.dohyundev.review.review.command.domain.review.command

sealed class ReviewItemCommand {
    abstract val question: String
    abstract val description: String?
    abstract val isRequired: Boolean
}
