package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun OpenTree(onBack: () -> Unit, onClick: (Controller.DrawTree) -> Unit) {
    var files = listOf(Pair("", ""))
    var dataBaseType: Controller.DatabaseType = Controller.DatabaseType.Json
    MaterialTheme {
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
                        containerColor = md_theme_light_primary
                    )
                ) {
                    Text(
                        text = "Exit",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        files = Controller.Database(Controller.DatabaseType.Json).getAllTrees()
                        dataBaseType = Controller.DatabaseType.Json
                    },
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
                    onClick = {
                        files = Controller.Database(Controller.DatabaseType.SQLite).getAllTrees()
                        dataBaseType = Controller.DatabaseType.SQLite
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.3f).height(57.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primary
                    )
                ) {
                    Text(
                        text = "SQLite",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        files = Controller.Database(Controller.DatabaseType.Neo4j).getAllTrees()
                        dataBaseType = Controller.DatabaseType.Neo4j
                    },
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
            Spacer(modifier = Modifier.height(15.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                LazyColumn {
                    items(files) { file ->
                        Row {
                            Text(
                                text = "name: \"${file.first}\" type: ${file.second}"
                            )
                            Spacer(modifier = Modifier.width(15.dp))
                            Button(
                                onClick = {
                                    onClick(Controller.DrawTree(file.first, dataBaseType))
                                },
                                shape = MaterialTheme.shapes.extraLarge,
                                modifier = Modifier.weight(0.30f).height(57.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = md_theme_light_primary
                                )
                            ) {
                                Text("Open")
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Button(
                                onClick = {
                                    Controller.Database(dataBaseType).removeTree(file.first)
                                },
                                shape = MaterialTheme.shapes.extraLarge,
                                modifier = Modifier.weight(0.30f).height(57.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = md_theme_light_primary
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
}
