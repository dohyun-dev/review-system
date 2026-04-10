package com.dohyundev.review.auth.application.service

import com.dohyundev.review.auth.application.exception.InvalidEmployeeException
import com.dohyundev.review.auth.application.exception.InvalidCredentialsException
import com.dohyundev.review.auth.application.exception.RefreshTokenNotFoundException
import com.dohyundev.review.auth.application.port.`in`.LoginUseCase
import com.dohyundev.review.auth.application.port.`in`.LogoutUseCase
import com.dohyundev.review.auth.application.port.`in`.TokenRefreshUseCase
import com.dohyundev.review.auth.application.port.`in`.command.LoginCommand
import com.dohyundev.review.auth.application.port.out.RefreshTokenRepository
import com.dohyundev.review.auth.application.port.out.TokenManager
import com.dohyundev.review.auth.domain.TokenSet
import com.dohyundev.review.auth.domain.exception.RefreshTokenExpiredException
import com.dohyundev.review.employee.command.port.out.EmployeeRepository
import com.dohyundev.review.employee.command.domain.Employee
import com.dohyundev.review.employee.command.domain.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AuthCommandService(
    private val employeeRepository: EmployeeRepository,
    private val passwordEncoder: PasswordEncoder,
    private val tokenManager: TokenManager,
    private val refreshTokenRepository: RefreshTokenRepository,
) : LoginUseCase, TokenRefreshUseCase, LogoutUseCase {

    override fun login(command: LoginCommand): TokenSet {
        val employee = employeeRepository.findByNo(command.toEmployeeNo())
            .orElseThrow { InvalidCredentialsException() }

        if (!passwordEncoder.matches(command.password, employee.password)) {
            throw InvalidCredentialsException()
        }

        return generateAndSaveToken(employee)
    }

    override fun refresh(refreshToken: String): TokenSet {
        val stored = refreshTokenRepository.findByToken(refreshToken)
            ?: throw RefreshTokenNotFoundException()

        if (stored.isExpired()) {
            throw RefreshTokenExpiredException()
        }

        refreshTokenRepository.deleteByToken(stored.token)

        val employee = employeeRepository.findById(stored.employeeId)
            .orElseThrow { InvalidEmployeeException() }

        return generateAndSaveToken(employee)
    }

    private fun generateAndSaveToken(employee: Employee): TokenSet {
        val tokenSet = tokenManager.generateToken(employee)

        refreshTokenRepository.save(tokenManager.parseRefreshToken(tokenSet.refreshToken))

        return tokenSet
    }

    override fun logout(refreshToken: String) {
        refreshTokenRepository.deleteByToken(refreshToken)
    }
}
