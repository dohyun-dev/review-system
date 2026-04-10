package com.dohyundev.review.review.command.adapter.`in`.web.api

import com.dohyundev.review.review.command.adapter.`in`.web.api.request.CreateReviewGroupRequest
import com.dohyundev.review.review.command.adapter.`in`.web.api.request.UpdateReviewGroupRequest
import com.dohyundev.review.review.command.application.port.`in`.CloseReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.CreateReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.DeleteReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.ReopenReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.StartReviewGroupUseCase
import com.dohyundev.review.review.command.application.port.`in`.UpdateReviewGroupUseCase
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@Tag(name = "관리자 - 리뷰 그룹 관리", description = "리뷰 그룹 생성, 수정, 삭제, 상태 변경 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/review-groups")
class AdminReviewGroupCommandApiController(
    private val createReviewGroupUseCase: CreateReviewGroupUseCase,
    private val updateReviewGroupUseCase: UpdateReviewGroupUseCase,
    private val deleteReviewGroupUseCase: DeleteReviewGroupUseCase,
    private val startReviewGroupUseCase: StartReviewGroupUseCase,
    private val closeReviewGroupUseCase: CloseReviewGroupUseCase,
    private val reopenReviewGroupUseCase: ReopenReviewGroupUseCase,
) {
    @Operation(summary = "리뷰 그룹 생성")
    @ApiResponse(responseCode = "201", description = "생성 성공")
    @PostMapping
    fun create(@RequestBody @Valid request: CreateReviewGroupRequest): ResponseEntity<Void> {
        createReviewGroupUseCase.create(request.toCommand())
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "리뷰 그룹 수정")
    @ApiResponse(responseCode = "204", description = "수정 성공")
    @PutMapping("/{reviewGroupId}")
    fun update(
        @PathVariable reviewGroupId: Long,
        @RequestBody @Valid request: UpdateReviewGroupRequest,
    ): ResponseEntity<Void> {
        updateReviewGroupUseCase.update(request.toCommand(reviewGroupId))
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "리뷰 그룹 삭제")
    @ApiResponse(responseCode = "204", description = "삭제 성공")
    @DeleteMapping("/{reviewGroupId}")
    fun delete(@PathVariable reviewGroupId: Long): ResponseEntity<Void> {
        deleteReviewGroupUseCase.delete(reviewGroupId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "리뷰 그룹 시작")
    @ApiResponse(responseCode = "204", description = "시작 성공")
    @PostMapping("/{reviewGroupId}/start")
    fun start(@PathVariable reviewGroupId: Long): ResponseEntity<Void> {
        startReviewGroupUseCase.start(reviewGroupId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "리뷰 그룹 마감")
    @ApiResponse(responseCode = "204", description = "마감 성공")
    @PostMapping("/{reviewGroupId}/close")
    fun close(@PathVariable reviewGroupId: Long): ResponseEntity<Void> {
        closeReviewGroupUseCase.close(reviewGroupId)
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "리뷰 그룹 재개")
    @ApiResponse(responseCode = "204", description = "재개 성공")
    @PostMapping("/{reviewGroupId}/reopen")
    fun reopen(@PathVariable reviewGroupId: Long): ResponseEntity<Void> {
        reopenReviewGroupUseCase.reopen(reviewGroupId)
        return ResponseEntity.noContent().build()
    }
}
