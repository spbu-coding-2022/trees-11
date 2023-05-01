package app

import androidx.compose.runtime.*
import androidx.compose.ui.awt.ComposeWindow


sealed class Screen {
    object MainWindow: Screen()

    object CreatNewWindow: Screen()

    object OpenTree: Screen()

    data class TreeWindow(val tree: Controller.DrawTree): Screen()
}
@Composable
fun Main(window: ComposeWindow) {
    var screenState by remember { mutableStateOf<Screen>(Screen.MainWindow) }

    when (val screen = screenState) {
        is Screen.MainWindow ->
            MainWindow(
                onClickNew = { screenState = Screen.CreatNewWindow },
                onClickOpen = { screenState = Screen.OpenTree }
            )

        is Screen.CreatNewWindow ->
            CreatNewTree(
                onBack = {screenState = Screen.MainWindow},
                onClick = { screenState = Screen.TreeWindow(tree = it) }
            )

        is Screen.OpenTree ->
            OpenTree(
                onBack = {screenState = Screen.MainWindow},
                onClick = { screenState = Screen.TreeWindow(tree = it) }
            )

        is Screen.TreeWindow ->
            Tree(
                onBack = {screenState = Screen.MainWindow},
                tree = screen.tree
            )
    }
}
