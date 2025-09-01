package com.example.extrackify.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.example.extrackify.R
import com.example.extrackify.databinding.FragmentAnalyticsBinding
import com.example.extrackify.view_model.DashboardViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class AnalyticsFragment : Fragment() {
    
    private var _binding: FragmentAnalyticsBinding? = null
    private val binding get() = _binding!!
    
    private val dashboardViewModel: DashboardViewModel by activityViewModels()
    
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAnalyticsBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        setupCharts()
        observeData()
    }
    
    private fun setupCharts() {
        setupPieChart(binding.pieChartCategories)
    }
    
    private fun setupPieChart(chart: PieChart) {
        chart.apply {
            setUsePercentValues(true)
            description.isEnabled = false
            setExtraOffsets(5f, 10f, 5f, 5f)
            dragDecelerationFrictionCoef = 0.95f
            isDrawHoleEnabled = true
            setHoleColor(Color.WHITE)
            setTransparentCircleColor(Color.WHITE)
            setTransparentCircleAlpha(110)
            holeRadius = 58f
            transparentCircleRadius = 61f
            setDrawCenterText(true)
            rotationAngle = 0f
            isRotationEnabled = true
            isHighlightPerTapEnabled = true
            
            legend.isEnabled = true
            legend.textSize = 12f
        }
    }
    
    private fun observeData() {
        dashboardViewModel.monthlySummary.observe(viewLifecycleOwner) { summary ->
            updatePieChart(binding.pieChartCategories, summary.categoryBreakdown)
        }
    }
    
    private fun updatePieChart(chart: PieChart, categoryData: Map<String, Double>) {
        if (categoryData.isEmpty()) {
            chart.clear()
            return
        }
        
        val entries = categoryData.map { (category, amount) ->
            PieEntry(amount.toFloat(), category)
        }
        
        val dataSet = PieDataSet(entries, "Expense Categories").apply {
            setDrawIcons(false)
            sliceSpace = 3f
            iconsOffset = com.github.mikephil.charting.utils.MPPointF(0f, 40f)
            selectionShift = 5f
            
            colors = listOf(
                Color.parseColor("#FF6B6B"), // Food
                Color.parseColor("#4ECDC4"), // Transport
                Color.parseColor("#45B7D1"), // Entertainment
                Color.parseColor("#96CEB4"), // Shopping
                Color.parseColor("#FFEAA7"), // Bills
                Color.parseColor("#DDA0DD"), // Health
                Color.parseColor("#98D8C8"), // Education
                Color.parseColor("#F7DC6F")  // Other
            )
        }
        
        val data = PieData(dataSet).apply {
            setValueFormatter(PercentFormatter())
            setValueTextSize(11f)
            setValueTextColor(Color.WHITE)
        }
        
        chart.data = data
        chart.highlightValues(null)
        chart.invalidate()
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}