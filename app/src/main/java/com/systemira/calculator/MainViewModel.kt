package com.systemira.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MainViewModel: ViewModel(), CalculatorInteractionListener {
    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    private var result: Double = 0.0
    private var currentNumberStr: String = ""
    private var currentNumber: Double = 0.0
    private var lastOperation: Operation? = null

    override fun onClearAllClick() {
        currentNumberStr = ""
        currentNumber = 0.0
        result = 0.0
        lastOperation = null
        _state.update {
            it.copy(
                lastLine = "",
                currentLine = "",
                resultStr = ""
            )
        }
    }

    override fun onBackspaceClick() {
        if (isLastEntryNumber()) {
            handleBackspaceNumber()
        } else {
            handleBackspaceOperation()
        }
    }

    private fun isLastEntryNumber(): Boolean {
        return currentNumberStr.isNotEmpty()
    }

    private fun handleBackspaceNumber() {
        currentNumberStr = currentNumberStr.dropLast(1)
        currentNumber = currentNumberStr.toDoubleOrNull() ?: 0.0
        _state.update {
            it.copy(
                currentLine = it.currentLine.dropLast(1)
            )
        }
    }

    private fun handleBackspaceOperation() {
        lastOperation = null
        _state.update {
            it.copy(
                currentLine = it.currentLine.dropLast(3)
            )
        }
    }

    override fun onNumberClick(number: String) {
        if (number == "0") {
            handleZeroDigit()
        } else {
            handleDigit(number)
        }
    }

    private fun handleZeroDigit() {
        if (currentNumberStr.isNotEmpty()) {
            handleDigit("0")
        }
    }

    private fun handleDigit(number: String) {
        currentNumberStr += number
        currentNumber = currentNumberStr.toDoubleOrNull() ?: 0.0
        _state.update {
            it.copy(
                currentLine = it.currentLine + currentNumberStr,
            )
        }
    }

    override fun onOperationClick(operation: Operation) {
        if (currentNumberStr.isNotEmpty() && isLastEntryNumber()) {
            calculatePreviousResult()
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
    }

    private fun calculatePreviousResult() {
//        TODO("Not yet implemented")
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

    override fun onFloatPointClick() {
//        TODO("Not yet implemented")
    }

    override fun onNegationClick() {
//        TODO("Not yet implemented")
    }

    override fun onEqualClick() {
//        TODO("Not yet implemented")
    }


}