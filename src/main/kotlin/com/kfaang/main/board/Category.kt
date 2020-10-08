package com.kfaang.main.board

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.kfaang.main.membership.Account
import javax.persistence.*

@Entity
class Category(
    @Id @GeneratedValue
    var id: Long? = null,

    @Column(unique = true)
    var name: String? = null,

    @OneToOne
    var createBy: Account? = null
) {

    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    override fun toString() = kotlinToString(properties =  toStringProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Category::id)
        private val toStringProperties = arrayOf(
                Category::id,
                Category::name
        )
    }
}