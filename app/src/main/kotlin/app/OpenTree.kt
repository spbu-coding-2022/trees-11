package app

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex


@Composable
fun OpenTree(onBack: () -> Unit, onClick: (Controller.DrawTree) -> Unit) {
    val files = remember { mutableStateOf(mutableStateListOf<Triple<String, String, Pair<Float, Float>>>()) }
    val dataBaseType = remember { mutableStateOf(Controller.DatabaseType.Json) }
    Column(
        modifier = Modifier.fillMaxSize().padding(start = 120.dp, end = 120.dp),
        verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Button(
                onClick = onBack,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.3f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Exit",
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = {
                    files.value =
                        Controller.Database(Controller.DatabaseType.Json).getAllTrees().toMutableStateList()
                    dataBaseType.value = Controller.DatabaseType.Json
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.3f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Json",
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    files.value =
                        Controller.Database(Controller.DatabaseType.SQLite).getAllTrees().toMutableStateList()
                    dataBaseType.value = Controller.DatabaseType.SQLite
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.3f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "SQLite",
                )
            }
            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    files.value =
                        Controller.Database(Controller.DatabaseType.Neo4j).getAllTrees().toMutableStateList()
                    dataBaseType.value = Controller.DatabaseType.Neo4j
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.3f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text(
                    text = "Neo4j",
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        if (files.value.isNotEmpty()) {

            Button(
                onClick = {
                    Controller.Database(dataBaseType.value).clean()
                    files.value = mutableStateListOf()
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.height(50.dp).fillMaxWidth(0.95f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onErrorContainer
                )
            ) {
                Text("delete all trees saved in ${dataBaseType.value}")
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

        LazyColumn {
            items(files.value) { file ->
                Spacer(modifier = Modifier.width(20.dp))
                Box(
                    modifier = Modifier.fillMaxWidth(0.95f)
                        .zIndex(0f)
                        .border(4.dp, MaterialTheme.colorScheme.background, RoundedCornerShape(20.dp))
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize()
                            .zIndex(1f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Button(
                            onClick = {
                                onClick(Controller.DrawTree(file.first, dataBaseType.value))
                            },
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = Modifier.weight(3f).width(30.dp).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.primary
                            )
                        ) {
                            Text("Open")
                        }

                        Text(
                            modifier = Modifier.weight(6f),
                            textAlign = TextAlign.Center,
                            text = "name: \"${file.first}\""
                        )
                        Text(
                            modifier = Modifier.weight(3f),
                            textAlign = TextAlign.Center,
                            text = file.second
                        )

                        Button(
                            onClick = {
                                Controller.Database(dataBaseType.value).removeTree(file.first)
                                files.value.remove(file)
                            },
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = Modifier.weight(3f).width(30.dp).fillMaxHeight(),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        ) {
                            Text("Delete")
                        }
                    }
                }
            }
        }
    }
}
