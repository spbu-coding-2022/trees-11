package app

import UIT.md_theme_light_primary
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess

@Composable
fun MainWindow(onClickNew: () -> Unit, onClickOpen: () -> Unit) {
    MaterialTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.aligned(Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(
                onClick = onClickNew,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth(0.6f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_primary
                )
            ) {
                Text(
                    text = "New",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Spacer(modifier = Modifier.height(15.dp))
            Button(
                onClick = onClickOpen,
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth(0.6f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_primary
                )
            ) {
                Text(
                    text = "Open",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Spacer(modifier = Modifier.height(15.dp))

            Button(
                onClick = { exitProcess(1) },
                shape = MaterialTheme.shapes.extraLarge,
                modifier = Modifier.fillMaxWidth(0.6f).height(70.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = md_theme_light_primary
                )
            ) {
                Text(
                    text = "Exit",
                    style = MaterialTheme.typography.headlineSmall
                )
            }
        }

    }
}
