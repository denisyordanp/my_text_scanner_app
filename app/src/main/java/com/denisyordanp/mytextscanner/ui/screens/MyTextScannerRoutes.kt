package com.denisyordanp.mytextscanner.ui.screens

object MyTextScannerRoutes {
    const val CONVERTED_TEXT = "converted_text"
    fun goToEditScreen(convertedText: String) = "edit_screen/$convertedText"

    const val MAIN_SCREEN = "main_screen"
    const val EDIT_SCREEN = "edit_screen/{$CONVERTED_TEXT}"
}
