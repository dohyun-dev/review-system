package com.dohyundev.review.employee.fixture

import com.dohyundev.review.employee.command.port.`in`.command.RegisterEmployeeCommand
import com.dohyundev.review.employee.command.domain.EmployeeRole
import java.time.LocalDate

object EmployeeCommandFixture {

    fun registerCommand(
        employeeNo: String = "000001",
        name: String = "홍길동",
        hireDate: LocalDate = LocalDate.of(2020, 1, 1),
        role: EmployeeRole = EmployeeRole.USER,
    ) = RegisterEmployeeCommand(
        employeeNo = employeeNo,
        name = name,
        hireDate = hireDate,
        role = role,
    )
}
