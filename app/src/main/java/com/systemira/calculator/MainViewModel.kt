package com.systemira.calculator

import androidx.core.text.isDigitsOnly
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.util.Stack

class MainViewModel: ViewModel(), CalculatorInteractionListener {
    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    private val calculationStack: Stack<String> = Stack()
    private var hasFloatingPoint = false

    override fun onClearAllClick() {
        calculationStack.clear()
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
        return calculationStack.peek().isSignedDigitsOnly()
    }

    private fun String.isSignedDigitsOnly(): Boolean {
        if (this.first() == '-') {
            return this.drop(1).isDigitsOnly()
        }
        return this.isDigitsOnly()
    }

    private fun handleBackspaceNumber() {
        val number = calculationStack.pop()
        if (number.dropLast(1).isNotEmpty()) {
            calculationStack.push(number.dropLast(1))
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
        } else if (calculationStack.isEmpty()){
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
        return calculationStack.peek().isDigitsOnly() && calculationStack.size > 1
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
        if (isLastEntryNumber()) {
            calculationStack.push(operation.asString())
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
//        TODO("Not yet implemented")
        updateLastLine()
        updateCurrentLine()
        calculationStack.clear()
    }

    private fun handleOperation(operation: Operation) {
        when (operation) {
            Operation.REMINDER -> {
                handleReminder()
            }

            Operation.DIVISION -> {
                handleDivision()
            }

            Operation.MULTIPLICATION -> {
                handleMultiplication()
            }

            Operation.ADDITION -> {
                handleAddition()
            }

            Operation.SUBTRACTION -> {
                handleSubtraction()
            }
        }
    }

    private fun handleReminder() {
//         TODO("Not yet implemented")
    }

    private fun handleDivision() {
//         TODO("Not yet implemented")
    }

    private fun handleMultiplication() {
//         TODO("Not yet implemented")
    }

    private fun handleAddition() {
//         TODO("Not yet implemented")
    }

    private fun handleSubtraction() {
//         TODO("Not yet implemented")
    }

    private fun updateCurrentLine() {
        // TODO
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
}