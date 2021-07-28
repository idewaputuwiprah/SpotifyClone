package com.idputuwiprah.spotifyclone.data.remote

import com.google.firebase.firestore.FirebaseFirestore
import com.idputuwiprah.spotifyclone.data.entities.Song
import com.idputuwiprah.spotifyclone.other.Constants.SONGS_COLLECTION
import kotlinx.coroutines.tasks.await
import java.lang.Exception

class RemoteDatabase {
    private val firestore = FirebaseFirestore.getInstance()
    private val songCollection = firestore.collection(SONGS_COLLECTION)

    suspend fun getSongs(): List<Song> {
        return try {
            songCollection.get().await().toObjects(Song::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}