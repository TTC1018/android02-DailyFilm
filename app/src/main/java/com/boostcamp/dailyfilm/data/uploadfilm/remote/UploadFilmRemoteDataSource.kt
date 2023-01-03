package com.boostcamp.dailyfilm.data.uploadfilm.remote

import android.net.Uri
import com.boostcamp.dailyfilm.BuildConfig
import com.boostcamp.dailyfilm.data.model.DailyFilmItem
import com.boostcamp.dailyfilm.data.model.Result
import com.boostcamp.dailyfilm.data.uploadfilm.UploadFilmDataSource
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow

class UploadFilmRemoteDataSource : UploadFilmDataSource {
    override fun uploadVideo(uploadDate: String, videoUri: Uri) = callbackFlow {
        val reference = storage.reference // File Pointer
        val videoRef =
            reference.child("user_videos/${videoUri.lastPathSegment}") // TODO 선택한 날짜값을 이름으로 재구성할 것
        val metadata = storageMetadata {
            contentType = "video/mp4"
        }

        videoRef.putFile(videoUri, metadata)
            .continueWithTask {
                videoRef.downloadUrl
            }
            .addOnSuccessListener { uri ->
                trySend(Result.Success(uri))
            }.addOnFailureListener { exception ->
                //  (exception as StorageException).errorCode
                trySend(Result.Error(exception))
            }
        awaitClose()
    }

    fun uploadFilmInfo(uploadDate: String, filmInfo: DailyFilmItem) =
        callbackFlow {
            userId?.let { id ->
                val reference = database.reference
                    .child(DIRECTORY_USER)
                    .child(id)
                    .child(uploadDate)

                reference.setValue(filmInfo)
                    .addOnSuccessListener {
                        trySend(Result.Success(Unit))
                    }
                    .addOnFailureListener { exception ->
                        trySend(Result.Error(exception))
                    }
            }

            awaitClose()
        }

    companion object {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val storage = Firebase.storage

        // BuildConfig.BUILD_TYPE
        val database = Firebase.database(BuildConfig.DATABASE_URL)
        const val DIRECTORY_USER = "users"
    }
}
