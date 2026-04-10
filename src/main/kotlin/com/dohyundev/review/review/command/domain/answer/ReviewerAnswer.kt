package com.dohyundev.review.review.command.domain.answer

import com.dohyundev.review.review.command.domain.review.ReviewItem
import com.dohyundev.review.review.command.domain.reviewer.Reviewer
import java.math.BigDecimal
import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany

@Entity
class ReviewerAnswer(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reviewer_id", nullable = false)
    val reviewer: Reviewer,

    @OneToMany(cascade = [CascadeType.ALL], orphanRemoval = true)
    @JoinColumn(name = "reviewer_answer_id", nullable = false)
    val items: MutableList<ReviewAnswerItem> = mutableListOf(),
) {
    init {
        check(reviewer.reviewGroup.isInProgress()) { "진행 중인 리뷰 그룹에만 답변할 수 있습니다." }
    }

    fun changeAnswers(newItems: List<ReviewAnswerItem>) {
        items.clear()
        items.addAll(newItems)
    }

    fun validateForDraft() {
        val reviewItems = allReviewItems()
        items.forEach { answerItem ->
            val reviewItem = reviewItems.find { it.id == answerItem.reviewItemId } ?: return@forEach
            answerItem.validate(reviewItem)
        }
    }

    fun validateForSubmit() {
        checkRequiredItems()
        val reviewItems = allReviewItems()
        items.forEach { answerItem ->
            val reviewItem = reviewItems.find { it.id == answerItem.reviewItemId } ?: return@forEach
            answerItem.validate(reviewItem)
        }
    }

    fun calculateScore(): BigDecimal {
        val reviewItems = allReviewItems().associateBy { it.id }
        return items.fold(BigDecimal.ZERO) { acc, answerItem ->
            val reviewItem = reviewItems[answerItem.reviewItemId] ?: return@fold acc
            acc + answerItem.calculateScore(reviewItem)
        }
    }

    private fun allReviewItems(): List<ReviewItem> = reviewer.review.sections.flatMap { it.items }

    private fun checkRequiredItems() {
        allReviewItems().filter { it.isRequired }.forEach { reviewItem ->
            val answerItem = items.find { it.reviewItemId == reviewItem.id }
            check(answerItem?.hasValue() == true) { "'${reviewItem.question}' 항목은 필수입니다." }
        }
    }
}
