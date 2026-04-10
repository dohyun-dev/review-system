package com.dohyundev.review.review.command.domain.review.command

import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceOptionCommand

data class ScoreChoiceReviewItemCommand(
    override val question: String,
    override val description: String? = null,
    override val isRequired: Boolean = false,
    val options: List<ScoreChoiceOptionCommand>,
) : ReviewItemCommand()
