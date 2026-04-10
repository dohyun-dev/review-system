package com.dohyundev.review.review.command.adapter.`in`.web.api

import com.dohyundev.review.review.command.adapter.`in`.web.api.request.AppendReviewerRequest
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewerUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewerUseCase
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
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자 - 리뷰어 관리", description = "리뷰어 생성/삭제 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/review-groups/{reviewGroupId}/reviewers")
class AdminReviewerCommandApiController(
    private val appendReviewerUseCase: AppendReviewerUseCase,
    private val removeReviewerUseCase: RemoveReviewerUseCase,
) {
    @Operation(summary = "리뷰어 생성")
    @ApiResponse(responseCode = "201", description = "생성 성공")
    @PostMapping
    fun appendReviewer(
        @PathVariable reviewGroupId: Long,
        @RequestBody @Valid request: AppendReviewerRequest,
    ): ResponseEntity<Void> {
        appendReviewerUseCase.appendReviewer(request.toCommand(reviewGroupId))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "리뷰어 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{reviewerId}")
    fun removeReviewer(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewerId: Long,
    ): ResponseEntity<Void> {
        removeReviewerUseCase.removeReviewer(reviewGroupId, reviewerId)
        return ResponseEntity.noContent().build()
    }
}