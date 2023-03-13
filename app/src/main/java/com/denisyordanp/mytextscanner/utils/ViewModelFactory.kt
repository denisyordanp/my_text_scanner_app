package com.denisyordanp.mytextscanner.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.denisyordanp.mytextscanner.repository.impl.RemoteFirebaseRepositoryImpl
import com.denisyordanp.mytextscanner.ui.MainViewModel
import com.google.firebase.firestore.FirebaseFirestore

class ViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val firestore = FirebaseFirestore.getInstance()
        val repository = RemoteFirebaseRepositoryImpl(firestore)
        return MainViewModel(
            remote = repository
        ) as T
    }
}
