package com.arttttt.tgminiapptest.timer

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arttttt.simplemvi.store.plus
import kotlinx.coroutines.Dispatchers

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimerContent() {

    val store = remember { TimerStore(Dispatchers.Main.immediate) }
    val state by store.states.collectAsState()

    DisposableEffect(store) {
        onDispose { store.destroy() }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        TopAppBar(
            title = {
                Text("Timer")
            },
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                Button(
                    enabled = !state.isTimerRunning,
                    onClick = {
                        store + TimerStore.Intent.StartTimer
                    }
                ) {
                    Text(
                        text = if (state.value != 0 && !state.isTimerRunning) {
                            "Resume timer"
                        } else {
                            "Start timer"
                        }
                    )
                }

                Button(
                    enabled = state.isTimerRunning,
                    onClick = {
                        store + TimerStore.Intent.StopTimer
                    }
                ) {
                    Text("Stop timer")
                }

                Button(
                    onClick = {
                        store + TimerStore.Intent.ResetTimer
                    }
                ) {
                    Text("Reset timer")
                }
            }

            Text(
                text = "timer: ${state.value}"
            )
        }
    }
}