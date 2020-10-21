package com.kfaang.main.board

import com.kfaang.main.board.dto.WritePostDto
import com.kfaang.main.board.dto.WriteReplyDto
import com.kfaang.main.membership.Account
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class BoardService(
        private val postRepository: PostRepository,
        private val categoryRepository: CategoryRepository,
        private val replyRepository: ReplyRepository
) {

    fun writePost(writePostDto: WritePostDto, account: Account) : Post {
        val post = writePostDto.toPost()

        val category = categoryRepository.findById(writePostDto.categoryId).orElseThrow()

        post.category = category
        post.account = account

        return postRepository.save(post)
    }

    fun createCategory(category: Category, account: Account): Category {
        category.createBy = account
        return categoryRepository.save(category)
    }

    fun getPost(postId: Long): Post {

        return postRepository.findById(postId).orElseThrow()
    }

    fun writeReply(writeReplyDto: WriteReplyDto, account: Account): Reply {
        val reply = writeReplyDto.toReply()
        reply.account = account
        reply.post = postRepository.findById(writeReplyDto.postId).orElseThrow()
        reply.wroteAt = LocalDateTime.now()

        return replyRepository.save(reply)
    }
}