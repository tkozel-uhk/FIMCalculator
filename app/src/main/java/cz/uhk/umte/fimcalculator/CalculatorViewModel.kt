package cz.uhk.umte.fimcalculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Třída pro správu stavu a logiky kalkulačky založená na StateFlow.
 */
class CalculatorViewModel : ViewModel() {
    
    // Interní mutovatelný stav
    private val _displayText = MutableStateFlow("0")
    
    // Veřejný neměnný proud stavu pro UI
    val displayText: StateFlow<String> = _displayText.asStateFlow()

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
        _displayText.value = "0"
        operand1 = null
        operator = null
        isNewNumber = true
    }

    private fun setOperator(newOperator: String) {
        operand1 = _displayText.value.toDoubleOrNull()
        operator = newOperator
        isNewNumber = true
    }

    private fun calculateResult() {
        val op1 = operand1
        val op = operator
        val op2 = _displayText.value.toDoubleOrNull()

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
            _displayText.value = number
            isNewNumber = false
        } else {
            if (_displayText.value == "0") {
                _displayText.value = number
            } else {
                _displayText.value += number
            }
        }
    }

    private fun appendDot() {
        if (isNewNumber) {
            _displayText.value = "0."
            isNewNumber = false
        } else if (!_displayText.value.contains(".")) {
            _displayText.value += "."
        }
    }

    private fun formatResult(result: Double) {
        _displayText.value = when {
            result.isNaN() -> "Error"
            result % 1.0 == 0.0 -> result.toLong().toString()
            else -> result.toString()
        }
    }
}
