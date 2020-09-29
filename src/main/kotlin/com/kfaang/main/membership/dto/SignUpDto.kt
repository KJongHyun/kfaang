package com.kfaang.main.membership.dto

import com.kfaang.main.membership.Account
import org.hibernate.validator.constraints.Length
import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Pattern


data class SignUpDto(
        @Email
        @NotBlank
        val email: String,

        @NotBlank
        @Length(min = 8, max = 50)
        val password: String,

        @NotBlank
        @Length(min = 3, max = 20)
        @Pattern(regexp = "^[ㄱ-ㅎ가-힣a-z0-9_-]{3,20}$")
        val nickname: String
) {
        fun toAccount() : Account {
                val account = Account();
                account.email = this.email
                account.password = this.password
                account.nickname = this.nickname
                return account
        }
}