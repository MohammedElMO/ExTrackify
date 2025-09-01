package com.example.extrackify.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.ExpenseModel
import com.example.extrackify.models.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

sealed class ExpenseUIState {
    object Idle : ExpenseUIState()
    object Loading : ExpenseUIState()
    data class Success(val message: String) : ExpenseUIState()
    data class Error(val message: String) : ExpenseUIState()
}

@HiltViewModel
class AddExpenseViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    
    private val _uiState = MutableLiveData<ExpenseUIState>(ExpenseUIState.Idle)
    val uiState: LiveData<ExpenseUIState> = _uiState
    
    fun addExpense(
        amount: Double,
        category: String,
        description: String,
        paymentMethod: String,
        dateString: String
    ) {
        _uiState.value = ExpenseUIState.Loading
        
        viewModelScope.launch {
            try {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                val date = dateFormat.parse(dateString) ?: Date()
                
                val expense = ExpenseModel(
                    amount = amount,
                    category = category,
                    description = description,
                    paymentMethod = paymentMethod,
                    date = date
                )
                
                expenseRepository.addExpense(expense)
                _uiState.value = ExpenseUIState.Success("Expense added successfully")
                
            } catch (e: Exception) {
                _uiState.value = ExpenseUIState.Error(e.message ?: "Failed to add expense")
            }
        }
    }
}