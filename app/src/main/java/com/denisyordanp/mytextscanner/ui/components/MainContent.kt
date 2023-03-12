package com.denisyordanp.mytextscanner.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.denisyordanp.mytextscanner.ui.theme.MyTextScannerTheme

@Composable
fun MainContent(modifier: Modifier = Modifier, message: String?, onClicked: () -> Unit) {
    Column(
            modifier = modifier,
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
                text = message ?: "",
                textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(8.dp))
        ScanButton(onClicked = onClicked)
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTextScannerTheme {
        MainContent(
                message = "",
                onClicked = {}
        )
    }
}
