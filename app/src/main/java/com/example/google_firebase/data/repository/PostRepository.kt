package com.example.google_firebase.data.repository

import android.net.Uri
import android.util.Log
import com.example.google_firebase.data.Response
import com.example.google_firebase.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.getValue
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID


class PostRepository {
    private val storage = FirebaseStorage.getInstance("gs://fir-auth-47d52.firebasestorage.app").getReference("heroes")
    private val db = FirebaseDatabase.getInstance().getReference("heroes")


    private val auth = FirebaseAuth.getInstance()

    fun getPosts(): Flow<Response<List<Post>>> = callbackFlow {
        trySend(Response.Loading)
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val postsList = mutableListOf<Post>()
                for (data in dataSnapshot.children) {
                    data.getValue<Post>()?.let { postsList.add(it) }
                }
                postsList.sortByDescending { it.timestamp }
                trySend(Response.Success(postsList))
            }

            override fun onCancelled(databaseError: DatabaseError) {
                trySend(Response.Error(databaseError.message))
            }
        }

        db.addValueEventListener(postListener)

        awaitClose { db.removeEventListener(postListener) }

    }.catch { exception ->
        emit(Response.Error(exception.message ?: "Error fetching posts"))
    }.flowOn(Dispatchers.IO)


    fun addPost(
        name: String,
        description: String,
        imageUri: Uri
    ): Flow<Response<Unit>> = flow {
        emit(Response.Loading)

        Log.d("PostRepoNama",name)
        Log.d("PostRepoDesc",description)
        Log.d("PostRepoImage", imageUri.toString())
        val imageRef = storage.child("${UUID.randomUUID()}.jpg")
        val uploadTask = imageRef.putFile(imageUri).await()
        val downloadUrl = imageRef.downloadUrl.await()

        val postId = db.push().key ?: throw Exception("Post ID is null")
        val user = auth.currentUser
        val postData = Post(
            id = postId,
            name = name,
            description = description,
            imageUrl = downloadUrl.toString(),
            userName = user?.displayName ?: "",
            userProfileImageUrl = user?.photoUrl.toString(),
            timestamp = System.currentTimeMillis()
        )
        db.child(postId).setValue(postData).await()
        emit(Response.Success(Unit))
    }.catch { e ->
        emit(Response.Error(e.message ?: "Failed to add hero"))

    }.flowOn(Dispatchers.IO)
}