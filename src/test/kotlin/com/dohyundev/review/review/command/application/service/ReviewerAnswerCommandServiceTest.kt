package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.common.exception.ForbiddenException
import com.dohyundev.review.review.command.application.port.`in`.CancelReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.DraftReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.SubmitReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.CancelReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.DraftReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.ScoreChoiceReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.SubmitReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.TextReviewerAnswerItemCommand
import com.dohyundev.review.review.command.application.port.out.ReviewGroupRepository
import com.dohyundev.review.review.command.application.port.out.ReviewRepository
import com.dohyundev.review.review.command.application.port.out.ReviewTargetRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerAnswerRepository
import com.dohyundev.review.review.command.application.port.out.ReviewerRepository
import com.dohyundev.review.review.command.domain.group.ReviewGroup
import com.dohyundev.review.review.command.domain.review.Review
import com.dohyundev.review.review.command.domain.review.ReviewSection
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.ScoreChoiceReviewItem
import com.dohyundev.review.review.command.domain.review.TextReviewItem
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import com.dohyundev.review.review.command.domain.reviewer.ReviewerStatus
import com.dohyundev.review.review.command.fixture.ReviewFixture
import com.dohyundev.review.review.command.fixture.ReviewGroupFixture
import com.dohyundev.review.review.command.fixture.ReviewItemFixture
import com.dohyundev.review.review.command.fixture.ReviewTargetFixture
import com.dohyundev.review.review.command.fixture.ReviewerFixture
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

@SpringBootTest
@Transactional
class ReviewerAnswerCommandServiceTest {

    @Autowired lateinit var draftReviewerAnswerUseCase: DraftReviewerAnswerUseCase
    @Autowired lateinit var submitReviewerAnswerUseCase: SubmitReviewerAnswerUseCase
    @Autowired lateinit var cancelReviewerAnswerUseCase: CancelReviewerAnswerUseCase
    @Autowired lateinit var reviewGroupRepository: ReviewGroupRepository
    @Autowired lateinit var reviewRepository: ReviewRepository
    @Autowired lateinit var reviewTargetRepository: ReviewTargetRepository
    @Autowired lateinit var reviewerRepository: ReviewerRepository
    @Autowired lateinit var reviewerAnswerRepository: ReviewerAnswerRepository
    @Autowired lateinit var startReviewGroupUseCase: com.dohyundev.review.review.command.application.port.`in`.StartReviewGroupUseCase

    private lateinit var reviewGroup: ReviewGroup
    private lateinit var review: Review
    private lateinit var reviewer: Reviewer
    private val employeeId = 1L

    private var requiredTextItemId = 0L
    private var scoreChoiceItemId = 0L
    private var validOptionId = 0L

    @BeforeTest
    fun setUp() {
        reviewGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())

        val option = ReviewItemFixture.scoreChoiceOption(label = "좋음", score = 4.0)
        val requiredTextItem = ReviewItemFixture.textItem(question = "주관식 질문", isRequired = true, maxLength = 10)
        val optionalScoreItem = ReviewItemFixture.scoreChoiceItem(
            question = "점수 질문",
            options = mutableListOf(option)
        )
        val section = ReviewSection(
            title = "섹션",
            sortOrder = 1,
            items = mutableListOf(requiredTextItem, optionalScoreItem),
        )
        review = reviewRepository.saveAndFlush(
            Review(
                reviewGroup = reviewGroup,
                type = ReviewType.DOWNWARD,
                sections = mutableListOf(section),
            )
        )

