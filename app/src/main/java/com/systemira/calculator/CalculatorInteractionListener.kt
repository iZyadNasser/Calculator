package com.systemira.calculator

interface CalculatorInteractionListener {
    fun onClearAllClick()
    fun onBackspaceClick()
    fun onNumberClick(number: String)
    fun onOperationClick(operation: Operation)
    fun onFloatPointClick()
    fun onNegationClick()
    fun onEqualClick()
}