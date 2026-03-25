package com.dohyundev.review.employee.fixture

import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.employee.domain.entity.Password
import java.time.LocalDate

object EmployeeFixture {
    fun create(
        no: String = "EMP001",
        name: String = "테스트사원",
        password: Password = Password("encoded:0000"),
        hireDate: LocalDate = LocalDate.of(2024, 1, 1),
    ): Employee {
        return Employee(
            no = no,
            name = name,
            password = password,
            hireDate = hireDate,
        )
    }
}