        val target = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = reviewGroup, employeeId = 9999L)
        )
        reviewer = reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(
                reviewGroup = reviewGroup,
                review = review,
                reviewTarget = target,
                employeeId = employeeId,
            )
        )

        startReviewGroupUseCase.start(reviewGroup.id!!)

        reviewRepository.flush()
        val savedReview = reviewRepository.findById(review.id!!).get()
        val savedSection = savedReview.sections.first()
        requiredTextItemId = savedSection.items.first { it is TextReviewItem }.id!!
        val scoreItem = savedSection.items.first { it is ScoreChoiceReviewItem }
                as ScoreChoiceReviewItem
        scoreChoiceItemId = scoreItem.id!!
        validOptionId = scoreItem.options.first().id!!
    }

    @Test
    fun `임시저장에 성공하면 DRAFT 상태가 된다`() {
        draftReviewerAnswerUseCase.draft(
            DraftReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = listOf(
                    TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                ),
            )
        )
        val saved = reviewerRepository.findById(reviewer.id!!).get()
        assertEquals(ReviewerStatus.DRAFT, saved.status)
    }

    @Test
    fun `임시저장 시 답변이 저장된다`() {
        draftReviewerAnswerUseCase.draft(
            DraftReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = listOf(
                    TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                ),
            )
        )
        val savedAnswer = reviewerAnswerRepository.findByReviewerId(reviewer.id!!)
        assertNotNull(savedAnswer)
        assertEquals(1, savedAnswer.items.size)
    }

    @Test
    fun `임시저장은 필수 항목이 없어도 성공한다`() {
        draftReviewerAnswerUseCase.draft(
            DraftReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = emptyList(),
            )
        )
        val saved = reviewerRepository.findById(reviewer.id!!).get()
        assertEquals(ReviewerStatus.DRAFT, saved.status)
    }

    @Test
    fun `제출에 성공하면 SUBMITTED 상태가 된다`() {
        submitReviewerAnswerUseCase.submit(
            SubmitReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = listOf(
                    TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                ),
            )
        )
        val saved = reviewerRepository.findById(reviewer.id!!).get()
        assertEquals(ReviewerStatus.SUBMITTED, saved.status)
    }

    @Test
    fun `제출 시 필수 항목이 없으면 예외가 발생한다`() {
        assertFailsWith<IllegalStateException> {
            submitReviewerAnswerUseCase.submit(
                SubmitReviewerAnswerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewerId = reviewer.id!!,
                    employeeId = employeeId,
                    answerItems = emptyList(),
                )
            )
        }
    }

    @Test
    fun `제출 시 Text 답변이 maxLength를 초과하면 예외가 발생한다`() {
        assertFailsWith<IllegalStateException> {
            submitReviewerAnswerUseCase.submit(
                SubmitReviewerAnswerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewerId = reviewer.id!!,
                    employeeId = employeeId,
                    answerItems = listOf(
                        TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "a".repeat(11)),
                    ),
                )
            )
        }
    }

    @Test
    fun `제출 시 ScoreChoice의 유효하지 않은 옵션이면 예외가 발생한다`() {
        assertFailsWith<IllegalStateException> {
            submitReviewerAnswerUseCase.submit(
                SubmitReviewerAnswerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewerId = reviewer.id!!,
                    employeeId = employeeId,
                    answerItems = listOf(
                        TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                        ScoreChoiceReviewerAnswerItemCommand(reviewItemId = scoreChoiceItemId, selectedOptionId = 999L),
                    ),
                )
            )
        }
    }

    @Test
    fun `제출 취소에 성공하면 NOT_SUBMITTED 상태가 된다`() {
        submitReviewerAnswerUseCase.submit(
            SubmitReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
                answerItems = listOf(
                    TextReviewerAnswerItemCommand(reviewItemId = requiredTextItemId, answer = "답변"),
                ),
            )
        )
        cancelReviewerAnswerUseCase.cancel(
            CancelReviewerAnswerCommand(
                reviewGroupId = reviewGroup.id!!,
                reviewerId = reviewer.id!!,
                employeeId = employeeId,
            )
        )
        val saved = reviewerRepository.findById(reviewer.id!!).get()
        assertEquals(ReviewerStatus.NOT_SUBMITTED, saved.status)
    }

    @Test
    fun `본인이 아닌 사용자가 임시저장하면 예외가 발생한다`() {
        assertFailsWith<ForbiddenException> {
            draftReviewerAnswerUseCase.draft(
                DraftReviewerAnswerCommand(
                    reviewGroupId = reviewGroup.id!!,
                    reviewerId = reviewer.id!!,
                    employeeId = 9999L,
                    answerItems = emptyList(),
                )
            )
        }
    }

    @Test
    fun `IN_PROGRESS가 아닌 리뷰 그룹에 임시저장하면 예외가 발생한다`() {
        val newGroup = reviewGroupRepository.saveAndFlush(ReviewGroupFixture.reviewGroup())
        val newReview = reviewRepository.saveAndFlush(
            ReviewFixture.review(reviewGroup = newGroup)
        )
        val newTarget = reviewTargetRepository.saveAndFlush(
            ReviewTargetFixture.target(reviewGroup = newGroup)
        )
        val newReviewer = reviewerRepository.saveAndFlush(
            ReviewerFixture.reviewer(reviewGroup = newGroup, review = newReview, reviewTarget = newTarget, employeeId = employeeId)
        )
        assertFailsWith<IllegalStateException> {
            draftReviewerAnswerUseCase.draft(
                DraftReviewerAnswerCommand(
                    reviewGroupId = newGroup.id!!,
                    reviewerId = newReviewer.id!!,
                    employeeId = employeeId,
                    answerItems = emptyList(),
                )
            )
        }
    }
}
