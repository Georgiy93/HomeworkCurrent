package ru.netology.nmedia.repository

import okhttp3.Response
import ru.netology.nmedia.dto.Post

interface PostRepository {
    fun getAll(callback: Callback<List<Post>>)
    //    fun likeById(id: Long): Post
    // cтарый варинат
    fun likeById(post: Post, callback: Callback<Post> )
    fun save(post: Post, callback: Callback<Unit>)
    fun removeById(id: Long,callback: Callback<Unit>)
    fun avatarLoad(post: Post, callback: Callback<Post>)

    interface Callback<T> {
        fun onSuccess(data: T) {}
        fun onError(e: Exception) {}
    }
}

