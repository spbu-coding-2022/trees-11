package UIT

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 800.dp, height = 600.dp)
    ) {
        MaterialTheme {
            val modifierColumn = Modifier.fillMaxSize().background(Color.White).padding(6.dp)
            Row(modifierColumn) {
                Column(
                    modifier = Modifier.padding(start = 32.dp, top = 16.dp)
                ) {
                    Insert()
                    Remove()
                    Find()
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {

                }
            }
            }
        }
    }

@Preview
@Composable
fun Insert(/*keyValue: (keyString: String, value: String) -> Unit*/) {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Insert:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

        Row {
            var textKey by remember { mutableStateOf("") }
            var textValue by remember { mutableStateOf("") }


            OutlinedTextField( value = textKey,
                onValueChange = { textKey = it },
                label = { Text(text = "key")},
                singleLine = true,
                modifier = Modifier.weight(0.30f),
                )

            Spacer(modifier = Modifier.width(16.dp))

            OutlinedTextField( value = textValue,
                onValueChange = { textValue = it },
                label = { Text(text = "value")},
                singleLine = true,
                modifier = Modifier.weight(0.30f),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = { /*keyValue(textKey, textValue)*/
                textKey = ""
                textValue = "" },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.30f), ){
                Text("go!")
            }
        }
    }
}

@Composable
fun Remove() {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Remove:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

        Row {
            var textKey by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textKey,
                onValueChange = { textKey = it },
                label = { Text(text = "key") },
                singleLine = true,
                modifier = Modifier.weight(0.70f),
            )

            Spacer(modifier = Modifier.width(16.dp))


            Button(
                onClick = {textKey = "" },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.30f)
            ) {
                Text("go!")
            }
        }
    }
}

@Composable
fun Find() {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Find:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

        Row {
            var textKey by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textKey,
                onValueChange = { textKey = it },
                label = { Text(text = "key") },
                singleLine = true,
                modifier = Modifier.weight(0.70f),
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = { textKey = "" },
                shape = MaterialTheme.shapes.small,
                modifier = Modifier.weight(0.30f),
            ) {
                Text("go!")
            }
        }
    }
}
