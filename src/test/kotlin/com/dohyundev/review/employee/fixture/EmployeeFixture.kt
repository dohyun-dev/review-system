package com.dohyundev.review.employee.fixture

import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.EmployeeNo
import com.dohyundev.review.employee.command.domain.EmployeeRole
import com.dohyundev.review.employee.command.domain.Password
import java.time.LocalDate

object EmployeeFixture {

    fun employee(
        id: Long? = null,
        no: String = "000001",
        name: String = "홍길동",
        hireDate: LocalDate = LocalDate.of(2020, 1, 1),
        role: EmployeeRole = EmployeeRole.USER,
    ) = Employee(
        id = id,
        no = EmployeeNo.create(no),
        name = name,
        hireDate = hireDate,
        password = Password(Password.DEFAULT_PASSWORD),
        role = role,
    )
}
