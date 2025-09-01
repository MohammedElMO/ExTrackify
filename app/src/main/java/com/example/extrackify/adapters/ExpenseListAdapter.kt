package com.example.extrackify.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.extrackify.databinding.ItemExpenseBinding
import com.example.extrackify.models.ExpenseModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ExpenseListAdapter(
    private val onItemClick: (ExpenseModel) -> Unit,
    private val onDeleteClick: (ExpenseModel) -> Unit
) : ListAdapter<ExpenseModel, ExpenseListAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ExpenseViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(expense: ExpenseModel) {
            binding.apply {
                textCategory.text = expense.category
                textDescription.text = expense.description
                textAmount.text = formatCurrency(expense.amount)
                textDate.text = formatDate(expense.date)
                textPaymentMethod.text = expense.paymentMethod
                
                root.setOnClickListener {
                    onItemClick(expense)
                }
                
                buttonDelete.setOnClickListener {
                    onDeleteClick(expense)
                }
            }
        }
        
        private fun formatCurrency(amount: Double): String {
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            return formatter.format(amount)
        }
        
        private fun formatDate(date: Date): String {
            val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
            return formatter.format(date)
        }
    }

    private class ExpenseDiffCallback : DiffUtil.ItemCallback<ExpenseModel>() {
        override fun areItemsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ExpenseModel, newItem: ExpenseModel): Boolean {
            return oldItem == newItem
        }
    }
}