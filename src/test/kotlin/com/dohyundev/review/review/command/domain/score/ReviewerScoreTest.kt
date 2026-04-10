package com.dohyundev.review.review.command.domain.score

import com.dohyundev.review.review.command.domain.answer.ReviewAnswerItem
import com.dohyundev.review.review.command.domain.answer.ScoreChoiceReviewAnswerItem
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import com.dohyundev.review.review.command.fixture.ReviewSectionFixture
import com.dohyundev.review.review.command.fixture.ReviewerAnswerFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import java.math.BigDecimal
import kotlin.test.Test
import kotlin.test.assertEquals

class ReviewerScoreTest {

    @Test
    fun `ReviewerAnswer로부터 점수를 계산하여 ReviewerScore를 생성한다`() {
        val option = ReviewItemFixture.scoreChoiceOption(id = 10L, score = 4.0)
        val scoreItem = ReviewItemFixture.scoreChoiceItem(id = 1L, options = mutableListOf(option))
        val review = ReviewFixture.review(
            sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(scoreItem)))
        )
        val reviewer = ReviewerFixture.inProgressReviewer(review = review)
        val answerItem = ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 10L)
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = reviewer, items = mutableListOf(answerItem))

        val score = ReviewerScore.from(reviewerAnswer)

        assertEquals(0, BigDecimal("4.0").compareTo(score.score))
        assertEquals(reviewer, score.reviewer)
    }

    @Test
    fun `답변 항목이 없으면 점수가 0이다`() {
        val reviewer = ReviewerFixture.inProgressReviewer()
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = reviewer)

        val score = ReviewerScore.from(reviewerAnswer)

        assertEquals(0, BigDecimal.ZERO.compareTo(score.score))
    }

    @Test
    fun `여러 ScoreChoice 답변의 점수를 합산한다`() {
        val option1 = ReviewItemFixture.scoreChoiceOption(id = 10L, score = 4.0)
        val option2 = ReviewItemFixture.scoreChoiceOption(id = 20L, score = 3.0)
        val scoreItem1 = ReviewItemFixture.scoreChoiceItem(id = 1L, options = mutableListOf(option1))
        val scoreItem2 = ReviewItemFixture.scoreChoiceItem(id = 2L, sortOrder = 2, options = mutableListOf(option2))
        val review = ReviewFixture.review(
            sections = mutableListOf(ReviewSectionFixture.section(items = mutableListOf(scoreItem1, scoreItem2)))
        )
        val reviewer = ReviewerFixture.inProgressReviewer(review = review)
        val answerItems = mutableListOf<ReviewAnswerItem>(
            ScoreChoiceReviewAnswerItem(reviewItemId = 1L, selectedOptionId = 10L),
            ScoreChoiceReviewAnswerItem(reviewItemId = 2L, selectedOptionId = 20L),
        )
        val reviewerAnswer = ReviewerAnswerFixture.reviewerAnswer(reviewer = reviewer, items = answerItems)

        val score = ReviewerScore.from(reviewerAnswer)

        assertEquals(0, BigDecimal("7.0").compareTo(score.score))
    }
}
