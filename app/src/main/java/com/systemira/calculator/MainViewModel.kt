package com.systemira.calculator

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack

class MainViewModel : ViewModel(), CalculatorInteractionListener {
    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    private val calculationStack: Stack<String> = Stack()
    private var hasFloatingPoint = false

    override fun onClearAllClick() {
        calculationStack.clear()
        hasFloatingPoint = false
        _state.update {
            it.copy(
                lastLine = "",
            )
        }
        updateCurrentLine()
    }

    override fun onBackspaceClick() {
        if (calculationStack.isEmpty()) return

        if (isLastEntryNumber()) {
            handleBackspaceNumber()
        } else if (isLastEntryFloatingPoint()) {
            handleBackspaceFloatingPoint()
        } else {
            handleBackspaceOperation()
        }

        updateCurrentLine()
    }

    private fun isLastEntryNumber(): Boolean {
        if (calculationStack.isEmpty()) return false
        if (calculationStack.size == 1 && calculationStack.peek().isSignedDigitsOnly()) return true
        return calculationStack.peek().isDigitsOnly() || calculationStack.peek().isDecimalNumber()
    }

    private fun String.isSignedDigitsOnly(): Boolean {
        if (this.isEmpty()) return false
        if (this.first() == '-') {
            return this.drop(1).isDigitsOnly()
        }
        return this.isDigitsOnly()
    }

    private fun String.isDecimalNumber(): Boolean {
        if (this.isEmpty()) return false
        val withoutSign = if (this.startsWith("-")) this.drop(1) else this
        if (!withoutSign.contains('.')) return false
        val parts = withoutSign.split('.')
        return parts.size == 2 && parts[0].isDigitsOnly() && parts[1].isDigitsOnly()
    }

    private fun handleBackspaceNumber() {
        val number = calculationStack.pop()
        val newNumber = number.dropLast(1)
        if (newNumber.isNotEmpty() && newNumber != "-") {
            calculationStack.push(newNumber)
            hasFloatingPoint = newNumber.contains('.')
        } else {
            hasFloatingPoint = false
        }
    }

    private fun isLastEntryFloatingPoint(): Boolean {
        if (calculationStack.isEmpty()) return false
        return calculationStack.peek() == "."
    }

    private fun handleBackspaceFloatingPoint() {
        calculationStack.pop()
        hasFloatingPoint = false
    }

    private fun handleBackspaceOperation() {
        calculationStack.pop()
    }

    override fun onNumberClick(digit: String) {
        if (isLastEntryNumber()) {
            handleAddNewDigitToExistingNumber(digit)
        } else if (calculationStack.isEmpty()) {
            handleNewNumberInEmptyStack(digit)
        } else {
            handleNewNumber(digit)
        }
        updateCurrentLine()
    }

    private fun handleAddNewDigitToExistingNumber(digit: String) {
        if (digit != "0" && isLastEntryZero().not()) {
            addNewDigitToLastNumber(digit)
        } else if (isLastEntryZero()) {
            replaceLastEntryWithDigit(digit)
        } else {
            if (isLastEntryMultiDigit() || (isLastEntryNumber() && isLastEntryZero().not())) {
                addNewDigitToLastNumber(digit)
            }
        }
    }

    private fun isLastEntryMultiDigit(): Boolean {
        if (calculationStack.isEmpty()) return false
        val entry = calculationStack.peek()
        return (entry.isSignedDigitsOnly() || entry.isDecimalNumber()) && entry.length > 1
    }

    private fun isLastEntryZero(): Boolean {
        if (calculationStack.isEmpty()) return false
        return calculationStack.peek() == "0"
    }

    private fun addNewDigitToLastNumber(digit: String) {
        val number = calculationStack.pop()
        calculationStack.push(number + digit)
    }

    private fun replaceLastEntryWithDigit(digit: String) {
        if (calculationStack.isNotEmpty() && isLastEntryNumber()) {
            calculationStack.pop()
            calculationStack.push(digit)
        }
    }

    private fun handleNewNumberInEmptyStack(digit: String) {
        if (digit != "0") {
            calculationStack.push(digit)
        }
    }

    private fun handleNewNumber(digit: String) {
        calculationStack.push(digit)
    }

    override fun onOperationClick(operation: Operation) {
        if (calculationStack.isNotEmpty() && isLastEntryNumber()) {
            calculationStack.push(operation.asString())
            hasFloatingPoint = false
        }
        updateCurrentLine()
    }

