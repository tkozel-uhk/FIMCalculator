# Workshop: Tvorba kalkulačky v Jetpack Compose

V tomto workshopu se naučíte základy moderního vývoje Android aplikací. Postupně projdeme od jednoduchého návrhu uživatelského rozhraní až po správnou architekturu s využitím `ViewModel`.

## 1. První kroky: Displej kalkulačky
Každá kalkulačka potřebuje místo, kde se zobrazují čísla. V Jetpack Compose vytváříme UI pomocí funkcí označených `@Composable`.

### Komponenta Surface a Text
Pro displej použijeme `Surface` (kontejner, který dává pozadí a tvar) a uvnitř `Text`.

```kotlin
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
                .padding(16.dp),
            style = MaterialTheme.typography.displayLarge,
            textAlign = TextAlign.End,
            maxLines = 1
        )
    }
}
```

---

## 2. Preview: Vidíme, co tvoříme
Anotace `@Preview` umožňuje vidět UI v Android Studiu bez nutnosti spouštět aplikaci.

```kotlin
@Preview(showBackground = true)
@Composable
fun CalculatorPreview() {
    FIMCalculatorTheme {
        CalculatorDisplay(value = "42")
    }
}
```

---

## 3. UI rozvržení: Klávesnice (Layouty)
Pro naskládání tlačítek použijeme:
*   **Column**: Vertikální směr.
*   **Row**: Horizontální směr.

```kotlin
@Composable
fun CalculatorKeyboard(onKeyClick: (String) -> Unit, modifier: Modifier = Modifier) {
    val keys = listOf(
        listOf("7", "8", "9", "/"),
        listOf("4", "5", "6", "*"),
        listOf("1", "2", "3", "-"),
        listOf("0", ".", "C", "+")
    )

    Column(modifier = modifier) {
        keys.forEach { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                row.forEach { key ->
                    Button(
                        onClick = { onKeyClick(key) },
                        modifier = Modifier.weight(1f).padding(4.dp)
                    ) {
                        Text(text = key)
                    }
                }
            }
        }
        // Tlačítko pro výsledek přes celou šířku
        Button(
            onClick = { onKeyClick("=") },
            modifier = Modifier.fillMaxWidth().padding(4.dp)
        ) {
            Text(text = "=")
        }
    }
}
```

---

## 4. Sestavení: Hlavní komponenta Calculator
Nyní propojíme displej a klávesnici do jednoho celku. Tato komponenta bude "vlastníkem" rozvržení.

```kotlin
@Composable
fun Calculator(
    modifier: Modifier = Modifier,
    viewModel: CalculatorViewModel = viewModel()
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(8.dp)
    ) {
        // Displej nahoře
        CalculatorDisplay(
            value = viewModel.displayText
        )
        // Klávesnice vyplní zbytek místa
        CalculatorKeyboard(
            onKeyClick = { viewModel.onKeyClick(it) },
            modifier = Modifier.fillMaxSize()
        )
    }
}
```

---

## 5. Reaktivita a stav (State)
Android aplikace reagují na změny stavu. Pokud se změní proměnná se stavem, UI se automaticky překreslí (tzv. **Recompozice**).

*   `mutableStateOf("0")`: Vytvoří sledovatelný stav.
*   `remember { ... }`: Zapamatuje si stav v rámci komponenty.

**Úkol:** Zkuste si, co se stane s textem na displeji, když otočíte telefonem (pokud je stav jen v `remember`).

---

## 6. Architektura: ViewModel
Aby naše data přežila i otočení telefonu, oddělíme logiku do třídy `ViewModel`.

```kotlin
class CalculatorViewModel : ViewModel() {
    var displayText by mutableStateOf("0")
        private set

    fun onKeyClick(key: String) {
        // Logika výpočtů (sčítání, odčítání, ...)
    }
}
```

---

## 7. Design a Material 3
Finální krok: Použití **Scaffold** pro standardní Android vzhled s horní lištou.

```kotlin
Scaffold(
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
```

---

## Shrnutí workshopu
Na konci máte aplikaci, která je:
1.  **Strukturovaná** (rozdělená na malé znovupoužitelné části).
2.  **Robustní** (přežije otočení displeje díky ViewModelu).
3.  **Moderní** (využívá Material 3 a Jetpack Compose).
