package com.example.google_firebase.data.model

data class Post(
    val id: String = "",
    val name: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val userName: String = "",
    val userProfileImageUrl: String = "",
    val timestamp: Long = System.currentTimeMillis()
)