    override fun onFloatPointClick() {
        if (hasFloatingPoint.not()) {
            calculationStack.push(".")
            hasFloatingPoint = true
        }
        updateCurrentLine()
    }

    override fun onNegationClick() {
        if (calculationStack.size == 1 && isLastEntryNumber()) {
            val number = calculationStack.pop()
            calculationStack.push(negateNumber(number))
        }
        updateCurrentLine()
    }

    private fun negateNumber(number: String): String {
        if (number.first() == '-') {
            return number.drop(1)
        }
        return "-$number"
    }

    override fun onEqualClick() {
        if (isValidStack()) {
            updateLastLine()
            val originalStack = Stack<String>()
            calculationStack.forEach { originalStack.push(it) }

            evaluateStack()
            val result = state.value.currentLine

            calculationStack.clear()
            hasFloatingPoint = false

            if (result != ERROR && result.isNotEmpty() && result != "0") {
                calculationStack.push(result)
                if (result.contains(".")) {
                    hasFloatingPoint = true
                }
                updateCurrentLine()
            }
        }
    }

    private fun isValidStack(): Boolean {
        if (calculationStack.isEmpty()) return true
        return calculationStack.peek().isOperation().not()
    }

    private fun evaluateStack() {
        _state.update {
            it.copy(
                currentLine = calculate()
            )
        }
    }

    private fun calculate(): String {
        if (calculationStack.isEmpty()) {
            return "0"
        }

        try {
            val expression = buildExpressionFromStack()
            return evaluateExpression(expression)
        } catch (_: Exception) {
            return ERROR
        }
    }

    private fun buildExpressionFromStack(): String {
        val stackItems = mutableListOf<String>()
        val tempStack = Stack<String>()

        while (calculationStack.isNotEmpty()) {
            tempStack.push(calculationStack.pop())
        }
        while (tempStack.isNotEmpty()) {
            stackItems.add(tempStack.pop())
        }

        var expression = ""
        var currentNumber = ""

        for (item in stackItems) {
            when {
                item.isOperation() -> {
                    if (currentNumber.isNotEmpty()) {
                        expression += currentNumber
                        currentNumber = ""
                    }
                    expression += " $item "
                }
                item == "." -> {
                    currentNumber += item
                }
                else -> {
                    currentNumber += item
                }
            }
        }

        if (currentNumber.isNotEmpty()) {
            expression += currentNumber
        }

        return expression.trim()
    }

    private fun evaluateExpression(expression: String): String {
        if (expression.isEmpty()) return "0"

        val parts = expression.split(" ").filter { it.isNotEmpty() }
        if (parts.size == 1) {
            return formatResult(parts[0].toFloatOrNull() ?: 0.0f)
        }

        val numbers = mutableListOf<Float>()
        val operations = mutableListOf<String>()

        for (i in parts.indices) {
            if (i % 2 == 0) {
                numbers.add(parts[i].toFloatOrNull() ?: return ERROR)
            } else {
                operations.add(parts[i])
            }
        }

        var i = 0
        while (i < operations.size) {
            val op = operations[i]
            if (op == "x" || op == "/" || op == "%") {
                val left = numbers[i]
                val right = numbers[i + 1]

                val result = when (op) {
                    "x" -> left * right
                    "/" -> {
                        if (right == 0.0f) return ERROR
                        left / right
                    }
                    "%" -> {
                        if (right == 0.0f) return ERROR
                        left % right
                    }
                    else -> return ERROR
                }

                numbers[i] = result
                numbers.removeAt(i + 1)
                operations.removeAt(i)
            } else {
                i++
            }
        }

        var result = numbers[0]
        for (i in operations.indices) {
            val nextNumber = numbers[i + 1]
            result = when (operations[i]) {
                "+" -> result + nextNumber
                "-" -> result - nextNumber
                else -> return ERROR
            }
        }

        return formatResult(result)
    }

    private fun String.isOperation(): Boolean {
        return this in Operation.entries.map { it.asString() }
    }

    private fun formatResult(result: Float): String {
        return if (result == result.toLong().toFloat()) {
            result.toLong().toString()
        } else {
            result.toString()
        }
    }

    private fun updateCurrentLine() {
        _state.update {
            it.copy(
                currentLine = calculationStack.joinToString("")
            )
        }
    }

    private fun updateLastLine() {
        _state.update {
            it.copy(
                lastLine = it.currentLine
            )
        }
    }

    companion object {
        private const val ERROR = "Error"
    }
}