package com.dohyundev.review.organization.member.application.port.`in`

import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.member.application.port.dto.UpdateDepartmentMemberCommand
import com.dohyundev.review.organization.member.application.port.out.DepartmentMemberRepository
import com.dohyundev.review.organization.member.application.port.service.DepartmentMemberCommandService
import com.dohyundev.review.organization.member.domain.entity.DepartmentMember
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import com.dohyundev.review.organization.member.domain.exception.AlreadyPrimaryDepartmentExistsException
import com.dohyundev.review.organization.member.domain.exception.DepartmentMemberNotFoundException
import com.dohyundev.review.organization.member.fixture.DepartmentMemberFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UpdateDepartmentMemberUseCaseTest {

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
    fun `부서원의 직책과 유형을 변경한다`() {
        val member = saveMember()
        val newDuty = dutyRepository.save(Duty(name = "부장", sortOrder = 2))

        val command = UpdateDepartmentMemberCommand(
            dutyId = newDuty.id!!,
            type = DepartmentMemberType.CONCURRENT,
        )

        departmentMemberCommandService.update(member.id!!, command)

        val found = departmentMemberRepository.findById(member.id!!).orElseThrow()
        assertEquals(newDuty.id, found.duty.id)
        assertEquals(DepartmentMemberType.CONCURRENT, found.type)
    }

    @Test
    fun `존재하지 않는 부서원 ID면 DepartmentMemberNotFoundException을 던진다`() {
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())
        val command = UpdateDepartmentMemberCommand(
            dutyId = duty.id!!,
            type = DepartmentMemberType.PRIMARY,
        )

        assertThrows<DepartmentMemberNotFoundException> {
            departmentMemberCommandService.update(999999L, command)
        }
    }

    @Test
    fun `이미 주부서가 존재하는 사원을 주부서로 변경하면 AlreadyPrimaryDepartmentExistsException을 던진다`() {
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())
        val dept1 = departmentRepository.save(DepartmentMemberFixture.createDepartment(name = "개발팀"))
        val dept2 = departmentRepository.save(DepartmentMemberFixture.createDepartment(name = "기획팀"))

        departmentMemberRepository.save(
            DepartmentMember(department = dept1, employee = employee, duty = duty, type = DepartmentMemberType.PRIMARY)
        )
        val concurrent = departmentMemberRepository.save(
            DepartmentMember(department = dept2, employee = employee, duty = duty, type = DepartmentMemberType.CONCURRENT)
        )

        val command = UpdateDepartmentMemberCommand(
            dutyId = duty.id!!,
            type = DepartmentMemberType.PRIMARY,
        )

        assertThrows<AlreadyPrimaryDepartmentExistsException> {
            departmentMemberCommandService.update(concurrent.id!!, command)
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
