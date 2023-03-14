package com.denisyordanp.mytextscanner.utils

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    data class Success(val convertedText: String) : UploadStatus()
    sealed class Error : UploadStatus() {
        data class Upload(val error: Exception) : Error()
        data class Image(val error: Exception?) : Error()
    }
}
