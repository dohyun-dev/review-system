package com.dohyundev.review.employee.application.port.`in`

import com.dohyundev.review.employee.application.port.out.EmployeeRepository
import com.dohyundev.review.employee.application.port.service.EmployeeCommandService
import com.dohyundev.review.employee.domain.dto.RegisterEmployeeCommand
import com.dohyundev.review.employee.domain.entity.Employee
import com.dohyundev.review.employee.domain.entity.EmployeeRole
import com.dohyundev.review.employee.domain.exception.DuplicateEmployeeNoException
import kr.co.modaoutlet.mgr.employee.domain.EmployeeStatus
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@SpringBootTest
@Transactional
class RegisterEmployeeUseCaseTest {

    @Autowired
    lateinit var employeeCommandService: EmployeeCommandService

    @Autowired
    lateinit var employeeRepository: EmployeeRepository


    @Test
    fun `사원을 저장하고 DB에서 조회할 수 있다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP001",
            name = "홍길동",
            hireDate = LocalDate.of(2024, 1, 1),
        )

        val saved = employeeCommandService.register(command)

        val found = employeeRepository.findById(saved.id!!).orElseThrow()
        assertEquals("EMP001", found.no)
        assertEquals("홍길동", found.name)
        assertEquals(LocalDate.of(2024, 1, 1), found.hireDate)
        assertEquals(EmployeeRole.USER, found.role)
        assertEquals(EmployeeStatus.ACTIVE, found.status)
    }

    @Test
    fun `비밀번호는 기본값 0000이 인코딩되어 저장된다`() {
        val command = RegisterEmployeeCommand(
            no = "EMP002",
            name = "김사원",
            hireDate = LocalDate.of(2024, 1, 1),
        )

        val saved = employeeCommandService.register(command)

        assertNotEquals(Employee.DEFAULT_PASSWORD, saved.password.value)
    }

    @Test
    fun `중복된 사원번호로 등록하면 DuplicateEmployeeNoException을 던진다`() {
        employeeCommandService.register(
            RegisterEmployeeCommand(no = "EMP001", name = "홍길동", hireDate = LocalDate.of(2024, 1, 1))
        )

        assertThrows<DuplicateEmployeeNoException> {
            employeeCommandService.register(
                RegisterEmployeeCommand(no = "EMP001", name = "김철수", hireDate = LocalDate.of(2024, 1, 1))
            )
        }
    }
}
