package com.example.extrackify

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.extrackify.databinding.ActivityAddExpenseBinding
import com.example.extrackify.models.ExpenseCategory
import com.example.extrackify.models.PaymentMethod
import com.example.extrackify.view_model.AddExpenseViewModel
import com.example.extrackify.view_model.ExpenseUIState
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddExpenseActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityAddExpenseBinding
    private val addExpenseViewModel: AddExpenseViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityAddExpenseBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupUI()
        observeViewModel()
    }
    
    private fun setupUI() {
        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Setup category spinner
        val categoryAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            ExpenseCategory.values().map { it.displayName }
        )
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerCategory.adapter = categoryAdapter
        
        // Setup payment method spinner
        val paymentAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            PaymentMethod.values().map { it.displayName }
        )
        paymentAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.spinnerPaymentMethod.adapter = paymentAdapter
        
        // Setup date picker
        binding.editTextDate.setOnClickListener {
            showDatePicker()
        }
        
        // Set current date as default
        val currentDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        binding.editTextDate.setText(currentDate)
        
        // Setup save button
        binding.buttonSave.setOnClickListener {
            saveExpense()
        }
    }
    
    private fun showDatePicker() {
        val calendar = Calendar.getInstance()
        val datePickerDialog = DatePickerDialog(
            this,
            { _, year, month, dayOfMonth ->
                calendar.set(year, month, dayOfMonth)
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                binding.editTextDate.setText(dateFormat.format(calendar.time))
            },
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        datePickerDialog.show()
    }
    
    private fun saveExpense() {
        val amount = binding.editTextAmount.text.toString().toDoubleOrNull()
        val category = ExpenseCategory.values()[binding.spinnerCategory.selectedItemPosition].name
        val description = binding.editTextDescription.text.toString()
        val paymentMethod = PaymentMethod.values()[binding.spinnerPaymentMethod.selectedItemPosition].name
        val dateString = binding.editTextDate.text.toString()
        
        if (amount == null || amount <= 0) {
            Toast.makeText(this, "Please enter a valid amount", Toast.LENGTH_SHORT).show()
            return
        }
        
        if (description.isBlank()) {
            Toast.makeText(this, "Please enter a description", Toast.LENGTH_SHORT).show()
            return
        }
        
        addExpenseViewModel.addExpense(
            amount = amount,
            category = category,
            description = description,
            paymentMethod = paymentMethod,
            dateString = dateString
        )
    }
    
    private fun observeViewModel() {
        addExpenseViewModel.uiState.observe(this) { state ->
            when (state) {
                is ExpenseUIState.Loading -> {
                    binding.buttonSave.isEnabled = false
                    binding.buttonSave.text = "Saving..."
                }
                is ExpenseUIState.Success -> {
                    Toast.makeText(this, "Expense added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                }
                is ExpenseUIState.Error -> {
                    binding.buttonSave.isEnabled = true
                    binding.buttonSave.text = "Save Expense"
                    Toast.makeText(this, state.message, Toast.LENGTH_LONG).show()
                }
                is ExpenseUIState.Idle -> {
                    binding.buttonSave.isEnabled = true
                    binding.buttonSave.text = "Save Expense"
                }
            }
        }
    }
}