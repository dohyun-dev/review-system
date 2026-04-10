package com.dohyundev.review.employee.command.adapter.`in`.web.api

import com.dohyundev.review.employee.command.adapter.`in`.web.api.request.RegisterEmployeeRequest
import com.dohyundev.review.employee.command.adapter.`in`.web.api.request.UpdateEmployeeRequest
import com.dohyundev.review.employee.command.port.`in`.RegisterEmployeeUseCase
import com.dohyundev.review.employee.command.port.`in`.ResetPasswordUseCase
import com.dohyundev.review.employee.command.port.`in`.UpdateEmployeeUseCase
import com.dohyundev.review.employee.command.port.`in`.command.ResetPasswordCommand
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자 - 사원 관리", description = "사원 등록, 수정, 비밀번호 초기화 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/employees")
class AdminEmployeeCommandApiController(
    private val registerEmployeeUseCase: RegisterEmployeeUseCase,
    private val updateEmployeeUseCase: UpdateEmployeeUseCase,
    private val resetPasswordUseCase: ResetPasswordUseCase,
    private val employeeMapper: EmployeeCommandMapper,
) {

    @Operation(summary = "사원 등록")
    @ApiResponse(responseCode = "201", description = "등록 성공")
    @PostMapping
    fun register(@RequestBody @Valid request: RegisterEmployeeRequest): ResponseEntity<Void> {
        registerEmployeeUseCase.register(employeeMapper.toCommand(request))
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @Operation(summary = "사원 정보 수정")
    @ApiResponse(responseCode = "204", description = "수정 성공")
    @PutMapping("/{employeeId}")
    fun update(
        @PathVariable employeeId: Long,
        @RequestBody @Valid request: UpdateEmployeeRequest,
    ): ResponseEntity<Void> {
        updateEmployeeUseCase.update(employeeMapper.toCommand(employeeId, request))
        return ResponseEntity.noContent().build()
    }

    @Operation(summary = "비밀번호 초기화 (관리자)")
    @ApiResponse(responseCode = "204", description = "초기화 성공")
    @PostMapping("/{employeeId}/reset-password")
    fun resetPassword(@PathVariable employeeId: Long): ResponseEntity<Void> {
        resetPasswordUseCase.resetPassword(ResetPasswordCommand(employeeId = employeeId))
        return ResponseEntity.noContent().build()
    }
}
