package com.systemira.calculator

enum class Operation {
    REMINDER,
    DIVISION,
    MULTIPLICATION,
    ADDITION,
    SUBTRACTION,
}

fun Operation.asString(): String {
    return when (this) {
        Operation.REMINDER -> "%"
        Operation.DIVISION -> "/"
        Operation.MULTIPLICATION -> "x"
        Operation.ADDITION -> "+"
        Operation.SUBTRACTION -> "-"
    }
}