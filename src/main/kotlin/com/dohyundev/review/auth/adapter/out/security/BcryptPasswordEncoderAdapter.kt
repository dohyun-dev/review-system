package com.dohyundev.review.auth.adapter.out.security

import com.dohyundev.review.employee.command.domain.Password
import com.dohyundev.review.employee.command.domain.PasswordEncoder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class BcryptPasswordEncoderAdapter : PasswordEncoder {
    private val bcrypt = BCryptPasswordEncoder()

    override fun encode(rawPassword: String): Password {
        require(rawPassword.isNotBlank()) { "비밀번호는 비어 있을 수 없습니다." }
        return Password(bcrypt.encode(rawPassword)!!)
    }

    override fun matches(rawPassword: String?, encodedPassword: Password): Boolean {
        if (rawPassword == null) return false
        return bcrypt.matches(rawPassword, encodedPassword.value)
    }
}