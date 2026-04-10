package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.review.command.application.port.`in`.CloseReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.ReopenReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.StartReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.SubmitReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.SubmitReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.TextReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerAnswerRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerScoreRepository
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItemOption
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import com.dohyundev.review.review.command.domain.target.ReviewTarget
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import java.math.BigDecimal
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@SpringBootTest
class ReviewerScoreCommandServiceTest {

    @Autowired lateinit var startReviewGroupUseCase: StartReviewGroupUseCase
    @Autowired lateinit var closeReviewGroupUseCase: CloseReviewGroupUseCase
    @Autowired lateinit var reopenReviewGroupUseCase: ReopenReviewGroupUseCase
    @Autowired lateinit var submitReviewerAnswerUseCase: SubmitReviewerAnswerUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewRepository: ReviewRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewerRepository: ReviewerRepository
    @Autowired lateinit var reviewerAnswerRepository: ReviewerAnswerRepository
    @Autowired lateinit var reviewerScoreRepository: ReviewerScoreRepository

    private lateinit var reviewGroup: ReviewGroup
    private lateinit var review: Review
    private lateinit var reviewer: Reviewer
    private lateinit var target: ReviewTarget
    private val employeeId = 1L

    private var requiredTextItemId = 0L
    private var scoreChoiceItemId = 0L
    private var validOptionId = 0L
    private var validOptionScore = 0.0

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())

        val option = ReviewItemFixture.scoreChoiceOption(label = "좋음", score = 4.0)
        val requiredTextItem = ReviewItemFixture.textItem(question = "주관식 질문", isRequired = true)
        val scoreItem = ReviewItemFixture.scoreChoiceItem(
            question = "점수 질문",
            isRequired = true,
            options = mutableListOf(option),
        )
        val section = ReviewSection(title = "섹션", sortOrder = 1, items = mutableListOf(requiredTextItem, scoreItem))
        review = reviewRepository.saveAndFlush(
            Review(reviewGroup = reviewGroup, type = ReviewType.DOWNWARD, sections = mutableListOf(section))
        )

        val savedSection = review.sections.first()
        requiredTextItemId = savedSection.items.filterIsInstance<TextReviewItem>().first().id!!
        val savedScoreItem = savedSection.items.filterIsInstance<ScoreChoiceReviewItem>().first()
        scoreChoiceItemId = savedScoreItem.id!!
        validOptionId = savedScoreItem.options.first().id!!
        validOptionScore = (savedScoreItem.options.first() as ScoreChoiceReviewItemOption).score

        target = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 9999L)
        )
        reviewer = reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(reviewGroup = reviewGroup, review = review, reviewTarget = target, employeeId = employeeId)
        )

        startReviewGroupUseCase.start(reviewGroup.id!!)
    }

    @AfterTest
    fun tearDown() {
        reviewerScoreRepository.deleteAll()
        reviewerAnswerRepository.deleteAll()
        reviewerRepository.deleteAll()
        reviewTargetRepository.deleteAll()
        reviewRepository.deleteAll()
        reviewGroupRepository.deleteAll()
    }

    private fun submitAnswer() {
        submitReviewerAnswerUseCase.submit(
            SubmitReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = listOf(
                    TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                    ScoreChoiceReviewerAnswerItemCommand(reviewItemId = scoreChoiceItemId, selectedOptionId = validOptionId),
                ),
            )
        )
    }

    @Test
    fun `리뷰 그룹 종료 시 제출된 답변의 점수가 계산된다`() {
        submitAnswer()

        closeReviewGroupUseCase.close(reviewGroup.id!!)

        val scores = reviewerScoreRepository.findAllByReviewerReviewGroupId(reviewGroup.id!!)
        assertEquals(1, scores.size)
        assertEquals(0, BigDecimal.valueOf(validOptionScore).compareTo(scores.first().score))
    }

    @Test
    fun `재종료 시 기존 점수를 삭제하고 재계산한다`() {
        submitAnswer()
        closeReviewGroupUseCase.close(reviewGroup.id!!)

        reopenReviewGroupUseCase.reopen(reviewGroup.id!!)
        closeReviewGroupUseCase.close(reviewGroup.id!!)

        val scores = reviewerScoreRepository.findAllByReviewerReviewGroupId(reviewGroup.id!!)
        assertEquals(1, scores.size)
        assertEquals(0, BigDecimal.valueOf(validOptionScore).compareTo(scores.first().score))
    }

    @Test
    fun `답변을 제출하지 않은 리뷰어는 점수가 계산되지 않는다`() {
        closeReviewGroupUseCase.close(reviewGroup.id!!)

        val scores = reviewerScoreRepository.findAllByReviewerReviewGroupId(reviewGroup.id!!)
        assertEquals(0, scores.size)
    }
}
