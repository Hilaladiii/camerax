package com.example.google_firebase.ui.create

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.google_firebase.data.Response
import com.example.google_firebase.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CreateViewModel(private val postRepository: PostRepository) : ViewModel() {

    private val _addHeroState: MutableStateFlow<Response<Unit>> = MutableStateFlow(Response.Idle)
    val addHeroState: StateFlow<Response<Unit>>
        get() = _addHeroState

    fun addHero(name: String, description: String, imageUri: Uri) {
        viewModelScope.launch {
            postRepository.addPost(name, description, imageUri).collect { response ->
                _addHeroState.value = response
            }
        }
    }



    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val postRepository = PostRepository()
                CreateViewModel(
                    postRepository = postRepository
                )
            }
        }
    }
}