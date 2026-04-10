package com.dohyundev.review.review.command.adapter.`in`.web.api

import com.dohyundev.review.review.command.adapter.`in`.web.api.request.AppendReviewTargetRequest
import com.dohyundev.review.review.command.application.port.`in`.AppendReviewTargetUseCase
import com.dohyundev.review.review.command.application.port.`in`.RemoveReviewTargetUseCase
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

@Tag(name = "관리자 - 리뷰 대상자 관리", description = "리뷰 대상자 추가/삭제 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/review-groups/{reviewGroupId}/review-targets")
class AdminReviewTargetCommandApiController(
    private val appendReviewTargetUseCase: AppendReviewTargetUseCase,
    private val removeReviewTargetUseCase: RemoveReviewTargetUseCase,
) {
    @Operation(summary = "리뷰 대상자 추가")
    @ApiResponse(responseCode = "201", description = "추가 성공")
    @PostMapping
    fun appendReviewTarget(
        @PathVariable reviewGroupId: Long,
        @RequestBody @Valid request: AppendReviewTargetRequest,
    ): ResponseEntity<Void> {
        appendReviewTargetUseCase.appendReviewTarget(request.toCommand(reviewGroupId))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "리뷰 대상자 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{reviewTargetId}")
    fun removeReviewTarget(
        @PathVariable reviewGroupId: Long,
        @PathVariable reviewTargetId: Long,
    ): ResponseEntity<Void> {
        removeReviewTargetUseCase.removeReviewTarget(reviewTargetId)
        return ResponseEntity.noContent().build()
    }
}
