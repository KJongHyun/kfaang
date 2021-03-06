package com.kfaang.main.board.dto

import com.kfaang.main.board.Post

class WritePostDto(
        var title: String? = null,
        var contents: String? = null,
        var categoryId: Long
) {

    fun toPost(): Post {
        return Post(title = this.title, contents = this.contents)
    }

}