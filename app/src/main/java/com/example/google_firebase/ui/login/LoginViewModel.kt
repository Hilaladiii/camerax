package com.example.google_firebase.ui.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.google_firebase.data.Response
import com.example.google_firebase.data.repository.AuthRepository
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(private val authRepository: AuthRepository) : ViewModel() {

    private val _authState: MutableStateFlow<Response<FirebaseUser?>> = MutableStateFlow(Response.Loading)
    val authState: StateFlow<Response<FirebaseUser?>> = _authState

    init {
        getCurrentUser()
    }

    private fun getCurrentUser() {
        val user = authRepository.currentUser
        _authState.value = if (user != null) Response.Success(user) else Response.Error("User not logged in")
    }

    fun signInWithGoogle() {
        viewModelScope.launch {
//            _authState.value = Response.Loading
//            _authState.value = authRepository.signInWithGoogleCredentials()
            authRepository.signInWithGoogleCredentials().collect { state ->
                _authState.value = state
            }
        }
    }

    companion object {
        fun Factory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val authRepository = AuthRepository(context)
                LoginViewModel(
                    authRepository = authRepository
                )
            }
        }
    }

}