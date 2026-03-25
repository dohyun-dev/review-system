package com.dohyundev.review.employee.application.port.out

import com.dohyundev.review.employee.domain.entity.Employee
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface EmployeeRepository: JpaRepository<Employee, Long> {
    fun findByNo(no: String): Optional<Employee>
}