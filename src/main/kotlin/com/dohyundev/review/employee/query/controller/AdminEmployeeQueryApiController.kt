package com.dohyundev.review.employee.query.controller

import com.dohyundev.review.employee.query.model.EmployeeListView
import com.dohyundev.review.employee.query.repository.EmployeeListQueryRepository
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Tag(name = "관리자 - 사원 조회", description = "사원 목록 조회 API")
@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequestMapping("/api/admin/employees")
class AdminEmployeeQueryApiController(
    private val employeeListQueryRepository: EmployeeListQueryRepository,
) {
    @Operation(summary = "사원 목록 조회")
    @GetMapping
    fun getEmployeeList(
        @PageableDefault(size = 20) pageable: Pageable,
    ): ResponseEntity<Page<EmployeeListView>> =
        ResponseEntity.ok(employeeListQueryRepository.findAll(pageable))
}
