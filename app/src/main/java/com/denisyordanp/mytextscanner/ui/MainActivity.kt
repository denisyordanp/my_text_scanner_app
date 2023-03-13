package com.denisyordanp.mytextscanner.ui

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.lifecycle.ViewModelProvider
import com.denisyordanp.mytextscanner.ui.components.MainContent
import com.denisyordanp.mytextscanner.ui.components.ScanButton
import com.denisyordanp.mytextscanner.ui.theme.MyTextScannerTheme
import com.denisyordanp.mytextscanner.utils.TIME_FORMAT
import com.denisyordanp.mytextscanner.utils.UploadStatus
import com.denisyordanp.mytextscanner.utils.ViewModelFactory
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    private val factory = ViewModelFactory()
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this, factory)[MainViewModel::class.java]
    }

    private var mImageUri: String = ""

    private val startForResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) { result: ActivityResult ->
        if (result.resultCode == Activity.RESULT_OK) {
            val imgFile = File(mImageUri)
            if (imgFile.exists()) {
                val myBitmap = BitmapFactory.decodeFile(imgFile.absolutePath)
                runTextRecognition(myBitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTextScannerTheme {
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
                                is UploadStatus.Error.Image -> MainContent(message = "No text found") {
                                    startCapturingImage()
                                }

                                is UploadStatus.Error.Upload -> MainContent(
                                    message = "Sorry there's something wrong, please try again: \n" +
                                            "${uploadState.error.message}"
                                ) {
                                    startCapturingImage()
                                }
                            }

                            is UploadStatus.Loading -> CircularProgressIndicator(
                                modifier = Modifier
                                    .size(30.dp)
                            )

                            is UploadStatus.Idle -> ScanButton {
                                startCapturingImage()
                            }

                            is UploadStatus.Success -> {
                                // Go to edit screen
                            }
                        }
                    }
                }
            }
        }
    }

    private fun startCapturingImage() {
        val file = createEmptyFile()
        val outputFileUri = createFileUri(file)
        val cameraIntent = createCameraIntent(outputFileUri)

        startForResult.launch(cameraIntent)
    }

    private fun createFileUri(file: File): Uri {
        return FileProvider.getUriForFile(
            this,
            this.applicationContext.packageName + ".provider",
            file
        )
    }

    private fun createCameraIntent(fileUri: Uri): Intent {
        return Intent(MediaStore.ACTION_IMAGE_CAPTURE).apply {
            putExtra(MediaStore.EXTRA_OUTPUT, fileUri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
    }

    private fun createEmptyFile(): File {
        val timeStamp: String =
            SimpleDateFormat(TIME_FORMAT, Locale.getDefault()).format(Date())
        val imageFileName = "$timeStamp.jpg"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        mImageUri = storageDir.absolutePath + "/" + imageFileName
        return File(mImageUri)
    }

    private fun runTextRecognition(capturedImage: Bitmap) {
        val image = InputImage.fromBitmap(capturedImage, 0)
        val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

        recognizer.process(image)
            .addOnSuccessListener { texts ->
                processTextRecognitionResult(texts)
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun processTextRecognitionResult(texts: Text) {
        val blocks = texts.textBlocks
        if (blocks.size == 0) {
            viewModel.errorImageProcessing()
            return
        }

        val convertedText = blocks.joinToString(separator = "\n") {
            it.text
        }
        viewModel.uploadText(convertedText)
    }
}
