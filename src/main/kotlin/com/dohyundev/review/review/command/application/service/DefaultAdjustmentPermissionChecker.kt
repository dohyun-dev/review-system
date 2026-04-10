package com.dohyundev.review.review.command.application.service

import com.dohyundev.review.employee.command.domain.EmployeeRole
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException
import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.review.command.domain.score.AdjustmentPermissionChecker
import org.springframework.stereotype.Component

@Component
class DefaultAdjustmentPermissionChecker(
    private val employeeRepository: EmployeeRepository,
) : AdjustmentPermissionChecker {

    override fun check(employeeId: Long, reviewGroupId: Long) {
        val employee = employeeRepository.findById(employeeId)
            .orElseThrow { EmployeeNotFoundException() }

        check(employee.role == EmployeeRole.ADMIN || employee.role == EmployeeRole.REVIEW_RESULT_VIEWER) {
            "가감 권한이 없습니다."
        }
    }
}
