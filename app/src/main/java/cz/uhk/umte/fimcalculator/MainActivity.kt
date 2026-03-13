package cz.uhk.umte.fimcalculator

import android.content.res.Configuration
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
    val displayText by viewModel.displayText.collectAsState()
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    if (isLandscape) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CalculatorDisplay(
                value = displayText,
                modifier = Modifier.weight(1f).fillMaxHeight()
            )
            CalculatorKeyboard(
                onKeyClick = { viewModel.onKeyClick(it) },
                modifier = Modifier.weight(1.5f).fillMaxHeight()
            )
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(8.dp)
        ) {
            CalculatorDisplay(
                value = displayText,
                modifier = Modifier.fillMaxWidth()
            )
            CalculatorKeyboard(
                onKeyClick = { viewModel.onKeyClick(it) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CalculatorDisplay(value: String, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    Surface(
        modifier = modifier.padding(vertical = if (isLandscape) 0.dp else 12.dp),
        color = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    ) {
        Box(
            modifier = if (isLandscape) Modifier.fillMaxSize() else Modifier.fillMaxWidth(),
            contentAlignment = if (isLandscape) Alignment.Center else Alignment.CenterEnd
        ) {
            Text(
                text = value,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = if (isLandscape) 0.dp else 16.dp),
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = if (isLandscape) 40.sp else 32.sp
                ),
                textAlign = if (isLandscape) TextAlign.Center else TextAlign.End,
                maxLines = 1
            )
        }
    }
}

@Composable
fun CalculatorKeyboard(onKeyClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE
    val fontSize = if (isLandscape) 24.sp else 36.sp

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
                modifier = Modifier.fillMaxWidth().weight(1f)
            ) {
                row.forEach { key ->
                    Button(
                        onClick = { onKeyClick(key) },
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .padding(2.dp)
                    ) {
                        Text(
                            text = key,
                            style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize)
                        )
                    }
                }
            }
        }
        Button(
            onClick = { onKeyClick("=") },
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .padding(2.dp)
        ) {
            Text(text = "=", style = MaterialTheme.typography.titleLarge.copy(fontSize = fontSize))
        }
    }
}

@Preview(showBackground = true, name = "Calculator Portrait")
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
