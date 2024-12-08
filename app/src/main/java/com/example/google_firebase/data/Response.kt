package com.example.google_firebase.data

sealed class Response<out T: Any?> {

    object Loading : Response<Nothing>()

    object Idle : Response<Nothing>()

    data class Success<out T: Any?>(val data: T) : Response<T>()

    data class Error(val errorMessage: String) : Response<Nothing>()
}