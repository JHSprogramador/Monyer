package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.aprendiendo.databinding.FragmentDashboardBinding
import com.example.aprendiendo.ui.viewmodel.ExpenseViewModel
import com.example.aprendiendo.ui.viewmodel.SavingGoalViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private val binding get() = _binding!!

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var savingGoalViewModel: SavingGoalViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        savingGoalViewModel = ViewModelProvider(this)[SavingGoalViewModel::class.java]

        setupObservers()
    }

    private fun setupObservers() {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())

        // Observar gastos totales
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            val total = expenses.sumOf { it.amount }
            binding.tvTotalExpenses.text = formatter.format(total)
            binding.tvExpenseCount.text = "${expenses.size} gastos"

            // Calcular promedio diario (últimos 30 días)
            if (expenses.isNotEmpty()) {
                val avgDaily = total / 30
                binding.tvAvgDaily.text = formatter.format(avgDaily)
            } else {
                binding.tvAvgDaily.text = formatter.format(0.0)
            }

            // Gastos por categoría
            val byCategory = expenses.groupBy { it.category }
            val topCategory = byCategory.maxByOrNull { it.value.sumOf { expense -> expense.amount } }
            binding.tvTopCategory.text = topCategory?.key ?: "N/A"
            binding.tvTopCategoryAmount.text = formatter.format(
                topCategory?.value?.sumOf { it.amount } ?: 0.0
            )

            // Categorías con más gastos
            updateCategoryBreakdown(byCategory, formatter)
        }

        // Observar objetivos de ahorro
        savingGoalViewModel.allSavingGoals.observe(viewLifecycleOwner) { goals ->
            val activeGoals = goals.filter { !it.isCompleted }
            val completedGoals = goals.filter { it.isCompleted }

            binding.tvActiveGoals.text = "${activeGoals.size}"
            binding.tvCompletedGoals.text = "${completedGoals.size}"

            val totalSaved = goals.sumOf { it.currentAmount }
            val totalTarget = goals.sumOf { it.targetAmount }

            binding.tvTotalSaved.text = formatter.format(totalSaved)
            binding.tvTotalTarget.text = formatter.format(totalTarget)

            // Calcular progreso general
            val overallProgress = if (totalTarget > 0) {
                ((totalSaved / totalTarget) * 100).toInt()
            } else 0
            binding.progressOverall.progress = overallProgress
            binding.tvProgressPercentage.text = "$overallProgress%"
        }

        // Fecha de última actualización
        val dateFormat = SimpleDateFormat("dd MMM yyyy, HH:mm", Locale.getDefault())
        binding.tvLastUpdate.text = "Última actualización: ${dateFormat.format(Date())}"
    }

    private fun updateCategoryBreakdown(
        categoryMap: Map<String, List<com.example.aprendiendo.data.entities.Expense>>,
        formatter: NumberFormat
    ) {
        val sortedCategories = categoryMap.entries
            .sortedByDescending { it.value.sumOf { expense -> expense.amount } }
            .take(5)

        val breakdown = StringBuilder()
        sortedCategories.forEachIndexed { index, entry ->
            val total = entry.value.sumOf { it.amount }
            val icon = getCategoryIcon(entry.key)
            breakdown.append("$icon ${entry.key}: ${formatter.format(total)}\n")
        }

        binding.tvCategoryBreakdown.text = if (breakdown.isEmpty()) {
            "No hay gastos registrados"
        } else {
            breakdown.toString().trim()
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

