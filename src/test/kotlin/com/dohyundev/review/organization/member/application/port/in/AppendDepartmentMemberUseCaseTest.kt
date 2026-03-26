package com.dohyundev.review.organization.member.application.port.`in`

import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.organization.department.application.port.out.DepartmentRepository
import com.dohyundev.review.organization.member.application.port.dto.AppendDepartmentMemberCommand
import com.dohyundev.review.organization.member.application.port.out.DepartmentMemberRepository
import com.dohyundev.review.organization.member.application.port.service.DepartmentMemberCommandService
import com.dohyundev.review.organization.member.domain.entity.DepartmentMemberType
import com.dohyundev.review.organization.member.domain.exception.AlreadyPrimaryDepartmentExistsException
import com.dohyundev.review.organization.member.domain.exception.DuplicateDepartmentMemberException
import com.dohyundev.review.organization.member.fixture.DepartmentMemberFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class AppendDepartmentMemberUseCaseTest {

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
    fun `부서에 사원을 추가하고 DB에서 조회할 수 있다`() {
        val department = departmentRepository.save(DepartmentMemberFixture.createDepartment())
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())

        val command = AppendDepartmentMemberCommand(
            departmentId = department.id!!,
            employeeId = employee.id!!,
            dutyId = duty.id!!,
            type = DepartmentMemberType.PRIMARY,
        )

        val saved = departmentMemberCommandService.append(command)

        val found = departmentMemberRepository.findById(saved.id!!).orElseThrow()
        assertEquals(department.id, found.department.id)
        assertEquals(employee.id, found.employee.id)
        assertEquals(duty.id, found.duty.id)
        assertEquals(DepartmentMemberType.PRIMARY, found.type)
    }

    @Test
    fun `같은 부서에 같은 사원을 중복 추가하면 DuplicateDepartmentMemberException을 던진다`() {
        val department = departmentRepository.save(DepartmentMemberFixture.createDepartment())
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())

        val command = AppendDepartmentMemberCommand(
            departmentId = department.id!!,
            employeeId = employee.id!!,
            dutyId = duty.id!!,
            type = DepartmentMemberType.PRIMARY,
        )

        departmentMemberCommandService.append(command)

        assertThrows<DuplicateDepartmentMemberException> {
            departmentMemberCommandService.append(command)
        }
    }

    @Test
    fun `존재하지 않는 부서 ID로 추가하면 예외를 던진다`() {
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())

        val command = AppendDepartmentMemberCommand(
            departmentId = 999999L,
            employeeId = employee.id!!,
            dutyId = duty.id!!,
            type = DepartmentMemberType.PRIMARY,
        )

        assertThrows<NoSuchElementException> {
            departmentMemberCommandService.append(command)
        }
    }

    @Test
    fun `이미 주부서가 존재하는 사원을 주부서로 추가하면 AlreadyPrimaryDepartmentExistsException을 던진다`() {
        val employee = employeeRepository.save(DepartmentMemberFixture.createEmployee())
        val duty = dutyRepository.save(DepartmentMemberFixture.createDuty())
        val dept1 = departmentRepository.save(DepartmentMemberFixture.createDepartment(name = "개발팀"))
        val dept2 = departmentRepository.save(DepartmentMemberFixture.createDepartment(name = "기획팀"))

        departmentMemberCommandService.append(AppendDepartmentMemberCommand(
            departmentId = dept1.id!!, employeeId = employee.id!!, dutyId = duty.id!!, type = DepartmentMemberType.PRIMARY,
        ))

        assertThrows<AlreadyPrimaryDepartmentExistsException> {
            departmentMemberCommandService.append(AppendDepartmentMemberCommand(
                departmentId = dept2.id!!, employeeId = employee.id!!, dutyId = duty.id!!, type = DepartmentMemberType.PRIMARY,
            ))
        }
    }
}
