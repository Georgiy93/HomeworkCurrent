package ru.netology.nmedia.repository

import android.app.ProgressDialog.show
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getAttributionTag
import androidx.core.content.ContextCompat.startActivity
import kotlinx.coroutines.NonDisposableHandle.parent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import org.xml.sax.ErrorHandler
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.netology.nmedia.R
import ru.netology.nmedia.activity.AppActivity
import ru.netology.nmedia.activity.ErrorHandlingActivity
import ru.netology.nmedia.activity.FeedFragment
import ru.netology.nmedia.activity.NewPostFragment
import ru.netology.nmedia.adapter.PostViewHolder

import ru.netology.nmedia.api.PostApi
import ru.netology.nmedia.databinding.CardPostBinding
import ru.netology.nmedia.databinding.FragmentNewPostBinding
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel


class PostRepositoryImpl : PostRepository {


    override fun getAll(callback: PostRepository.Callback<List<Post>>) {

        PostApi.service.getALL()
            .enqueue(object : Callback<List<Post>> {
                override fun onResponse(
                    call: Call<List<Post>>,
                    response: Response<List<Post>>
                ) {
                    if (!response.isSuccessful) {
                        when (response.code()) {
                            404 -> Toast.makeText(,"not found code 404", Toast.LENGTH_SHORT).show()
                            500 -> Toast.makeText(,"server broken code 500", Toast.LENGTH_SHORT).show()
                            else-> Toast.makeText(,"unknown error", Toast.LENGTH_SHORT).show()

                        }

                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val posts = response.body()
                    if (posts == null) {
                        callback.onError(RuntimeException("Body is null"))
                        return
                    }
                    callback.onSuccess(posts)
                }

                override fun onFailure(call: Call<List<Post>>, t: Throwable) {
                    callback.onError(Exception(t))
                }


            })

    }


    override fun likeById(post: Post, callback: PostRepository.Callback<Post>) {
        val likedByMeValue = post.likedByMe
        PostApi.service.let {
            if (likedByMeValue)
                it.dislikeById(post.id)
            else
                it.likeById(post.id)
        }

            .enqueue(object : Callback<Post> {


                override fun onResponse(
                    call: Call<Post>,
                    response: Response<Post>
                ) {


                    if (!response.isSuccessful) {
                        when (response.code()) {
                            404 -> Toast.makeText(,"not found code 404", Toast.LENGTH_SHORT).show()
                            500 -> Toast.makeText(,"server broken code 500", Toast.LENGTH_SHORT).show()
                            else-> Toast.makeText(,"unknown error", Toast.LENGTH_SHORT).show()

                        }
                        callback.onError(RuntimeException(response.message()))
                        return
                    }
                    val data = response.body()
                    if (data == null) {
                        callback.onError(RuntimeException("Body is null"))
                        return
                    }

                    callback.onSuccess(data)


                }

                override fun onFailure(call: Call<Post>, t: Throwable) {
                    callback.onError(Exception(t))
                }


            })


    }


// TODO: do this in homework

    override fun save(post: Post, callback: PostRepository.Callback<Unit>) {
        PostApi.service.savePost(post).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (!response.isSuccessful) {
                    when (response.code()) {
                        404 -> Toast.makeText(,"not found code 404", Toast.LENGTH_SHORT).show()
                        500 -> Toast.makeText(,"server broken code 500", Toast.LENGTH_SHORT).show()
                        else-> Toast.makeText(,"unknown error", Toast.LENGTH_SHORT).show()

                    }
                    callback.onError(Exception(response.message()))
                    return
                }
                val data = response.body()
                if (data == null) {
                    callback.onError(RuntimeException("Body is null"))
                    return
                }

                callback.onSuccess(data)
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                callback.onError(Exception(t))
            }

        })

    }


    override fun removeById(id: Long, callback: PostRepository.Callback<Unit>) {
        PostApi.service.deletePostById(id)
            .enqueue(object : Callback<Unit> {
                override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                    if (!response.isSuccessful) {
                        when (response.code()) {
                            404 -> Toast.makeText(,"not found code 404", Toast.LENGTH_SHORT).show()
                            500 -> Toast.makeText(,"server broken code 500", Toast.LENGTH_SHORT).show()
                            else-> Toast.makeText(,"unknown error", Toast.LENGTH_SHORT).show()

                        }
                        callback.onError(Exception(response.message()))
                        return
                    }
                    val data = response.body()
                    if (data == null) {
                        callback.onError(RuntimeException("Body is null"))
                        return
                    }

                    callback.onSuccess(data)
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    callback.onError(Exception(t))
                }

            })
    }

}



