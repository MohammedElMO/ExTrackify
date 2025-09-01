package com.example.extrackify.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.ExpenseModel
import com.example.extrackify.models.ExpenseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpenseListViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    
    private val _expenses = MutableLiveData<List<ExpenseModel>>()
    val expenses: LiveData<List<ExpenseModel>> = _expenses
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadExpenses() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                _expenses.value = expenseRepository.getExpenses()
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshExpenses() {
        loadExpenses()
    }
    
    fun deleteExpense(expenseId: String) {
        viewModelScope.launch {
            try {
                val success = expenseRepository.deleteExpense(expenseId)
                if (success) {
                    loadExpenses() // Refresh the list
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}