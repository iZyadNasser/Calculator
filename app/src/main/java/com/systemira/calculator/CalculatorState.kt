package com.systemira.calculator

data class CalculatorState(
    val lastOperation: String = "",
    val currentOperation: String = "",
    val result: String = "",
)
