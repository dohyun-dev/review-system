package com.dohyundev.review.employee.domain.entity

import com.dohyundev.review.employee.domain.dto.ChangePasswordCommand
import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import com.dohyundev.review.employee.domain.dto.UpdateEmployeeCommand
import com.dohyundev.review.employee.domain.service.PasswordEncoder
import com.dohyundev.review.employee.fixture.EmployeeFixture
import io.mockk.every
import java.time.LocalDate
import io.mockk.mockk
import kr.co.modaoutlet.mgr.employee.domain.EmployeeStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class EmployeeTest {

    private val passwordEncoder = mockk<PasswordEncoder> {
        every { encode(any()) } answers { Password("encoded:${firstArg<String>()}") }
        every { matches(any(), any()) } answers { secondArg<String>() == "encoded:${firstArg<String?>()}" }
    }

    @Test
    fun `register - 기본 비밀번호 0000으로 사원을 생성한다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP001",
            name = "홍길동",
            hireDate = LocalDate.of(2024, 1, 1),
        )

        val employee = Employee.register(command, passwordEncoder)

        assertEquals("EMP001", employee.no)
        assertEquals("홍길동", employee.name)
        assertEquals(LocalDate.of(2024, 1, 1), employee.hireDate)
        assertEquals("encoded:${Employee.DEFAULT_PASSWORD}", employee.password.value)
    }

    @Test
    fun `register - 역할을 지정하면 해당 역할로 사원을 생성한다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP002",
            name = "김관리",
            hireDate = LocalDate.of(2024, 1, 1),
            role = EmployeeRole.ADMIN,
        )

        val employee = Employee.register(command, passwordEncoder)

        assertEquals(EmployeeRole.ADMIN, employee.role)
    }

    @Test
    fun `register - 역할을 지정하지 않으면 USER 역할로 사원을 생성한다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP003",
            name = "이사용",
            hireDate = LocalDate.of(2024, 1, 1),
        )

        val employee = Employee.register(command, passwordEncoder)

        assertEquals(EmployeeRole.USER, employee.role)
    }

    @Test
    fun `register - 신규 사원의 기본 상태는 재직이다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP004",
            name = "박재직",
            hireDate = LocalDate.of(2024, 1, 1),
        )

        val employee = Employee.register(command, passwordEncoder)

        assertEquals(EmployeeStatus.ACTIVE, employee.status)
    }

    @Test
    fun `update - 이름, 역할, 입사일, 상태를 변경한다`() {
        val employee = createEmployee()
        val command = UpdateEmployeeCommand(
            name = "변경된이름",
            role = EmployeeRole.ADMIN,
            hireDate = LocalDate.of(2025, 6, 1),
            status = EmployeeStatus.ON_LEAVE,
        )

        employee.update(command)

        assertEquals("변경된이름", employee.name)
        assertEquals(EmployeeRole.ADMIN, employee.role)
        assertEquals(LocalDate.of(2025, 6, 1), employee.hireDate)
        assertEquals(EmployeeStatus.ON_LEAVE, employee.status)
    }

    @Test
    fun `changePassword - 새 비밀번호로 인코딩하여 변경한다`() {
        val employee = createEmployee()
        val command = ChangePasswordCommand(password = "newPass123")

        employee.changePassword(command, passwordEncoder)

        assertEquals("encoded:newPass123", employee.password.value)
    }

    private fun createEmployee(): Employee = EmployeeFixture.create()
}
