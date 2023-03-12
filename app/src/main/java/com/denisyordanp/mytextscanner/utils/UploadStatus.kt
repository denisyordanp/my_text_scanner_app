package com.denisyordanp.mytextscanner.utils

sealed class UploadStatus {
    object Idle : UploadStatus()
    object Loading : UploadStatus()
    object Success : UploadStatus()
    sealed class Error : UploadStatus() {
        data class Upload(val error: Exception) : Error()
        object Image : Error()
    }
}
