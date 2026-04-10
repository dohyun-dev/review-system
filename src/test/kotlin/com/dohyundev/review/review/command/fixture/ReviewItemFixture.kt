package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.command.domain.review.TextareaReviewItem

object ReviewItemFixture {

    fun scoreChoiceOption(
        id: Long? = null,
        label: String = "좋음",
        score: Double = 4.0,
        sortOrder: Int = 1,
    ) = ScoreChoiceReviewItemOption(
        id = id,
        label = label,
        score = score,
        sortOrder = sortOrder,
    )

    fun textItem(
        id: Long? = null,
        question: String = "질문",
        sortOrder: Int = 1,
        isRequired: Boolean = false,
        maxLength: Int? = null,
    ) = TextReviewItem(
        id = id,
        question = question,
        sortOrder = sortOrder,
        isRequired = isRequired,
        maxLength = maxLength,
    )

    fun textareaItem(
        id: Long? = null,
        question: String = "질문",
        sortOrder: Int = 1,
        isRequired: Boolean = false,
        maxLength: Int? = null,
    ) = TextareaReviewItem(
        id = id,
        question = question,
        sortOrder = sortOrder,
        isRequired = isRequired,
        maxLength = maxLength,
    )

    fun scoreChoiceItem(
        id: Long? = null,
        question: String = "질문",
        sortOrder: Int = 1,
        isRequired: Boolean = false,
        options: MutableList<ScoreChoiceReviewItemOption> = mutableListOf(
            scoreChoiceOption(label = "매우 좋음", score = 5.0, sortOrder = 1),
            scoreChoiceOption(label = "좋음", score = 4.0, sortOrder = 2),
        ),
    ) = ScoreChoiceReviewItem(
        id = id,
        question = question,
        sortOrder = sortOrder,
        isRequired = isRequired,
        options = options,
    )
}
