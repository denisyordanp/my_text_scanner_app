package com.denisyordanp.mytextscanner.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.denisyordanp.mytextscanner.R
import com.denisyordanp.mytextscanner.ui.MainViewModel
import com.denisyordanp.mytextscanner.ui.components.MainContent
import com.denisyordanp.mytextscanner.ui.components.ScanButton
import com.denisyordanp.mytextscanner.utils.UploadStatus

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    onScanClicked: () -> Unit,
    toEditScreen: (convertedText: String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            when (val uploadState = viewModel.uploadStatus.collectAsState().value) {
                is UploadStatus.Error -> when (uploadState) {
                    is UploadStatus.Error.Image -> {
                        val message = uploadState.error?.let {
                            stringResource(R.string.sorry_there_s_something_wrong_please_try_again, uploadState.error.message ?: "")
                        } ?: stringResource(R.string.no_text_found)

                        MainContent(
                            message = message,
                            onClicked = onScanClicked
                        )
                    }

                    is UploadStatus.Error.Upload -> MainContent(
                        message = stringResource(R.string.sorry_there_s_something_wrong_please_try_again, uploadState.error.message ?: ""),
                        onClicked = onScanClicked
                    )
                }

                is UploadStatus.Loading -> CircularProgressIndicator(
                    modifier = Modifier
                        .size(30.dp)
                )

                is UploadStatus.Idle -> ScanButton(
                    onClicked = onScanClicked
                )

                is UploadStatus.Success -> {
                    toEditScreen(uploadState.convertedText)
                    viewModel.resetToInitialState()
                }
            }
        }
    }
}
