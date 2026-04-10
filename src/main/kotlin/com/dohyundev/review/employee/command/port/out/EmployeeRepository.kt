package com.dohyundev.review.employee.command.port.out

import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.EmployeeNo
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface EmployeeRepository : JpaRepository<Employee, Long> {
    fun findByNo(no: EmployeeNo): Optional<Employee>
    fun existsByNo(no: EmployeeNo): Boolean
}
