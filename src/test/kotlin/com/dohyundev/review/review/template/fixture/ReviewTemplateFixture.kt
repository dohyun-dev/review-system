package com.dohyundev.review.review.template.fixture

import com.dohyundev.review.review.template.domain.ReviewTemplate
import com.dohyundev.review.review.template.domain.ReviewTemplateItem
import com.dohyundev.review.review.template.domain.ReviewTemplateSection
import com.dohyundev.review.review.template.domain.ScoreChoiceItemOptionTemplate
import com.dohyundev.review.review.template.domain.ScoreChoiceItemTemplate
import com.dohyundev.review.review.template.domain.TextItemTemplate
import com.dohyundev.review.review.template.domain.TextareaItemTemplate

object ReviewTemplateFixture {

    fun createTemplate(name: String = "성과 리뷰 템플릿", sortOrder: Int = 1): ReviewTemplate =
        ReviewTemplate.create(name = name, sortOrder = sortOrder)

    fun createSection(name: String = "직무 역량"): ReviewTemplateSection =
        ReviewTemplateSection.create(name = name)

    fun createScoreChoiceItem(question: String = "업무 성과는 어떠했나요?"): ScoreChoiceItemTemplate =
        ScoreChoiceItemTemplate.create(question = question)

    fun createTextItem(question: String = "잘한 점을 작성해주세요"): TextItemTemplate =
        TextItemTemplate.create(question = question)

    fun createTextareaItem(question: String = "개선할 점을 자세히 작성해주세요"): TextareaItemTemplate =
        TextareaItemTemplate.create(question = question)

    fun createScoreOption(content: String = "매우 우수", score: Double = 5.0): ScoreChoiceItemOptionTemplate =
        ScoreChoiceItemOptionTemplate.create(content = content, score = score)

    fun createScoreChoiceItemWithOptions(): ScoreChoiceItemTemplate {
        val item = createScoreChoiceItem()
        item.addOption(ScoreChoiceItemOptionTemplate.create(content = "매우 우수", score = 5.0))
        item.addOption(ScoreChoiceItemOptionTemplate.create(content = "우수", score = 4.0))
        item.addOption(ScoreChoiceItemOptionTemplate.create(content = "보통", score = 3.0))
        return item
    }

    fun createSectionWithItems(): ReviewTemplateSection {
        val section = createSection()
        section.addItem(createScoreChoiceItem())
        section.addItem(createTextItem())
        return section
    }

    fun createTemplateWithSections(): ReviewTemplate {
        val template = createTemplate()
        template.addSection(createSection(name = "직무 역량"))
        template.addSection(createSection(name = "리더십"))
        return template
    }

    fun addItemsToSection(section: ReviewTemplateSection, vararg items: ReviewTemplateItem) {
        items.forEach { section.addItem(it) }
    }
}
