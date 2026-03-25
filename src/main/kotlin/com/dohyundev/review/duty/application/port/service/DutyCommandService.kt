package com.dohyundev.review.duty.application.port.service

import com.dohyundev.review.duty.application.port.`in`.CreateDutyUseCase
import com.dohyundev.review.duty.application.port.`in`.UpdateDutyUseCase
import com.dohyundev.review.duty.application.port.out.DutyRepository
import com.dohyundev.review.duty.domain.Duty
import com.dohyundev.review.duty.application.port.dto.CreateDutyCommand
import com.dohyundev.review.duty.domain.dto.UpdateDutyCommand
import com.dohyundev.review.duty.domain.exception.DuplicateDutyNameException
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class DutyCommandService(
    private val dutyRepository: DutyRepository,
) : CreateDutyUseCase, UpdateDutyUseCase {

    override fun create(command: CreateDutyCommand): Duty {
        if (dutyRepository.findByName(command.name).isPresent) throw DuplicateDutyNameException()

        val duty = Duty.create(
            name = command.name,
            sortOrder = dutyRepository.findMaxSortOrder() + 1,
        )

        return dutyRepository.save(duty)
    }

    override fun update(id: Long, command: UpdateDutyCommand): Duty {
        val duty = dutyRepository.findByIdOrNull(id)
            ?: throw NoSuchElementException("직책을 찾을 수 없습니다. id=$id")

        if (duty.name != command.name && dutyRepository.findByName(command.name).isPresent) {
            throw DuplicateDutyNameException()
        }

        duty.update(command)

        return dutyRepository.save(duty)
    }
}
