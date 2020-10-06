package com.kfaang.main.board

import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.membership.Account
import org.springframework.stereotype.Service

@Service
class BoardService(
        private val postRepository: PostRepository
) {

    fun writePost(writePostDto: WritePostDto, category: Category, account: Account) : Post {
        val post = writePostDto.toPost()

        post.category = category
        post.account = account

        return postRepository.save(post)
    }

}