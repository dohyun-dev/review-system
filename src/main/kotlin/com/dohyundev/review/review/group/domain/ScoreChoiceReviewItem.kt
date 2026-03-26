package com.dohyundev.review.review.group.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceReviewItem(
    question: String,
    description: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewItem")
    val options: MutableList<ReviewItemOption> = mutableListOf(),
) : ReviewItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): ScoreChoiceReviewItem {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return ScoreChoiceReviewItem(question = question, description = description)
        }
    }

    fun addOption(option: ReviewItemOption) {
        option.changeSortOrder((options.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        options.add(option)
        option.reviewItem = this
    }

    fun removeOption(option: ReviewItemOption) {
        options.remove(option)
        option.reviewItem = null
    }
}
