package com.dohyundev.review.employee.command.adapter.`in`.web.api.request

import com.dohyundev.review.employee.command.domain.EmployeeRole
import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size
import java.time.LocalDate

data class RegisterEmployeeRequest(
    @field:NotBlank(message = "사원번호는 비어 있을 수 없습니다.")
    @field:Size(min = 6, max = 6, message = "사원번호는 6자리여야 합니다.")
    @Schema(description = "사원번호 (6자리)", example = "000001")
    val employeeNo: String = "",

    @field:NotBlank(message = "이름은 비어 있을 수 없습니다.")
    @Schema(description = "이름", example = "홍길동")
    val name: String = "",

    @field:NotNull(message = "입사일은 필수입니다.")
    @Schema(description = "입사일", example = "2024-01-01")
    val hireDate: LocalDate,

    @field:NotNull(message = "권한은 필수입니다.")
    @Schema(description = "권한")
    val role: EmployeeRole
)
