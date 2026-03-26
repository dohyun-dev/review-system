package com.dohyundev.review.review.group.domain

import com.dohyundev.review.review.group.fixture.ReviewGroupFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ReviewerTest {

    @Test
    fun `submitAnswers - 텍스트 답변을 제출한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createTextItem()
        val answer = TextReviewerAnswer(reviewItem = item, answer = "열심히 했습니다")

        reviewer.submitAnswers(listOf(answer))

        assertEquals(1, reviewer.answers.size)
        assertEquals(reviewer, answer.reviewer)
    }

    @Test
    fun `submitAnswers - 서술형 답변을 제출한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createTextareaItem()
        val answer = TextareaReviewerAnswer(reviewItem = item, answer = "개선할 점은 다음과 같습니다...")

        reviewer.submitAnswers(listOf(answer))

        assertEquals(1, reviewer.answers.size)
    }

    @Test
    fun `submitAnswers - 점수 선택형 답변을 제출한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createScoreChoiceItem()
        val option = ReviewGroupFixture.createItemOption()
        item.addOption(option)
        val answer = ScoreChoiceReviewerAnswer(reviewItem = item, selectedOption = option)

        reviewer.submitAnswers(listOf(answer))

        assertEquals(1, reviewer.answers.size)
    }

    @Test
    fun `submitAnswers - 텍스트 답변이 공백이면 예외가 발생한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createTextItem()
        val answer = TextReviewerAnswer(reviewItem = item, answer = " ")

        assertThrows<IllegalArgumentException> {
            reviewer.submitAnswers(listOf(answer))
        }
    }

    @Test
    fun `submitAnswers - 서술형 답변이 공백이면 예외가 발생한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createTextareaItem()
        val answer = TextareaReviewerAnswer(reviewItem = item, answer = " ")

        assertThrows<IllegalArgumentException> {
            reviewer.submitAnswers(listOf(answer))
        }
    }

    @Test
    fun `submitAnswers - 항목에 속하지 않는 선택지를 선택하면 예외가 발생한다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createScoreChoiceItem()
        val otherOption = ReviewGroupFixture.createItemOption()
        val answer = ScoreChoiceReviewerAnswer(reviewItem = item, selectedOption = otherOption)

        assertThrows<IllegalArgumentException> {
            reviewer.submitAnswers(listOf(answer))
        }
    }

    @Test
    fun `submitAnswers - 재제출 시 기존 답변이 교체된다`() {
        val reviewer = ReviewGroupFixture.createReviewer()
        val item = ReviewGroupFixture.createTextItem()
        reviewer.submitAnswers(listOf(TextReviewerAnswer(reviewItem = item, answer = "첫 번째 답변")))

        reviewer.submitAnswers(listOf(TextReviewerAnswer(reviewItem = item, answer = "수정된 답변")))

        assertEquals(1, reviewer.answers.size)
        assertEquals("수정된 답변", (reviewer.answers.first() as TextReviewerAnswer).answer)
    }
}
