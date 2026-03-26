package com.dohyundev.review.organization.member.fixture

import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.employee.domain.entity.Password
import com.dohyundev.review.organization.department.domain.entity.Department
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import java.time.LocalDate

object DepartmentMemberFixture {

    fun createDepartment(name: String = "개발팀"): Department {
        return Department(name = name)
    }

    fun createEmployee(no: String = "EMP001", name: String = "테스트사원"): Employee {
        return Employee(
            no = no,
            name = name,
            password = Password(value = "encoded:0000"),
            hireDate = LocalDate.of(2024, 1, 1),
        )
    }

    fun createDuty(name: String = "팀장", sortOrder: Int = 1): Duty {
        return Duty(name = name, sortOrder = sortOrder)
    }

    fun create(
        department: Department = createDepartment(),
        employee: Employee = createEmployee(),
        duty: Duty = createDuty(),
        type: DepartmentMemberType = DepartmentMemberType.PRIMARY,
    ): DepartmentMember {
        return DepartmentMember(
            department = department,
            employee = employee,
            duty = duty,
            type = type,
        )
    }
}
