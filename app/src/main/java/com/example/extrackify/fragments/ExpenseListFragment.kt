package com.example.extrackify.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.extrackify.AddExpenseActivity
import com.example.extrackify.adapters.ExpenseListAdapter
import com.example.extrackify.databinding.FragmentExpenseListBinding
import com.example.extrackify.view_model.DashboardViewModel

class ExpenseListFragment : Fragment() {
    
    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!
    
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    private lateinit var expenseAdapter: ExpenseListAdapter
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupRecyclerView()
        observeData()
        
        binding.fabAddExpense.setOnClickListener {
            startActivity(Intent(requireContext(), AddExpenseActivity::class.java))
        }
    }
    
    private fun setupRecyclerView() {
        expenseAdapter = ExpenseListAdapter(
            onItemClick = { expense ->
                // Handle expense detail navigation
            },
            onDeleteClick = { expense ->
                // Handle delete
            }
        )
        
        binding.recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expenseAdapter
        }
    }
    
    private fun observeData() {
        dashboardViewModel.recentExpenses.observe(viewLifecycleOwner) { expenses ->
            expenseAdapter.submitList(expenses)
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}