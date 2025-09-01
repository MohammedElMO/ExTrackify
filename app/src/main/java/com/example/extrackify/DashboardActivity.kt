package com.example.extrackify

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.extrackify.databinding.ActivityDashboardBinding
import com.example.extrackify.fragments.AnalyticsFragment
import com.example.extrackify.fragments.ExpenseListFragment
import com.example.extrackify.fragments.OverviewFragment
import com.example.extrackify.utils.navigation.NavigationUtils
import com.example.extrackify.view_model.DashboardViewModel
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DashboardActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityDashboardBinding
    private val dashboardViewModel: DashboardViewModel by viewModels()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        
        setupViewPager()
        setupBottomNavigation()
        
        // Observe data
        dashboardViewModel.loadDashboardData()
    }
    
    private fun setupViewPager() {
        val adapter = DashboardPagerAdapter()
        binding.viewPager.adapter = adapter
        
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Analytics"
                2 -> "Expenses"
                else -> ""
            }
        }.attach()
    }
    
    private fun setupBottomNavigation() {
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_dashboard -> true
                R.id.nav_add_expense -> {
                    NavigationUtils.navigateToActivity(this, AddExpenseActivity::class.java)
                    false
                }
                R.id.nav_expenses -> {
                    NavigationUtils.navigateToActivity(this, ExpenseListActivity::class.java)
                    false
                }
                R.id.nav_settings -> {
                    NavigationUtils.navigateToActivity(this, SettingsActivity::class.java)
                    false
                }
                else -> false
            }
        }
        
        // Set dashboard as selected
        binding.bottomNavigation.selectedItemId = R.id.nav_dashboard
    }
    
    private inner class DashboardPagerAdapter : FragmentStateAdapter(this) {
        override fun getItemCount(): Int = 3
        
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> OverviewFragment()
                1 -> AnalyticsFragment()
                2 -> ExpenseListFragment()
                else -> OverviewFragment()
            }
        }
    }
}