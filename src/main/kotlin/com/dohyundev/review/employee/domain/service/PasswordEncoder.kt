package com.dohyundev.review.employee.domain.service

import com.dohyundev.review.employee.domain.entity.Password

interface PasswordEncoder {
    fun encode(rawPassword: String): Password
    fun matches(rawPassword: String?, encodedPassword: String): Boolean
}