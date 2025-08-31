package com.systemira.calculator

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.systemira.calculator.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        observeStates()

        setupOnClickListeners()
    }

    private fun observeStates() {
        lifecycleScope.launch {
            viewModel.state.collect { state ->
                binding.textLastOperation.text = state.lastLine
                binding.textResult.text = state.resultStr
            }
        }
    }

    private fun setupOnClickListeners() {
        setupOperationOnClickListeners()
        setupNumberOnClickListeners()
        binding.buttonOperationAc.setOnClickListener {
            viewModel.onClearAllClick()
        }
        binding.buttonOperationBack.setOnClickListener {
            viewModel.onBackspaceClick()
        }
        binding.buttonOperationEqual.setOnClickListener {
            viewModel.onEqualClick()
        }
        binding.buttonPositiveNegative.setOnClickListener {
            viewModel.onNegationClick()
        }
    }

    private fun setupNumberOnClickListeners() {
        binding.buttonDigit0.setOnClickListener {
            viewModel.onNumberClick("0")
        }
        binding.buttonDigit1.setOnClickListener {
            viewModel.onNumberClick("1")
        }
        binding.buttonDigit2.setOnClickListener {
            viewModel.onNumberClick("2")
        }
        binding.buttonDigit3.setOnClickListener {
            viewModel.onNumberClick("3")
        }
        binding.buttonDigit4.setOnClickListener {
            viewModel.onNumberClick("4")
        }
        binding.buttonDigit5.setOnClickListener {
            viewModel.onNumberClick("5")
        }
        binding.buttonDigit6.setOnClickListener {
            viewModel.onNumberClick("6")
        }
        binding.buttonDigit7.setOnClickListener {
            viewModel.onNumberClick("7")
        }
        binding.buttonDigit8.setOnClickListener {
            viewModel.onNumberClick("8")
        }
        binding.buttonDigit9.setOnClickListener {
            viewModel.onNumberClick("9")
        }
        binding.buttonOperationDot.setOnClickListener {
            viewModel.onFloatPointClick()
        }
    }

    private fun setupOperationOnClickListeners() {
        binding.buttonOperationReminder.setOnClickListener {
            viewModel.onOperationClick(Operation.REMINDER)
        }
        binding.buttonOperationDivision.setOnClickListener {
            viewModel.onOperationClick(Operation.DIVISION)
        }
        binding.buttonOperationMultiplication.setOnClickListener {
            viewModel.onOperationClick(Operation.MULTIPLICATION)
        }
        binding.buttonOperationAddition.setOnClickListener {
            viewModel.onOperationClick(Operation.ADDITION)
        }
        binding.buttonOperationSubtraction.setOnClickListener {
            viewModel.onOperationClick(Operation.SUBTRACTION)
        }
    }
}