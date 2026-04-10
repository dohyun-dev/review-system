package com.dohyundev.review.review.command.domain.answer

import com.dohyundev.review.review.command.domain.answer.ScoreChoiceReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.TextReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.TextareaReviewAnswerItem
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import com.dohyundev.review.review.command.fixture.ReviewSectionFixture
import com.dohyundev.review.review.command.fixture.ReviewerAnswerFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class ReviewAnswerItemTest {

    // TextReviewAnswerItem

    @Test
    fun `TextReviewAnswerItem - answer가 null이면 hasValue는 false다`() {
        val item = TextReviewAnswerItem(reviewItemId = 1L, answer = null)
        assertFalse(item.hasValue())
    }

    @Test
    fun `TextReviewAnswerItem - answer가 있으면 hasValue는 true다`() {
        val item = TextReviewAnswerItem(reviewItemId = 1L, answer = "답변")
        assertTrue(item.hasValue())
    }

    @Test
    fun `TextReviewAnswerItem - maxLength를 초과하면 validate가 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, maxLength = 5)
        val item = TextReviewAnswerItem(reviewItemId = 1L, answer = "123456")
        assertFailsWith<IllegalStateException> { item.validate(reviewItem) }
    }

    @Test
    fun `TextReviewAnswerItem - maxLength 이내면 validate가 성공한다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, maxLength = 10)
        val item = TextReviewAnswerItem(reviewItemId = 1L, answer = "12345")
        item.validate(reviewItem)
    }

    // TextareaReviewAnswerItem

    @Test
    fun `TextareaReviewAnswerItem - answer가 null이면 hasValue는 false다`() {
        val item = TextareaReviewAnswerItem(reviewItemId = 1L, answer = null)
        assertFalse(item.hasValue())
    }

    @Test
    fun `TextareaReviewAnswerItem - answer가 있으면 hasValue는 true다`() {
        val item = TextareaReviewAnswerItem(reviewItemId = 1L, answer = "긴 답변")
        assertTrue(item.hasValue())
    }

    @Test
    fun `TextareaReviewAnswerItem - maxLength를 초과하면 validate가 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.textareaItem(id = 1L, maxLength = 5)
        val item = TextareaReviewAnswerItem(reviewItemId = 1L, answer = "123456")
        assertFailsWith<IllegalStateException> { item.validate(reviewItem) }
    }

    // ScoreChoiceReviewAnswerItem

    @Test
    fun `ScoreChoiceReviewAnswerItem - 유효하지 않은 selectedOptionId면 validate가 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.scoreChoiceItem(id = 1L)
        val item = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 999L)
        assertFailsWith<IllegalStateException> { item.validate(reviewItem) }
    }

    @Test
    fun `ScoreChoiceReviewAnswerItem - 유효한 selectedOptionId면 validate가 성공한다`() {
        val option = ReviewItemFixture.scoreChoiceOption(id = 10L)
        val reviewItem = ReviewItemFixture.scoreChoiceItem(id = 1L, options = mutableListOf(option))
        val item = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 10L)
        item.validate(reviewItem)
    }


    // ReviewerAnswer.validateForSubmit

    @Test
    fun `필수 항목에 답변이 없으면 validateForSubmit이 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, isRequired = true)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review))
        assertFailsWith<IllegalStateException> {
            reviewerAnswer.validateForSubmit()
        }
    }

    @Test
    fun `필수 항목의 답변이 null이면 validateForSubmit이 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, isRequired = true)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = TextReviewAnswerItem(reviewItemId = 1L, answer = null)
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        assertFailsWith<IllegalStateException> {
            reviewerAnswer.validateForSubmit()
        }
    }

    @Test
    fun `필수 항목이 모두 채워지면 validateForSubmit이 성공한다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, isRequired = true)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = TextReviewAnswerItem(reviewItemId = 1L, answer = "답변")
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        reviewerAnswer.validateForSubmit()
    }

    @Test
    fun `Text 답변이 maxLength를 초과하면 validateForSubmit이 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, maxLength = 5)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = TextReviewAnswerItem(reviewItemId = 1L, answer = "123456")
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        assertFailsWith<IllegalStateException> {
            reviewerAnswer.validateForSubmit()
        }
    }

    @Test
    fun `ScoreChoice 답변의 selectedOptionId가 유효하지 않으면 validateForSubmit이 예외를 발생시킨다`() {
        val reviewItem = ReviewItemFixture.scoreChoiceItem(id = 1L)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 999L)
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        assertFailsWith<IllegalStateException> {
            reviewerAnswer.validateForSubmit()
        }
    }

    @Test
    fun `ScoreChoice 답변의 selectedOptionId가 유효하면 validateForSubmit이 성공한다`() {
        val option = ReviewItemFixture.scoreChoiceOption(id = 10L)
        val reviewItem = ReviewItemFixture.scoreChoiceItem(id = 1L, options = mutableListOf(option))
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 10L)
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        reviewerAnswer.validateForSubmit()
    }

    // ReviewerAnswer.validateForDraft

    @Test
    fun `validateForDraft 시 Text 답변이 maxLength를 초과하면 예외가 발생한다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, maxLength = 5)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = TextReviewAnswerItem(reviewItemId = 1L, answer = "123456")
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        assertFailsWith<IllegalStateException> {
            reviewerAnswer.validateForDraft()
        }
    }

    @Test
    fun `validateForDraft 시 필수 항목이 비어 있어도 예외가 발생하지 않는다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L, isRequired = true)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review))
        reviewerAnswer.validateForDraft()
    }

    // ReviewerAnswer.calculateScore

    @Test
    fun `ScoreChoice 답변의 점수를 합산한다`() {
        val option = ReviewItemFixture.scoreChoiceOption(id = 10L, score = 4.0)
        val reviewItem = ReviewItemFixture.scoreChoiceItem(id = 1L, options = mutableListOf(option))
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val answerItem = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 10L)
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review), items = mutableListOf(answerItem))
        val score = reviewerAnswer.calculateScore()
        assertEquals(0, java.math.BigDecimal("4.0").compareTo(score))
    }

    @Test
    fun `답변 항목이 없으면 calculateScore는 0이다`() {
        val reviewItem = ReviewItemFixture.textItem(id = 1L)
        val review = ReviewFixture.review(sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(reviewItem))))
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = ReviewerFixture.inProgressReviewer(review = review))
        assertEquals(java.math.BigDecimal.ZERO, reviewerAnswer.calculateScore())
    }
}
