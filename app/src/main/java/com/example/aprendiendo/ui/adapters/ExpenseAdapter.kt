package com.example.aprendiendo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aprendiendo.data.entities.Expense
import com.example.aprendiendo.databinding.ItemExpenseBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class ExpenseAdapter(
    private val onExpenseClick: (Expense) -> Unit
) : ListAdapter<Expense, ExpenseAdapter.ExpenseViewHolder>(ExpenseDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val binding = ItemExpenseBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ExpenseViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class ExpenseViewHolder(
        private val binding: ItemExpenseBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(expense: Expense) {
            binding.apply {
                tvDescription.text = expense.description
                tvCategory.text = expense.category
                
                // Format amount
                val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                tvAmount.text = formatter.format(expense.amount)
                
                // Format date
                val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                tvDate.text = dateFormatter.format(expense.date)
                
                // Set category icon based on category
                tvCategoryIcon.text = getCategoryIcon(expense.category)
                
                root.setOnClickListener {
                    onExpenseClick(expense)
                }
            }
        }
        
        private fun getCategoryIcon(category: String): String {
            return when (category) {
                "Alimentación" -> "🍽️"
                "Transporte" -> "🚗"
                "Entretenimiento" -> "🎬"
                "Compras" -> "🛒"
                "Salud" -> "🏥"
                "Educación" -> "📚"
                "Servicios" -> "🔧"
                else -> "💰"
            }
        }
    }
    
    class ExpenseDiffCallback : DiffUtil.ItemCallback<Expense>() {
        override fun areItemsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: Expense, newItem: Expense): Boolean {
            return oldItem == newItem
        }
    }
}