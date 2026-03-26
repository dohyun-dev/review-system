package com.dohyundev.review.review.group.fixture

import com.dohyundev.review.review.group.domain.Review
import com.dohyundev.review.review.group.domain.ReviewGroup
import com.dohyundev.review.review.group.domain.ReviewItemOption
import com.dohyundev.review.review.group.domain.ReviewSection
import com.dohyundev.review.review.group.domain.Reviewer
import com.dohyundev.review.review.group.domain.ReviewTarget
import com.dohyundev.review.review.group.domain.ScoreChoiceReviewItem
import com.dohyundev.review.review.group.domain.TextReviewItem
import com.dohyundev.review.review.group.domain.TextareaReviewItem
import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.employee.domain.entity.EmployeeRole
import com.dohyundev.review.employee.domain.entity.Password
import java.time.LocalDate

object ReviewGroupFixture {

    fun createEmployee(no: String = "EMP001", name: String = "홍길동"): Employee =
        Employee(no = no, name = name, password = Password("encoded"), hireDate = LocalDate.now())

    fun createReviewGroup(name: String = "2026 상반기 리뷰"): ReviewGroup =
        ReviewGroup.create(name = name)

    fun createReview(): Review =
        Review.create()

    fun createSection(name: String = "직무 역량"): ReviewSection =
        ReviewSection.create(name = name)

    fun createTextItem(question: String = "잘한 점을 작성해주세요"): TextReviewItem =
        TextReviewItem.create(question = question)

    fun createTextareaItem(question: String = "개선할 점을 자세히 작성해주세요"): TextareaReviewItem =
        TextareaReviewItem.create(question = question)

    fun createScoreChoiceItem(question: String = "업무 성과는 어떠했나요?"): ScoreChoiceReviewItem =
        ScoreChoiceReviewItem.create(question = question)

    fun createItemOption(content: String = "매우 우수", score: Double = 5.0): ReviewItemOption =
        ReviewItemOption.create(content = content, score = score)

    fun createReviewTarget(employee: Employee = createEmployee()): ReviewTarget =
        ReviewTarget.create(employee = employee)

    fun createReviewer(
        review: Review = createReview(),
        reviewTarget: ReviewTarget = createReviewTarget(),
        employee: Employee = createEmployee(no = "EMP002", name = "김리뷰어"),
    ): Reviewer = Reviewer.create(review = review, reviewTarget = reviewTarget, employee = employee)
}
