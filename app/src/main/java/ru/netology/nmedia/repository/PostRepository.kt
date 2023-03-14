package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import okhttp3.Response
import ru.netology.nmedia.dto.Post

interface PostRepository {
    val data:LiveData<List<Post>>
    suspend fun getAll()

    //    fun likeById(id: Long): Post
    // cтарый варинат
    suspend fun likeById(post: Post):Post
    suspend fun save(post: Post)
    suspend fun removeById(id: Long)
//    fun avatarLoad(post: Post, callback: Callback<Post>)




}

