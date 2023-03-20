package ru.netology.nmedia.viewmodel

import android.app.Application
import androidx.lifecycle.*
import kotlinx.coroutines.launch
import retrofit2.HttpException
import ru.netology.nmedia.db.AppDb
import ru.netology.nmedia.dto.Post
import ru.netology.nmedia.model.FeedModel
import ru.netology.nmedia.model.FeedModelState
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
    authorAvatar = ""
)

class PostViewModel(application: Application) : AndroidViewModel(application) {
    // упрощённый вариант
    private val context = getApplication<Application>().applicationContext
    private val repository: PostRepository =
        PostRepositoryImpl(AppDb.getInstance(application).postDao())
    private val _state = MutableLiveData(FeedModelState())


    private val _messageError = SingleLiveEvent<String>()
    val messageError: LiveData<String>
        get() = _messageError
    val state: LiveData<FeedModelState>
        get() = _state
    val data: LiveData<FeedModel> =repository.data.map { FeedModel(posts = it, empty = it.isEmpty()) }


    val edited = MutableLiveData(empty)
    private val _postCreated = SingleLiveEvent<Unit>()
    val postCreated: LiveData<Unit>
        get() = _postCreated

    init {
        loadPosts()
    }

    fun loadPosts() {
        _state.value = FeedModelState(loading = true)


        viewModelScope.launch {
            try {
                repository.getAll()

                _state.value = FeedModelState()

            } catch (e: Exception) {

                _messageError.value = e.message
            }
        }
    }
    fun refresh() {
        _state.value = FeedModelState(refreshing = true)


        viewModelScope.launch {
            try {
                repository.getAll()

                _state.value = FeedModelState()

            } catch (e: Exception) {

                _messageError.value = e.message
            }
        }
    }

    fun save() {
        viewModelScope.launch {
            edited.value?.let {
                try {
                    repository.save(it)
                    _postCreated.postValue(Unit)
                } catch (e: Exception) {

                    _messageError.value = e.message
                }

                //TODO


            }
            edited.value = empty
        }


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

        viewModelScope.launch {
            try {
                 repository.likeById(post)

                _state.value = FeedModelState()


            } catch (e: Exception) {

                _messageError.value = e.message
            }
        }

    }
        fun removeById(id: Long) {
        viewModelScope.launch {
            try {
                repository.removeById(id)

            } catch (e: Exception) {

                _messageError.value = e.message
            }
        }
    }

}