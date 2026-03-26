package com.dohyundev.review.organization.member.domain.entity

import com.dohyundev.review.organization.member.fixture.DepartmentMemberFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class DepartmentMemberTest {

    @Test
    fun `create - 부서, 사원, 직책, 유형으로 부서원을 생성한다`() {
        val department = DepartmentMemberFixture.createDepartment()
        val employee = DepartmentMemberFixture.createEmployee()
        val duty = DepartmentMemberFixture.createDuty()

        val member = DepartmentMember.create(
            department = department,
            employee = employee,
            duty = duty,
            type = DepartmentMemberType.PRIMARY,
        )

        assertEquals(department, member.department)
        assertEquals(employee, member.employee)
        assertEquals(duty, member.duty)
        assertEquals(DepartmentMemberType.PRIMARY, member.type)
    }

    @Test
    fun `update - 직책과 유형을 변경한다`() {
        val member = DepartmentMemberFixture.create(type = DepartmentMemberType.PRIMARY)
        val newDuty = DepartmentMemberFixture.createDuty(name = "부장", sortOrder = 2)

        member.update(duty = newDuty, type = DepartmentMemberType.CONCURRENT)

        assertEquals(newDuty, member.duty)
        assertEquals(DepartmentMemberType.CONCURRENT, member.type)
    }

}
