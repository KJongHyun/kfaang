package com.kfaang.main.membership.validator

import com.kfaang.main.AccountRepository
import com.kfaang.main.membership.Account
import com.kfaang.main.membership.dto.SignUpDto
import org.springframework.stereotype.Component
import org.springframework.validation.Errors
import org.springframework.validation.Validator

@Component
class SignUpDtoValidator(
        private val accountRepository: AccountRepository
) : Validator {
    override fun supports(clazz: Class<*>): Boolean {
        return SignUpDto::class.java.isAssignableFrom(clazz)
    }

    override fun validate(target: Any, errors: Errors) {
        val signUpDto = target as SignUpDto

        if (accountRepository.existsByEmail(signUpDto.email)) {
            errors.rejectValue("email", "invalid.email", arrayOf(signUpDto.email), "이미 사용중인 이메일 입니다.")
        }

        if (accountRepository.existsByNickname(signUpDto.nickname)) {
            errors.rejectValue("nickname", "invalid.nickname", arrayOf(signUpDto.nickname), "이미 사용중인 닉네임 입니다.")
        }

    }


}