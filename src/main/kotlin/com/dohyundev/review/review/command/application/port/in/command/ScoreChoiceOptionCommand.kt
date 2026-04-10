package com.dohyundev.review.review.command.application.port.`in`.command

data class ScoreChoiceOptionCommand(
    val label: String,
    val description: String? = null,
    val score: Double,
)
