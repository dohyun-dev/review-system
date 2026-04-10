package com.dohyundev.review.review.command.adapter.`in`.web.api

import com.dohyundev.review.review.command.adapter.`in`.web.api.request.ReviewerAnswerRequest
import com.dohyundev.review.review.command.application.port.`in`.CancelReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.DraftReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.SubmitReviewerAnswerUseCase
import com.dohyundev.review.review.command.application.port.`in`.command.CancelReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.DraftReviewerAnswerCommand
import com.dohyundev.review.review.command.application.port.`in`.command.SubmitReviewerAnswerCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "리뷰어 - 답변", description = "리뷰 답변 임시저장/제출/취소 API")
@PreAuthorize("isAuthenticated()")
@RestController
@RequestMapping("/api/review-groups/{reviewGroupId}/reviewers/{reviewerId}/answers")
class ReviewerAnswerCommandApiController(
    private val draftReviewerAnswerUseCase: DraftReviewerAnswerUseCase,
    private val submitReviewerAnswerUseCase: SubmitReviewerAnswerUseCase,
    private val cancelReviewerAnswerUseCase: CancelReviewerAnswerUseCase,
) {

    @Operation(summary = "답변 임시저장")
    @ApiResponse(responseCode = "200", description = "임시저장 성공")
    @PostMapping("/draft")
    fun draft(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewerId: Long,
        @AuthenticationPrincipal employeeId: Long,
        @RequestBody @Valid request: ReviewerAnswerRequest,
    ): ResponseEntity<Void> {
        draftReviewerAnswerUseCase.draft(
            DraftReviewerAnswerCommand(
                reviewGroupId = reviewGroupId,
                reviewerId = reviewerId,
                employeeId = employeeId,
                answerItems = request.answers.map { it.toCommand() },
            )
        )
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "답변 제출")
    @ApiResponse(responseCode = "200", description = "제출 성공")
    @PostMapping("/submit")
    fun submit(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewerId: Long,
        @AuthenticationPrincipal employeeId: Long,
        @RequestBody @Valid request: ReviewerAnswerRequest,
    ): ResponseEntity<Void> {
        submitReviewerAnswerUseCase.submit(
            SubmitReviewerAnswerCommand(
                reviewGroupId = reviewGroupId,
                reviewerId = reviewerId,
                employeeId = employeeId,
                answerItems = request.answers.map { it.toCommand() },
            )
        )
        return ResponseEntity.ok().build()
    }

    @Operation(summary = "제출 취소")
    @ApiResponse(responseCode = "200", description = "취소 성공")
    @PostMapping("/cancel")
    fun cancel(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewerId: Long,
        @AuthenticationPrincipal employeeId: Long,
    ): ResponseEntity<Void> {
        cancelReviewerAnswerUseCase.cancel(
            CancelReviewerAnswerCommand(
                reviewGroupId = reviewGroupId,
                reviewerId = reviewerId,
                employeeId = employeeId,
            )
        )
        return ResponseEntity.ok().build()
    }
}
