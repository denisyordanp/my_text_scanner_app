package com.denisyordanp.mytextscanner.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.denisyordanp.mytextscanner.repository.RemoteFirebaseRepository
import com.denisyordanp.mytextscanner.utils.UploadStatus
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel(
    private val remote: RemoteFirebaseRepository
) : ViewModel() {

    private val _uploadStatus = MutableStateFlow<UploadStatus>(UploadStatus.Idle)
    val uploadStatus = _uploadStatus.asStateFlow()

    fun uploadText(text: String) {
        viewModelScope.launch {
            remote.uploadText(text)
                .collect {
                    _uploadStatus.value = it
                }
        }
    }

    fun errorImageProcessing() {
        viewModelScope.launch {
            _uploadStatus.value = UploadStatus.Error.Image
        }
    }
}
