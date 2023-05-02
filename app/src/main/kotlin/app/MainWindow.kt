package app

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.system.exitProcess

@Composable
fun MainWindow(onClickNew: () -> Unit, onClickOpen: () -> Unit) {
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
                containerColor = MaterialTheme.colorScheme.primary
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
                containerColor = MaterialTheme.colorScheme.primary
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
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text(
                text = "Exit",
                style = MaterialTheme.typography.headlineSmall
            )
        }
    }

}
