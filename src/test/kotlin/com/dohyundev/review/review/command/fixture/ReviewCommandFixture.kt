package com.dohyundev.review.review.command.fixture

import com.dohyundev.review.review.command.application.port.`in`.command.AppendReviewCommand
import com.dohyundev.review.review.command.domain.review.ReviewType
import com.dohyundev.review.review.command.domain.review.command.ReviewSectionCommand
import com.dohyundev.review.review.command.domain.review.command.TextReviewItemCommand
import com.dohyundev.review.review.command.application.port.`in`.command.UpdateReviewCommand

object ReviewCommandFixture {

    fun sectionCommand(
        title: String = "섹션",
        description: String? = null,
    ) = ReviewSectionCommand(
        title = title,
        description = description,
        items = listOf(
            TextReviewItemCommand(question = "질문")
        ),
    )

    fun createCommand(
        reviewGroupId: Long,
        type: ReviewType = ReviewType.SELF,
    ) = AppendReviewCommand(
        reviewGroupId = reviewGroupId,
        type = type,
        sections = listOf(sectionCommand()),
    )

    fun updateCommand(
        reviewGroupId: Long,
        reviewId: Long,
        type: ReviewType = ReviewType.UPWARD,
        sections: List<ReviewSectionCommand> = listOf(sectionCommand()),
    ) = UpdateReviewCommand(
        reviewGroupId = reviewGroupId,
        reviewId = reviewId,
        type = type,
        sections = sections,
    )

    fun updateFormCommand(
        reviewGroupId: Long,
        reviewId: Long,
        type: ReviewType = ReviewType.SELF,
    ) = UpdateReviewCommand(
        reviewGroupId = reviewGroupId,
        reviewId = reviewId,
        type = type,
        sections = listOf(sectionCommand(title = "수정된 섹션")),
    )
}
