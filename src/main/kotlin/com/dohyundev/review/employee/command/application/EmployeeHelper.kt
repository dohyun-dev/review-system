package com.dohyundev.review.employee.command.application

import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.exception.EmployeeNotFoundException

object EmployeeHelper {
    fun findById(employeeId: Long, employeeRepository: EmployeeRepository): Employee =
        employeeRepository.findById(employeeId)
            .orElseThrow { EmployeeNotFoundException() }
}