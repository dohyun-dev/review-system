package com.dohyundev.review.organization.member.application.port.`in`

import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.member.application.port.out.DepartmentMemberRepository
import com.dohyundev.review.organization.member.application.port.service.DepartmentMemberCommandService
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import com.dohyundev.review.organization.member.domain.exception.DepartmentMemberNotFoundException
import com.dohyundev.review.organization.member.fixture.DepartmentMemberFixture
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class RemoveDepartmentMemberUseCaseTest {

    @Autowired
    lateinit var departmentMemberCommandService: DepartmentMemberCommandService

    @Autowired
    lateinit var departmentMemberRepository: DepartmentMemberRepository

    @Autowired
    lateinit var departmentRepository: DepartmentRepository

    @Autowired
    lateinit var employeeRepository: EmployeeRepository

    @Autowired
    lateinit var dutyRepository: DutyRepository

    @Test
    fun `부서원을 삭제한다`() {
        val member = saveMember()

        departmentMemberCommandService.remove(member.id!!)

        assertFalse(departmentMemberRepository.findById(member.id!!).isPresent)
    }

    @Test
    fun `존재하지 않는 부서원 ID면 DepartmentMemberNotFoundException을 던진다`() {
        assertThrows<DepartmentMemberNotFoundException> {
            departmentMemberCommandService.remove(999999L)
        }
    }

    private fun saveMember(): DepartmentMember {
        val department = departmentRepository.save(DepartmentMemberFixture.createDepartment())
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())

        return departmentMemberRepository.save(
            DepartmentMember(
                department = department,
                employee = employee,
                duty = duty,
                type = DepartmentMemberType.PRIMARY,
            )
        )
    }
}
