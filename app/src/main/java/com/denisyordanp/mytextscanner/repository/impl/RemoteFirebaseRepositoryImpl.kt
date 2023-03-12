package com.denisyordanp.mytextscanner.repository.impl

import com.denisyordanp.mytextscanner.repository.RemoteFirebaseRepository
import com.denisyordanp.mytextscanner.utils.COLLECTION_NAME
import com.denisyordanp.mytextscanner.utils.TEXT_FIELD_NAME
import com.denisyordanp.mytextscanner.utils.UploadStatus
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow

class RemoteFirebaseRepositoryImpl(
    private val fireStore: FirebaseFirestore
) : RemoteFirebaseRepository {
    override fun uploadText(text: String): Flow<UploadStatus> {
        val textData = hashMapOf(
            TEXT_FIELD_NAME to text
        )
        return callbackFlow {
            fireStore.collection(COLLECTION_NAME)
                .add(textData)
                .addOnSuccessListener {
                    trySend(UploadStatus.Success)
                }
                .addOnFailureListener {
                    trySend(UploadStatus.Error.Upload(it))
                }
        }
    }
}
