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
    fun processNewAccount(signUpDto: SignUpDto) : Account {
        val account = signUpDto.toAccount()
        account.password = passwordEncoder.encode(account.password)
        account.completeJoin()
        return accountRepository.save(account)
    }

    override fun loadUserByUsername(username: String): UserDetails {
        val account = accountRepository.findByEmail(username)?: throw UsernameNotFoundException(username)
        return User(account.email, account.password, authorities(account.roles))
    }

    private fun authorities(roles: MutableSet<AccountRole>): MutableCollection<out GrantedAuthority>? {
        return roles.map {
            SimpleGrantedAuthority("ROLE_${it.name}")
        }.toMutableSet()
    }
}