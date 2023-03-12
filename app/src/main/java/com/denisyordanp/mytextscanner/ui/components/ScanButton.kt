package com.denisyordanp.mytextscanner.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.denisyordanp.mytextscanner.ui.theme.MyTextScannerTheme

@Composable
fun ScanButton(modifier: Modifier = Modifier, onClicked: () -> Unit) {
    Button(
        modifier = modifier,
        onClick = onClicked
    ) {
        Text(
            modifier = Modifier
                .padding(16.dp),
            text = "Scan picture"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun Preview() {
    MyTextScannerTheme {
        ScanButton(
            onClicked = {}
        )
    }
}
