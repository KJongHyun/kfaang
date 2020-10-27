package com.kfaang.main.board

import com.kfaang.main.membership.Account
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Reply(
        @Id @GeneratedValue
        var id: Long? = null,

        var contents: String = "",

        @ManyToOne
        var post: Post? = null,

        @ManyToOne
        var account: Account? = null,

        @ManyToOne
        var parentReply: Reply? = null,

        @OneToMany
        var replies: MutableList<Reply> = mutableListOf(),

        var wroteAt: LocalDateTime? = null
) {


}