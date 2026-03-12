package cz.uhk.umte.fimcalculator

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * Třída pro správu stavu a logiky kalkulačky.
 * Finální fáze s implementací výpočtů.
 */
class CalculatorViewModel : ViewModel() {
    var displayText by mutableStateOf("0")
        private set

    private var operand1: Double? = null
    private var operator: String? = null
    private var isNewNumber = true

    fun onKeyClick(key: String) {
        when (key) {
            "C" -> reset()
            "+", "-", "*", "/" -> setOperator(key)
            "=" -> calculateResult()
            "." -> appendDot()
            else -> appendNumber(key)
        }
    }

    private fun reset() {
        displayText = "0"
        operand1 = null
        operator = null
        isNewNumber = true
    }

    private fun setOperator(newOperator: String) {
        operand1 = displayText.toDoubleOrNull()
        operator = newOperator
        isNewNumber = true
    }

    private fun calculateResult() {
        val op1 = operand1
        val op = operator
        val op2 = displayText.toDoubleOrNull()

        if (op1 != null && op != null && op2 != null) {
            val result = when (op) {
                "+" -> op1 + op2
                "-" -> op1 - op2
                "*" -> op1 * op2
                "/" -> if (op2 != 0.0) op1 / op2 else Double.NaN
                else -> op2
            }
            formatResult(result)
            operand1 = null
            operator = null
            isNewNumber = true
        }
    }

    private fun appendNumber(number: String) {
        if (isNewNumber) {
            displayText = number
            isNewNumber = false
        } else {
            if (displayText == "0") {
                displayText = number
            } else {
                displayText += number
            }
        }
    }

    private fun appendDot() {
        if (isNewNumber) {
            displayText = "0."
            isNewNumber = false
        } else if (!displayText.contains(".")) {
            displayText += "."
        }
    }

    private fun formatResult(result: Double) {
        displayText = when {
            result.isNaN() -> "Error"
            result % 1.0 == 0.0 -> result.toLong().toString()
            else -> result.toString()
        }
    }
}
