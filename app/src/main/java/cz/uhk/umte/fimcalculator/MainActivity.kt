package cz.uhk.umte.fimcalculator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import cz.uhk.umte.fimcalculator.ui.theme.FIMCalculatorTheme


class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            FIMCalculatorTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        TopAppBar(
                            title = { Text("FIM Kalkulačka") },
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = MaterialTheme.colorScheme.primary,
                                titleContentColor = MaterialTheme.colorScheme.onPrimary,
                            )
                        )
                    }
                ) { innerPadding ->
                    Calculator(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = viewModel()
) {
    // Kolekce StateFlow do Compose stavu
    val displayText by viewModel.displayText.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        CalculatorDisplay(
            value = displayText
        )
        CalculatorKeyboard(
            onKeyClick = { viewModel.onKeyClick(it) },
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun CalculatorDisplay(value: String, modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier.padding(vertical = 12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Text(
            text = value,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, 0.dp),
            style = MaterialTheme.typography.displayLarge.copy(fontSize = 32.sp),
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}

@Composable
fun CalculatorKeyboard(onKeyClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val keys = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "C", "+")
    )

    Column (
        modifier = modifier,
        verticalArrangement = Arrangement.SpaceEvenly
        ) {
        keys.forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                row.forEach { key ->
                    Button(
                        onClick = { onKeyClick(key) },
                        modifier = Modifier
                            .weight(1f)
                            .padding(4.dp)
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp)
                        )
                    }
                }
            }
        }
        Button(
            onClick = { onKeyClick("=") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp)
        ) {
            Text(text = "=", style = MaterialTheme.typography.titleLarge.copy(fontSize = 36.sp))
        }
    }
}

@Preview(showBackground = true, name = "Calculator")
@Composable
fun CalculatorPreview() {
    FIMCalculatorTheme {
        Scaffold(
            topBar = {
                @OptIn(ExperimentalMaterial3Api::class)
                TopAppBar(
                    title = { Text("FIM Kalkulačka") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        titleContentColor = MaterialTheme.colorScheme.onPrimary,
                    )
                )
            }
        ) { innerPadding ->
            Calculator(modifier = Modifier.padding(innerPadding))
        }
    }
}
