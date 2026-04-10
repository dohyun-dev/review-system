package com.dohyundev.review.employee.command.domain

interface PasswordEncoder {
    fun encode(rawPassword: String): Password
    fun matches(rawPassword: String?, encodedPassword: Password): Boolean
}
