package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extrackify.adapters.ExpenseListAdapter
import com.example.extrackify.databinding.ActivityExpenseListBinding
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.ExpenseListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ExpenseListActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityExpenseListBinding
    private val expenseListViewModel: ExpenseListViewModel by viewModels()
    private lateinit var expenseAdapter: ExpenseListAdapter
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityExpenseListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupUI()
        observeViewModel()
        
        expenseListViewModel.loadExpenses()
    }
    
    private fun setupUI() {
        // Setup toolbar
        binding.toolbar.setNavigationOnClickListener {
            finish()
        }
        
        // Setup RecyclerView
        expenseAdapter = ExpenseListAdapter(
            onItemClick = { expense ->
                NavigationUtils.navigateToActivity(this, ExpenseDetailActivity::class.java)
            },
            onDeleteClick = { expense ->
                expenseListViewModel.deleteExpense(expense.id)
            }
        )
        
        binding.recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(this@ExpenseListActivity)
            adapter = expenseAdapter
        }
        
        // Setup swipe refresh
        binding.swipeRefresh.setOnRefreshListener {
            expenseListViewModel.refreshExpenses()
        }
        
        // Setup FAB
        binding.fabAddExpense.setOnClickListener {
            NavigationUtils.navigateToActivity(this, AddExpenseActivity::class.java)
        }
    }
    
    private fun observeViewModel() {
        expenseListViewModel.expenses.observe(this) { expenses ->
            expenseAdapter.submitList(expenses)
            binding.textEmptyState.visibility = if (expenses.isEmpty()) 
                android.view.View.VISIBLE else android.view.View.GONE
        }
        
        expenseListViewModel.isLoading.observe(this) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
        
        expenseListViewModel.error.observe(this) { error ->
            error?.let {
                // Show error message
            }
        }
    }
}