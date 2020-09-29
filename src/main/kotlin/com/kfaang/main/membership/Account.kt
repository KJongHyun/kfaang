package com.kfaang.main.membership

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Account {

    @Id @GeneratedValue
    var id: Long? = null

    @Column(unique = true)
    var email: String? = null

    var password: String? = null

    var nickname: String? = null

    var joinedAt: LocalDateTime? = null

    @ElementCollection(fetch = FetchType.EAGER)
    @Enumerated(EnumType.STRING)
    var roles: MutableSet<AccountRole> = mutableSetOf()

    fun completeJoin() {
        joinedAt = LocalDateTime.now()
    }

    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    override fun toString() = kotlinToString(properties = toStringProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Account::id)
        private val toStringProperties = arrayOf(
                Account::id,
                Account::email
        )
    }
}