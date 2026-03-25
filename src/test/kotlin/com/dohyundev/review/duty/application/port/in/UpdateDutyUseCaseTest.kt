package com.dohyundev.review.duty.application.port.`in`

import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.duty.application.port.service.DutyCommandService
import com.dohyundev.review.duty.domain.DutyStatus
import com.dohyundev.review.duty.domain.dto.UpdateDutyCommand
import com.dohyundev.review.duty.domain.exception.DuplicateDutyNameException
import com.dohyundev.review.duty.fixture.DutyFixture
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@Transactional
class UpdateDutyUseCaseTest {

    @Autowired
    lateinit var dutyCommandService: DutyCommandService

    @Autowired
    lateinit var dutyRepository: DutyRepository

    @Test
    fun `직책 정보를 변경한다`() {
        val duty = dutyRepository.save(DutyFixture.create(name = "팀장", sortOrder = 1))
        val command = UpdateDutyCommand(name = "부장", sortOrder = 2, status = DutyStatus.INACTIVE)

        dutyCommandService.update(duty.id!!, command)

        val found = dutyRepository.findById(duty.id!!).orElseThrow()
        assertEquals("부장", found.name)
        assertEquals(2, found.sortOrder)
        assertEquals(DutyStatus.INACTIVE, found.status)
    }

    @Test
    fun `존재하지 않는 직책 ID면 NoSuchElementException을 던진다`() {
        val command = UpdateDutyCommand(name = "부장", sortOrder = 1, status = DutyStatus.ACTIVE)

        assertThrows<NoSuchElementException> {
            dutyCommandService.update(999999L, command)
        }
    }

    @Test
    fun `이미 존재하는 직책명으로 변경하면 DuplicateDutyNameException을 던진다`() {
        dutyRepository.save(DutyFixture.create(name = "부장", sortOrder = 1))
        val duty = dutyRepository.save(DutyFixture.create(name = "팀장", sortOrder = 2))
        val command = UpdateDutyCommand(name = "부장", sortOrder = 2, status = DutyStatus.ACTIVE)

        assertThrows<DuplicateDutyNameException> {
            dutyCommandService.update(duty.id!!, command)
        }
    }
}
