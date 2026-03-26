package com.dohyundev.review.review.template.domain

import jakarta.persistence.CascadeType
import jakarta.persistence.DiscriminatorValue
import jakarta.persistence.Entity
import jakarta.persistence.OneToMany

@Entity
@DiscriminatorValue("SCORE_CHOICE")
class ScoreChoiceItemTemplate(
    question: String,
    description: String? = null,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true, mappedBy = "reviewTemplateItem")
    val options: MutableList<ScoreChoiceItemOptionTemplate> = mutableListOf(),
) : ReviewTemplateItem(question = question, description = description) {

    companion object {
        fun create(question: String, description: String? = null): ScoreChoiceItemTemplate {
            require(question.isNotBlank()) { "질문은 공백일 수 없습니다" }
            return ScoreChoiceItemTemplate(question = question, description = description)
        }
    }

    fun addOption(option: ScoreChoiceItemOptionTemplate) {
        option.changeSortOrder((options.maxOfOrNull { it.sortOrder } ?: 0) + 1)
        options.add(option)
        option.reviewTemplateItem = this
    }
}
