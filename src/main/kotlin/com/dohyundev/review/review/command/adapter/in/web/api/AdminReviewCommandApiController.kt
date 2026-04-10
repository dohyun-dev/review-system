package com.dohyundev.review.review.command.adapter.`in`.web.api

import com.dohyundev.review.review.command.adapter.`in`.web.api.request.CreateReviewRequest
import com.dohyundev.review.review.command.adapter.`in`.web.api.request.UpdateReviewRequest
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewUseCase
import com.dohyundev.review.review.command.application.port.`in`.UpdateReviewUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자 - 리뷰 양식 관리", description = "리뷰 양식 생성, 수정, 삭제 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/review-groups/{reviewGroupId}/reviews")
class AdminReviewCommandApiController(
    private val appendReviewUseCase: AppendReviewUseCase,
    private val updateReviewUseCase: UpdateReviewUseCase,
    private val removeReviewUseCase: RemoveReviewUseCase,
) {
    @Operation(summary = "리뷰 양식 생성")
    @ApiResponse(responseCode = "201", description = "생성 성공")
    @PostMapping
    fun create(
        @PathVariable reviewGroupId: Long,
        @RequestBody @Valid request: CreateReviewRequest,
    ): ResponseEntity<Void> {
        appendReviewUseCase.appendReview(request.toCommand(reviewGroupId))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "리뷰 양식 수정 (섹션/항목)")
    @ApiResponse(responseCode = "204", description = "수정 성공")
    @PutMapping("/{reviewId}")
    fun update(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewId: Long,
        @RequestBody @Valid request: UpdateReviewRequest,
    ): ResponseEntity<Void> {
        updateReviewUseCase.updateReview(request.toCommand(reviewGroupId, reviewId))
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "리뷰 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{reviewId}")
    fun delete(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewId: Long,
    ): ResponseEntity<Void> {
        removeReviewUseCase.removeReview(reviewId)
        return ResponseEntity.noContent().build()
    }
}
