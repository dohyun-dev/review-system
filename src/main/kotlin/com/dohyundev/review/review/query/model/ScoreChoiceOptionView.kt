package com.dohyundev.review.review.query.model

data class ScoreChoiceOptionView(
    override val id: Long,
    override val label: String,
    override val description: String?,
    override val sortOrder: Int,
    val score: Double,
) : ReviewItemOptionView()
