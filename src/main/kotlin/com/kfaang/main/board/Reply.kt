package com.kfaang.main.board

import com.kfaang.main.membership.Account
import java.time.LocalDateTime
import javax.persistence.*

@Entity
class Reply(
        @Id @GeneratedValue
        var id: Long,

        @ManyToOne
        var account: Account,

        @OneToMany
        var replies: MutableList<Reply> = mutableListOf(),

        var wroteAt: LocalDateTime
) {


}