package ru.netology.nmedia.api

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.*
import ru.netology.nmedia.BuildConfig
import ru.netology.nmedia.dto.Post
import java.util.concurrent.TimeUnit




private val logger =HttpLoggingInterceptor().apply {
    level=HttpLoggingInterceptor.Level.BODY
}

private val client = OkHttpClient.Builder()
    .connectTimeout(30, TimeUnit.SECONDS)
    .let {
        if (BuildConfig.DEBUG) {
            it.addInterceptor(logger)
        } else {
            it
        }
    }
//    .addInterceptor(Interceptor { chain ->
//        val request: Request = chain.request()
//        val response = chain.proceed(request)
//
//        // todo deal with the issues the way you need to
//        if (response.code == 500) {
//            startActivity(
//                Intent(
//                    AppActivity,
//                    ru.netology.nmedia.activity.ErrorHandlingActivity::class.java
//                )
//            )
//            return@Interceptor response
//        }
//        response
//    })
    .build()
private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BuildConfig.BASE_URL)
    .client(client)
    .build()

interface PostApiService {
    @GET("posts")
    fun getALL(): Call<List<Post>>

    @DELETE("posts/{id}")
    fun deletePostById(@Path("id") id: Long): Call<Unit>

    @POST("posts")
    fun savePost(@Body body: Post): Call<Unit>

    @POST("posts/{id}/likes")
    fun likeById(@Path(value = "id") id: Long): Call<Post>

    @DELETE("posts/{id}/likes")
    fun dislikeById(@Path(value = "id") id: Long): Call<Post>
}

object PostApi {
    val service: PostApiService by lazy {
        retrofit.create()
    }

}









