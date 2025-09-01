package com.example.extrackify.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.extrackify.models.ExpenseModel
import com.example.extrackify.models.ExpenseRepository
import com.example.extrackify.models.ExpenseSummary
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val expenseRepository: ExpenseRepository
) : ViewModel() {
    
    private val _recentExpenses = MutableLiveData<List<ExpenseModel>>()
    val recentExpenses: LiveData<List<ExpenseModel>> = _recentExpenses
    
    private val _dailySummary = MutableLiveData<ExpenseSummary>()
    val dailySummary: LiveData<ExpenseSummary> = _dailySummary
    
    private val _monthlySummary = MutableLiveData<ExpenseSummary>()
    val monthlySummary: LiveData<ExpenseSummary> = _monthlySummary
    
    private val _yearlySummary = MutableLiveData<ExpenseSummary>()
    val yearlySummary: LiveData<ExpenseSummary> = _yearlySummary
    
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    
    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error
    
    fun loadDashboardData() {
        _isLoading.value = true
        _error.value = null
        
        viewModelScope.launch {
            try {
                // Load recent expenses
                _recentExpenses.value = expenseRepository.getExpenses(limit = 10)
                
                // Load summaries
                _dailySummary.value = expenseRepository.getExpenseSummary("daily")
                _monthlySummary.value = expenseRepository.getExpenseSummary("monthly")
                _yearlySummary.value = expenseRepository.getExpenseSummary("yearly")
                
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun refreshData() {
        loadDashboardData()
    }
}