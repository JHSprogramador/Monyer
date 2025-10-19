package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aprendiendo.R
import com.example.aprendiendo.databinding.FragmentDashboardBinding
import com.example.aprendiendo.ui.viewmodel.ExpenseViewModel
import com.example.aprendiendo.ui.viewmodel.SavingGoalViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
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
        setupClickListeners()
    }

    private fun setupClickListeners() {
        // Botón del Asistente de IA
        binding.cardAIAssistant.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_AIAssistantFragment)
            } catch (e: Exception) {
                showMessage("Abriendo Asistente de IA...")
            }
        }

        // Card de Resumen de Gastos - navega a la lista de gastos
        binding.cardExpenseSummary.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_ExpenseListFragment)
            } catch (e: Exception) {
                showMessage("Navegando a Gastos...")
            }
        }

        // Card de Top 5 Categorías - navega a la lista de gastos
        binding.cardCategoryBreakdown.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_ExpenseListFragment)
            } catch (e: Exception) {
                showMessage("Navegando a Gastos por Categoría...")
            }
        }

        // Card de Objetivos de Ahorro - navega a objetivos
        binding.cardSavingGoals.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_SavingGoalsFragment)
            } catch (e: Exception) {
                showMessage("Navegando a Objetivos de Ahorro...")
            }
        }

        // Click en la categoría principal para ver detalles
        binding.layoutTopCategory.setOnClickListener {
            val category = binding.tvTopCategory.text.toString()
            if (category != "N/A") {
                showCategoryDetails(category)
            }
        }

        // Click en Total Gastado
        binding.layoutTotalExpenses.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_ExpenseListFragment)
            } catch (e: Exception) {
                showMessage("Ver todos los gastos")
            }
        }

        // Click en Promedio Diario
        binding.layoutAvgDaily.setOnClickListener {
            showAverageDailyInfo()
        }

        // Click en objetivos activos
        binding.layoutActiveGoals.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_SavingGoalsFragment)
            } catch (e: Exception) {
                showMessage("Ver objetivos activos")
            }
        }

        // Click en objetivos completados
        binding.layoutCompletedGoals.setOnClickListener {
            try {
                findNavController().navigate(R.id.action_DashboardFragment_to_SavingGoalsFragment)
            } catch (e: Exception) {
                showMessage("Ver objetivos completados")
            }
        }
    }

    private fun showCategoryDetails(category: String) {
        expenseViewModel.allExpenses.value?.let { expenses ->
            val categoryExpenses = expenses.filter { it.category == category }
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            val total = categoryExpenses.sumOf { it.amount }
            val count = categoryExpenses.size
            val icon = getCategoryIcon(category)

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("$icon $category")
                .setMessage(
                    "Total gastado: ${formatter.format(total)}\n" +
                    "Número de gastos: $count\n" +
                    "Promedio por gasto: ${formatter.format(if (count > 0) total / count else 0.0)}"
                )
                .setPositiveButton("Ver Todos") { _, _ ->
                    try {
                        findNavController().navigate(R.id.action_DashboardFragment_to_ExpenseListFragment)
                    } catch (e: Exception) {
                        showMessage("Navegando a gastos...")
                    }
                }
                .setNegativeButton("Cerrar", null)
                .show()
        }
    }

    private fun showAverageDailyInfo() {
        expenseViewModel.allExpenses.value?.let { expenses ->
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            val total = expenses.sumOf { it.amount }
            val avgDaily = total / 30
            val avgWeekly = avgDaily * 7
            val avgMonthly = avgDaily * 30

            MaterialAlertDialogBuilder(requireContext())
                .setTitle("📊 Análisis de Gastos")
                .setMessage(
                    "Promedio diario: ${formatter.format(avgDaily)}\n" +
                    "Promedio semanal: ${formatter.format(avgWeekly)}\n" +
                    "Promedio mensual: ${formatter.format(avgMonthly)}\n\n" +
                    "Basado en los últimos 30 días"
                )
                .setPositiveButton("Entendido", null)
                .show()
        }
    }

    private fun showMessage(message: String) {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(message)
            .setPositiveButton("OK", null)
            .show()
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
        sortedCategories.forEachIndexed { _, entry ->
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

