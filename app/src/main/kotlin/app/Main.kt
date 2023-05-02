package app

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import UIT.AppTheme
import androidx.compose.ui.res.painterResource


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Trees",
        state = rememberWindowState(width = 900.dp, height = 700.dp),
        icon = painterResource("icon.png")
    ) {
        AppTheme {
            Main(window)
        }
    }
}