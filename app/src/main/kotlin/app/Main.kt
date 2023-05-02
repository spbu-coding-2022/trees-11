package app

import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import app.theme.AppTheme
import app.ui.Main


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