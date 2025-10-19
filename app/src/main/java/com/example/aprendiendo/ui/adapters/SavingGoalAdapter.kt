package com.example.aprendiendo.ui.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.aprendiendo.data.entities.SavingGoal
import com.example.aprendiendo.databinding.ItemSavingGoalBinding
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class SavingGoalAdapter(
    private val onGoalClick: (SavingGoal) -> Unit,
    private val onAddMoneyClick: (SavingGoal) -> Unit,
    private val onEditClick: (SavingGoal) -> Unit
) : ListAdapter<SavingGoal, SavingGoalAdapter.SavingGoalViewHolder>(SavingGoalDiffCallback()) {
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavingGoalViewHolder {
        val binding = ItemSavingGoalBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return SavingGoalViewHolder(binding)
    }
    
    override fun onBindViewHolder(holder: SavingGoalViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
    
    inner class SavingGoalViewHolder(
        private val binding: ItemSavingGoalBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        
        fun bind(goal: SavingGoal) {
            binding.apply {
                tvGoalName.text = goal.name
                tvGoalDescription.text = goal.description
                
                // Format amounts
                val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                tvCurrentAmount.text = formatter.format(goal.currentAmount)
                tvTargetAmount.text = "de ${formatter.format(goal.targetAmount)}"
                
                // Calculate progress
                val progress = if (goal.targetAmount > 0) {
                    ((goal.currentAmount / goal.targetAmount) * 100).toInt()
                } else {
                    0
                }
                
                progressBar.progress = progress
                tvProgress.text = "$progress%"
                
                // Status
                tvGoalStatus.text = if (goal.isCompleted) {
                    "Completado"
                } else {
                    "En progreso"
                }
                
                // Deadline
                tvDeadline.text = if (goal.deadline != null) {
                    val dateFormatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                    dateFormatter.format(goal.deadline)
                } else {
                    "Sin fecha límite"
                }
                
                // Click listeners
                root.setOnClickListener { onGoalClick(goal) }
                btnAddMoney.setOnClickListener { onAddMoneyClick(goal) }
                btnEdit.setOnClickListener { onEditClick(goal) }
            }
        }
    }
    
    class SavingGoalDiffCallback : DiffUtil.ItemCallback<SavingGoal>() {
        override fun areItemsTheSame(oldItem: SavingGoal, newItem: SavingGoal): Boolean {
            return oldItem.id == newItem.id
        }
        
        override fun areContentsTheSame(oldItem: SavingGoal, newItem: SavingGoal): Boolean {
            return oldItem == newItem
        }
    }
}