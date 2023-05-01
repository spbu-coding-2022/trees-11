package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp


@Composable
fun OpenTree(onBack: () -> Unit, onClick: (Controller.DrawTree) -> Unit) {
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
                        val files = Controller().Database(Controller.DatabaseType.Json).getAllTrees()
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
                        val files = Controller().Database(Controller.DatabaseType.SQLite).getAllTrees()
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
                        val files = Controller().Database(Controller.DatabaseType.Neo4j).getAllTrees()
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
            Row(verticalAlignment = Alignment.CenterVertically) {
            }

        }
    }
}
