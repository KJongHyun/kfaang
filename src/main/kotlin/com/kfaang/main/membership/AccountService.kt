package com.kfaang.main.membership

import com.kfaang.main.AccountRepository
import com.kfaang.main.membership.dto.SignUpDto
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class AccountService(
        val accountRepository: AccountRepository,
        val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    fun processNewAccount(signUpDto: SignUpDto): Account {
        val account = signUpProcess(signUpDto)

        return accountRepository.save(account)
    }

    fun processNewAdminAccount(signUpDto: SignUpDto): Account {
        val account = signUpProcess(signUpDto)
        account.roles = mutableSetOf(AccountRole.ADMIN, AccountRole.USER)

        return accountRepository.save(account)
    }

    private fun signUpProcess(signUpDto: SignUpDto): Account {
        val account = signUpDto.toAccount()
        account.password = passwordEncoder.encode(account.password)
        account.completeJoin()
        return account
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val account = accountRepository.findByEmail(username) ?: throw UsernameNotFoundException(username)
        return UserAccount(account)
    }

}