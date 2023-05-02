package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun CreatNewTree(onBack: () -> Unit, onClick: (Controller.DrawTree) -> Unit) {
    var name by remember { mutableStateOf("") }
    val inputError = remember { mutableStateOf(false) }
    val error: MutableState<String?> = remember { mutableStateOf(null) }

    fun isNameValid() {
        try {
            Controller.validateName(name)
        } catch (ex: Exception) {
            inputError.value = true
            error.value = ex.message
            return
        }
        inputError.value = false
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
                    onValueChange = { isNameValid(); name = it },
                    label = { Text(text = "name") },
                    isError = inputError.value,
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

                Button(
                    onClick = {
                        val tree = Controller.DrawTree(name, Controller.TreeType.BSTree)
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
                        text = "BSTree",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val tree = Controller.DrawTree(name, Controller.TreeType.AVLTree)
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
                        text = "AVLTree",
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))

                Button(
                    onClick = {
                        val tree = Controller.DrawTree(name, Controller.TreeType.RBTree)
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
                        text = "RBTree",
                    )
                }
            }
        }
    }
}
