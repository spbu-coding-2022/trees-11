package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.layout.*
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreatNewTree(onBack: () -> Unit, onClick: (Controller.DrawTree) -> Unit) {
    var name by remember { mutableStateOf("") }
    val error: MutableState<String?> = remember { mutableStateOf(null) }
    val treeType = remember { mutableStateOf(Controller.TreeType.BSTree) }
    val keysType = remember { mutableStateOf(Controller.KeysType.Int) }

    fun isNameValid() {
        try {
            Controller.validateName(name)
        } catch (ex: Exception) {
            error.value = ex.message
            return
        }
        error.value = null
    }

    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize().padding(start = 120.dp, end = 120.dp),
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                OutlinedTextField(
                    value = name,
                    onValueChange = {  name = it; isNameValid(); },
                    label = { Text(text = "name") },
                    isError = error.value != null,
                    singleLine = true,
                    modifier = Modifier.weight(0.70f),
                    shape = MaterialTheme.shapes.extraLarge,
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            if (error.value != null)
                Text(
                    text = error.value.toString()
                )


            Spacer(modifier = Modifier.height(15.dp))

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

                Box(modifier = Modifier.weight(0.3f)) {
                    val expanded = remember { mutableStateOf(false) }

                    Button(
                        onClick = {
                            expanded.value = true
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.fillMaxWidth().height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = treeType.value.toString(),
                        )
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(onClick = { treeType.value = Controller.TreeType.BSTree; expanded.value = false }) {
                            Text("Binary search tree")
                        }
                        Divider()
                        DropdownMenuItem(onClick = { treeType.value = Controller.TreeType.AVLTree; expanded.value = false }) {
                            Text("AVL tree")
                        }
                        Divider()
                        DropdownMenuItem(onClick = { treeType.value = Controller.TreeType.RBTree; expanded.value = false }) {
                            Text("Red-black tree")
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(modifier = Modifier.weight(0.3f)) {
                    val expanded = remember { mutableStateOf(false) }

                    Button(
                        enabled = false,
                        onClick = {
                            expanded.value = true
                        },
                        shape = MaterialTheme.shapes.extraLarge,
                        modifier = Modifier.fillMaxWidth().height(57.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = md_theme_light_primary
                        )
                    ) {
                        Text(
                            text = keysType.value.toString(),
                        )
                    }

                    DropdownMenu(
                        expanded = expanded.value,
                        onDismissRequest = { expanded.value = false }
                    ) {
                        DropdownMenuItem(onClick = { keysType.value = Controller.KeysType.Int; expanded.value = false }) {
                            Text("Int keys")
                        }
                        Divider()
                        DropdownMenuItem(onClick = { keysType.value = Controller.KeysType.Float; expanded.value = false }) {
                            Text("Float keys")
                        }
                        Divider()
                        DropdownMenuItem(onClick = { keysType.value = Controller.KeysType.String; expanded.value = false }) {
                            Text("String keys")
                        }
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    enabled = error.value == null && name.isNotEmpty(),
                    onClick = {
                        val tree = Controller.DrawTree(name, Controller.TreeType.RBTree, keysType.value)
                        name = ""
                        onClick(tree)
                    },
                    shape = MaterialTheme.shapes.extraLarge,
                    modifier = Modifier.weight(0.3f).height(57.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = md_theme_light_primary
                    )
                ) {
                    Text(
                        text = "Create",
                    )
                }
            }
        }
    }
}
