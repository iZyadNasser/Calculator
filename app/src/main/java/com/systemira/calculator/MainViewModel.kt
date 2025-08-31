package com.systemira.calculator

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class MainViewModel: ViewModel(), CalculatorInteractionListener {
    private val _state = MutableStateFlow(CalculatorState())
    val state = _state.asStateFlow()

    override fun onNumberClick(number: String) {
        TODO("Not yet implemented")
    }

    override fun onOperationClick(operation: Operation) {
        TODO("Not yet implemented")
    }

    override fun onFloatPointClick() {
        TODO("Not yet implemented")
    }


}