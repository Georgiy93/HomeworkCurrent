package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import okhttp3.Response
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.repository.*
import ru.netology.nmedia.util.SingleLiveEvent
import java.io.IOException
import kotlin.concurrent.thread

private val empty = Post(
    id = 0,
    content = "",
    author = "",
    likedByMe = false,
    likes = 0,
    published = "",
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val repository: PostRepository = PostRepositoryImpl()
    private val _data = MutableLiveData(FeedModel())
    val data: LiveData<FeedModel>
        get() = _data
    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _data.value = FeedModel(loading = true)
        repository.getAll(object : PostRepository.Callback<List<Post>> {
            override fun onSuccess(posts: List<Post>) {
                _data.postValue(FeedModel(posts = posts, empty = posts.isEmpty()))
            }

            override fun onError(e: Exception) {
                _data.postValue(FeedModel(error = true))
            }
        })


    }

    fun save() {

        edited.value?.let {

            repository.save(it, object : PostRepository.Callback<Unit> {
                override fun onSuccess(post:Unit) {
                    _postCreated.postValue(Unit)
                }
            })
        }



        edited.value = empty

    }

    fun edit(post: Post) {
        edited.value = post
    }

    fun changeContent(content: String) {
        val text = content.trim()
        if (edited.value?.content == text) {
            return
        }
        edited.value = edited.value?.copy(content = text)
    }


    fun likeById(post: Post) {


        repository.likeById(post,
            object : PostRepository.Callback<Post>{
                override fun onSuccess(post: Post) {

                    _data.postValue(
                        _data.value?.copy (posts = _data.value?.posts.orEmpty()
                            .map { if (it.id == post.id) post else it }))
                }
            })


    }


    fun removeById(id: Long) {
        repository.removeById(id, object : PostRepository.Callback<Unit> {
            val old = _data.value?.posts.orEmpty()
            override fun onSuccess(post:Unit) {

                _data.postValue(
                    _data.value?.copy(posts = _data.value?.posts.orEmpty()
                        .filter { it.id != id }
                    )
                )

            }
            override fun onError(e: Exception) {
                _data.postValue(_data.value?.copy(posts = old))
            }
        })

    }
    fun avatarLoad(post: Post) {
        repository.avatarLoad(post,
            object : PostRepository.Callback<Post>{
                override fun onSuccess(post: Post) {

                    _data.postValue(
                        _data.value?.copy (posts = _data.value?.posts.orEmpty()
                            .map { if (it.id == post.id) post else it }))
                }
            })
    }
}