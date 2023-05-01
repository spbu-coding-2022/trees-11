package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Tree(onBack: () -> Unit, tree: Controller.DrawTree) {
    var textForUser by remember { mutableStateOf("") }
    MaterialTheme {
            Row(modifier = Modifier.fillMaxSize().background(Color.White).padding(6.dp)) {
                Column(
                    modifier = Modifier.padding(start = 32.dp, top = 16.dp).width(400.dp)
                ) {
                    Insert(
                        onClick = { key, value ->
                            if ((key != "") && (value != "")) {
                                tree.drawInsert(key, value)
                                textForUser = "I insert node with key = $key and value = $value :)"
                            }
                            else {
                                textForUser = "Give me key and value pls :("
                            }
                        }
                    )
                    Remove(
                        onClick = { key ->
                            if (key != "") {
                                tree.drawRemove(key)
                                textForUser = "I remove node :)"
                            }
                            else {
                                textForUser = "Give me key pls :("
                            }
                        }
                    )
                    Find(
                        onClick = { key ->
                            val value = tree.drawFind(key)
                            if (value != null) {
                                textForUser = "Result: $value"
                            }
                            else {
                                textForUser = "I can't find node :("
                            }
                        }
                    )
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = textForUser,
                        modifier = Modifier.padding(start = 32.dp, top = 16.dp),
                        style = MaterialTheme.typography.headlineSmall,
                        )
                    Row(modifier = Modifier.height(570.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.aligned(Alignment.End)) {
                        Button(
                            onClick = onBack,
                            shape = MaterialTheme.shapes.extraLarge,
                            modifier = Modifier.weight(0.3f),
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
                            modifier = Modifier.weight(0.3f),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = md_theme_light_primary
                            )
                        ) {
                            Text(
                                text = "Save",
                            )
                        }
                    }
                }
                                Box (
                                modifier = Modifier
                                    .fillMaxSize().padding(start = 200.dp, end = 30.dp),
                    ) {
                                    ViewTree().drawTree(tree)
                    }
                }
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
                        containerColor = md_theme_light_primary
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
                        textKey = "" },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.30f).height(57.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primary
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
                        textKey = "" },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.30f).height(57.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primary
                    )
                ) {
                    Text("go!")
                }
            }
        }
    }
