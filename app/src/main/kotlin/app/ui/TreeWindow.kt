package app.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.AlertDialog
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import app.controller.Controller

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun Tree(onBack: () -> Unit, tree: Controller.DrawTree) {
    var textForUser by remember { mutableStateOf("") }
    val openDialog = remember { mutableStateOf(false) }

    val offSetX = remember { mutableStateOf(tree.viewCoordinates.first) }
    val offSetY = remember { mutableStateOf(tree.viewCoordinates.second) }

    tree.reInitAllDrawNodes()
    tree.viewCoordinates = Pair(offSetX.value, offSetY.value)

    Row(modifier = Modifier.fillMaxSize().background(Color.White).padding(6.dp)) {
        Column(
            modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(400.dp)
        ) {
            Insert(
                onClick = { key, value ->
                    if (!Controller.validKey(key)) {
                        textForUser = "Oops.. it's not Int, bro"
                    } else if (key != "") {
                        tree.drawInsert(key, value)
                        tree.reInitAllDrawNodes()
                        textForUser = "I insert node with key: $key and value: $value :)"
                    } else {
                        textForUser = "Give me key pls :("
                    }
                }
            )
            Remove(
                onClick = { key ->
                    if (key != "") {
                        tree.drawRemove(key)
                        tree.reInitAllDrawNodes()
                        textForUser = "I remove node :)"
                    } else {
                        textForUser = "Give me key pls :("
                    }
                }
            )
            Find(
                onClick = { key ->
                    val value = tree.drawFind(key)
                    if (value != null) {
                        textForUser = "Result: $value"
                    } else {
                        textForUser = "Ooops... I can't find node :("
                    }
                }
            )
            Spacer(modifier = Modifier.height(15.dp))
            Text(
                text = textForUser,
                modifier = Modifier.padding(start = 32.dp, top = 16.dp),
                style = MaterialTheme.typography.headlineSmall,
            )
            Spacer(modifier = Modifier.height(40.dp))
            Row(
                modifier = Modifier,
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.aligned(Alignment.End)
            ) {
                Button(
                    onClick = onBack,
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.3f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.onErrorContainer
                    )
                ) {
                    Text(
                        text = "Exit",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Button(
                    onClick = {
                        openDialog.value = true
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.3f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Text(
                        text = "Save",
                    )
                }
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = {
                    offSetX.value = 0f
                    offSetY.value = 0f
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.width(400.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("go to tree Root!")
            }
        }
        ViewTree().drawTree(tree, offSetX, offSetY)
    }
    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = { openDialog.value = false },
            title = {
                Text(text = "How do you want to save this tree?")
            },
            text = {
                Text("Select the database to save:")
            },
            buttons = {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.aligned(Alignment.CenterHorizontally)
                ) {
                    Button(
                        onClick = { openDialog.value = false },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Exit")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            tree.saveToDB(Controller.DatabaseType.Json)
                            openDialog.value = false
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Json")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            tree.saveToDB(Controller.DatabaseType.SQLite)
                            openDialog.value = false
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("SQLite")
                    }
                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = {
                            tree.saveToDB(Controller.DatabaseType.Neo4j)
                            openDialog.value = false
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.weight(0.3f).height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        )
                    ) {
                        Text("Neo4j")
                    }
                }
            }
        )
    }
}


@Composable
fun Insert(onClick: (key: String, value: String) -> Unit) {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Insert:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

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

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    onClick(textKey, textValue)
                    textKey = ""
                    textValue = ""
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.30f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("go!")
            }
        }
    }
}

@Composable
fun Remove(onClick: (key: String) -> Unit) {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Remove:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            var textKey by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textKey,
                onValueChange = { textKey = it },
                label = { Text(text = "key") },
                singleLine = true,
                modifier = Modifier.weight(0.70f),
                shape = MaterialTheme.shapes.extraLarge,
            )

            Spacer(modifier = Modifier.width(16.dp))


            Button(
                onClick = {
                    onClick(textKey)
                    textKey = ""
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.30f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("go!")
            }
        }
    }
}

@Composable
fun Find(onClick: (key: String) -> Unit) {
    Column(modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(300.dp)) {
        Text(text = "Find:", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(7.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            var textKey by remember { mutableStateOf("") }

            OutlinedTextField(
                value = textKey,
                onValueChange = { textKey = it },
                label = { Text(text = "key") },
                singleLine = true,
                modifier = Modifier.weight(0.70f),
                shape = MaterialTheme.shapes.extraLarge,
            )

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    onClick(textKey)
                    textKey = ""
                },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.weight(0.30f).height(57.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Text("go!")
            }
        }
    }
}
