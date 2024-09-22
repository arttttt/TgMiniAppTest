package com.arttttt.tgminiapptest

import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.window.ComposeViewport
import com.arttttt.tgminiapptest.bottomnavigation.BottomNavigationContent
import kotlinx.browser.document

@OptIn(ExperimentalComposeUiApi::class)
fun main() {
    ComposeViewport(document.body!!) {
        BottomNavigationContent()
    }
}
