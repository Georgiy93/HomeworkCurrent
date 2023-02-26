package ru.netology.nmedia.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okio.Utf8
import ru.netology.nmedia.dto.Post
import java.io.IOException
import java.util.concurrent.TimeUnit


class PostRepositoryImpl : PostRepository {
    private val client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .build()
    private val gson = Gson()
    private val typeToken = object : TypeToken<List<Post>>() {}

    companion object {
        private const val BASE_URL = "http://10.0.2.2:9999"
        private val jsonType = "application/json".toMediaType()
    }

    override fun getAll(callback: PostRepository.Callback<List<Post>>) {
        val request: Request = Request.Builder()
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception(response.message))
                        return
                    }
                    val data: List<Post>? = response.body?.string()
                        .let { gson.fromJson(it, typeToken.type) }
                    data ?: run {
                        callback.onError(Exception("Body is null"))
                        return
                    }
                    callback.onSuccess(data)

                }

            })

    }



    override fun likeById(post: Post, callback: PostRepository.Callback<Post>) {
        val likedByMeValue = post.likedByMe

        val answer: Request = Request.Builder().let {
            if (likedByMeValue)
                it.delete()
            else
                it.post("".toRequestBody())
        }.url("${BASE_URL}/api/slow/posts/${post.id}/likes")
            .build()
        return client.newCall(answer)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception(response.message))
                        return
                    }
                    val data: Post = response.body?.string()
                        .let { gson.fromJson(it, Post::class.java) }


                    callback.onSuccess(data)

                }


            })



    }


// TODO: do this in homework


    override fun save(post: Post, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .post(gson.toJson(post).toRequestBody(jsonType))
            .url("${BASE_URL}/api/slow/posts")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception(response.message))
                        return
                    }
                    val data = response.close()


                    callback.onSuccess(data)
                }
            })

    }

    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        val request: Request = Request.Builder()
            .delete()
            .url("${BASE_URL}/api/slow/posts/$id")
            .build()

        return client.newCall(request)
            .enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onError(e)
                }

                override fun onResponse(call: Call, response: Response) {
                    if (!response.isSuccessful) {
                        callback.onError(Exception(response.message))
                        return
                    }
                    val data = response.close()


                    callback.onSuccess(data)
                }
            })
    }
}
