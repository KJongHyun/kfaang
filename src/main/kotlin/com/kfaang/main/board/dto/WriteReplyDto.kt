package com.kfaang.main.board.dto

import com.kfaang.main.board.Reply

data class WriteReplyDto(
        var postId: Long,
        var parentReplyId: Long? = null,
        var contents: String
) {
    fun toReply(): Reply {
        return Reply(contents = contents)
    }
}
