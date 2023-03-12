package com.denisyordanp.mytextscanner.repository

import com.denisyordanp.mytextscanner.utils.UploadStatus
import kotlinx.coroutines.flow.Flow

interface RemoteFirebaseRepository {
    fun uploadText(text: String): Flow<UploadStatus>
}
