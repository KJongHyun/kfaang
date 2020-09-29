package com.kfaang.main.board

import au.com.console.kassava.kotlinEquals
import au.com.console.kassava.kotlinHashCode
import au.com.console.kassava.kotlinToString
import com.kfaang.main.membership.Account
import java.time.LocalDateTime
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.ManyToOne

@Entity
class Post {

    @Id @GeneratedValue
    var id: Long? = null
    var title: String? = null
    var contents: String? = null
    var wroteAt: LocalDateTime? = null

    @ManyToOne
    var category: Category? = null

    @ManyToOne
    var account: Account? = null

    override fun equals(other: Any?) = kotlinEquals(other = other, properties = equalsAndHashCodeProperties)

    override fun hashCode() = kotlinHashCode(properties = equalsAndHashCodeProperties)

    override fun toString() = kotlinToString(properties = toStringProperties)

    companion object {
        private val equalsAndHashCodeProperties = arrayOf(Post::id)
        private val toStringProperties = arrayOf(
                Post::id,
                Post::title
        )
    }

}