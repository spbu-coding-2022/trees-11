package UIT

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            Column(
                modifier = Modifier.fillMaxSize().padding(start = 120.dp, end = 120.dp),
                verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    var name by remember { mutableStateOf("") }

                    OutlinedTextField(
                        value = name,
                        onValueChange = { name = it },
                        label = { Text(text = "name") },
                        singleLine = true,
                        modifier = Modifier.weight(0.70f),
                        shape = MaterialTheme.shapes.extraLarge,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))

                Text(text = "Add your first key and value!", style = MaterialTheme.typography.titleMedium)

                Row(verticalAlignment = Alignment.CenterVertically) {
                    var textKey by remember { mutableStateOf("") }
                    var textValue by remember { mutableStateOf("") }


                    OutlinedTextField(
                        value = textKey,
                        onValueChange = { textKey = it },
                        label = { Text(text = "key") },
                        singleLine = true,
                        modifier = Modifier.weight(0.30f),
                        shape = MaterialTheme.shapes.extraLarge,
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    OutlinedTextField(
                        value = textValue,
                        onValueChange = { textValue = it },
                        label = { Text(text = "value") },
                        singleLine = true,
                        modifier = Modifier.weight(0.30f),
                        shape = MaterialTheme.shapes.extraLarge,
                    )
                }
                Spacer(modifier = Modifier.height(15.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Button(
                        onClick = { },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = "Exit",
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = { },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = "Json",
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = "SQLite",
                        ) }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = "Neo4j",
                        )
                    }
                }

            }
        }
    }
}