package com.example.extrackify.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extrackify.adapters.RecentExpensesAdapter
import com.example.extrackify.databinding.FragmentOverviewBinding
import com.example.extrackify.view_model.DashboardViewModel
import java.text.NumberFormat
import java.util.*

class OverviewFragment : Fragment() {
    
    private var _binding: FragmentOverviewBinding? = null
    private val binding get() = _binding!!
    
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    private lateinit var recentExpensesAdapter: RecentExpensesAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOverviewBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeData()
        
        binding.swipeRefresh.setOnRefreshListener {
            dashboardViewModel.refreshData()
        }
    }
    
    private fun setupRecyclerView() {
        recentExpensesAdapter = RecentExpensesAdapter { expense ->
            // Handle expense item click
        }
        
        binding.recyclerViewRecentExpenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = recentExpensesAdapter
        }
    }
    
    private fun observeData() {
        dashboardViewModel.dailySummary.observe(viewLifecycleOwner) { summary ->
            binding.textTodayAmount.text = formatCurrency(summary.totalAmount)
            binding.textTodayTransactions.text = "${summary.transactionCount} transactions"
        }
        
        dashboardViewModel.monthlySummary.observe(viewLifecycleOwner) { summary ->
            binding.textMonthlyAmount.text = formatCurrency(summary.totalAmount)
            binding.textMonthlyTransactions.text = "${summary.transactionCount} transactions"
        }
        
        dashboardViewModel.recentExpenses.observe(viewLifecycleOwner) { expenses ->
            recentExpensesAdapter.submitList(expenses)
        }
        
        dashboardViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.swipeRefresh.isRefreshing = isLoading
        }
    }
    
    private fun formatCurrency(amount: Double): String {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        return formatter.format(amount)
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}