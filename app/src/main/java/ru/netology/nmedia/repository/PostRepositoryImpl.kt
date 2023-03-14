package ru.netology.nmedia.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import retrofit2.HttpException

import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.dao.PostDao
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.entity.PostEntity


class PostRepositoryImpl(private val postDao: PostDao) : PostRepository {
    override val data: LiveData<List<Post>> = postDao.getAll().map { it.map(PostEntity::toDto) }



    override suspend fun getAll() {
        val postsResponse = PostApi.service.getALL()
        if (!postsResponse.isSuccessful) {
            throw HttpException(postsResponse)

        }
        val posts = postsResponse.body().orEmpty()
        postDao.insert(posts.map(PostEntity::fromDto))
    }

    override suspend fun likeById(post: Post): Post {

        val likedByMeValue = post.likedByMe
        val postResponse = PostApi.service.let {
            if (likedByMeValue)
                it.dislikeById(post.id)

            else
                it.likeById(post.id)
        }
        if (!postResponse.isSuccessful) {
            throw HttpException(postResponse)

        }
        return postResponse.body() ?: throw HttpException(postResponse)

    }

    override suspend fun save(post: Post) {
        val response=PostApi.service.savePost(post)
        if (!response.isSuccessful) {
            throw HttpException(response)
            return response.body() ?: throw HttpException(response)
        }

    }

    override suspend fun removeById(id: Long) {
        val response = PostApi.service.deletePostById(id)
        if (!response.isSuccessful) {
            throw HttpException(response)
        }
        return response.body() ?: throw HttpException(response)
    }

}



