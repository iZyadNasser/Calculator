package com.systemira.calculator

interface CalculatorInteractionListener {
    fun onNumberClick(number: String)
    fun onOperationClick(operation: Operation)
    fun onFloatPointClick()
}