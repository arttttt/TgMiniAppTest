package com.arttttt.tgminiapptest

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.material3.*
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

external class ThemeParams {

    @JsName("bg_color")
    val bgColor: String?

    @JsName("text_color")
    val textColor: String?
}

external interface WebApp {
    val initData: String
    val version: String
    val platform: String
    val colorScheme: String

    val themeParams: ThemeParams
    val isExpanded: Boolean

    fun close()
    fun ready()
    fun expand()
}

external interface Telegram {
    @JsName("WebApp")
    val webApp: WebApp
}

@JsName("Telegram")
external val telegram: Telegram

@Composable
fun TgMiniAppContent() {
    val snackbarState = remember { SnackbarHostState() }
        val coroutinesScope = rememberCoroutineScope()

        MaterialTheme {
            Column(
                modifier = Modifier
                    .size(400.dp)
                    .let { modifier ->
                        if (telegram.webApp.themeParams.bgColor != null) {
                            modifier.background(Color(telegram.webApp.themeParams.bgColor!!.toColorInt()))
                        } else {
                            modifier
                        }
                    },
                verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Button(
                    onClick = {
                        coroutinesScope.launch {
                            val result = snackbarState.showSnackbar("Close MiniApp?", "Close")

                            if (result == SnackbarResult.ActionPerformed) {
                                telegram.webApp.close()
                            }
                        }
                    }
                ) {
                    Text("Close MiniApp")
                }

                if (!telegram.webApp.isExpanded) {
                    Button(
                        onClick = {
                            telegram.webApp.expand()
                        },
                    ) {
                        Text("expand")
                    }
                }

                val textColor = if (telegram.webApp.themeParams.textColor != null) {
                    Color(telegram.webApp.themeParams.textColor!!.toColorInt())
                } else {
                    Color.Unspecified
                }

                Text(
                    text = "platform: ${telegram.webApp.platform}",
                    color = textColor,
                )
                Text(
                    text = "colorScheme: ${telegram.webApp.colorScheme}",
                    color = textColor,
                )
                Text(
                    text = "isExpanded: ${telegram.webApp.isExpanded}",
                    color = textColor,
                )
                Text(
                    text = "bg color: ${telegram.webApp.themeParams.bgColor}",
                    color = textColor,
                )
                Text(
                    text = "text color: ${telegram.webApp.themeParams.textColor}",
                    color = textColor,
                )
            }

            SnackbarHost(snackbarState)

            LaunchedEffect(Unit) {
                telegram.webApp.ready()
            }
        }
}

fun String.toColorInt(): Int {
    if (this[0] == '#') {
        var color = substring(1).toLong(16)
        if (length == 7) {
            color = color or 0x00000000ff000000L
        } else if (length != 9) {
            throw IllegalArgumentException("Unknown color")
        }
        return color.toInt()
    }
    throw IllegalArgumentException("Unknown color")
}