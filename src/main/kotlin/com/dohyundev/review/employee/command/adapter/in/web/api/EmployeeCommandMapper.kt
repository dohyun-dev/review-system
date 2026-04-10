package com.dohyundev.review.employee.command.adapter.`in`.web.api

import com.dohyundev.review.employee.command.adapter.`in`.web.api.request.RegisterEmployeeRequest
import com.dohyundev.review.employee.command.adapter.`in`.web.api.request.UpdateEmployeeRequest
import com.dohyundev.review.employee.command.port.`in`.command.RegisterEmployeeCommand
import com.dohyundev.review.employee.command.port.`in`.command.UpdateEmployeeCommand
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper
interface EmployeeCommandMapper {
    fun toCommand(request: RegisterEmployeeRequest): RegisterEmployeeCommand

    @Mapping(target = "employeeId", source = "id")
    fun toCommand(id: Long, request: UpdateEmployeeRequest): UpdateEmployeeCommand
}
