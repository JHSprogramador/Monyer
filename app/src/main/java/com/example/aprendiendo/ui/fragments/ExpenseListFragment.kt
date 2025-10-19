package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendiendo.R
import com.example.aprendiendo.databinding.FragmentExpenseListBinding
import com.example.aprendiendo.ui.adapters.ExpenseAdapter
import com.example.aprendiendo.ui.viewmodel.ExpenseViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

class ExpenseListFragment : Fragment() {

    private var _binding: FragmentExpenseListBinding? = null
    private val binding get() = _binding!!

    private lateinit var expenseViewModel: ExpenseViewModel
    private lateinit var expenseAdapter: ExpenseAdapter
    private var currentFilter: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentExpenseListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]

        setupRecyclerView()
        observeData()
        setupClickListeners()
    }

    private fun setupRecyclerView() {
        expenseAdapter = ExpenseAdapter { expense ->
            // Mostrar opciones al hacer click en un gasto
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Gasto: ${expense.description}")
                .setMessage("Monto: ${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(expense.amount)}\n" +
                        "Categoría: ${expense.category}")
                .setPositiveButton("Eliminar") { _, _ ->
                    confirmDeleteExpense(expense)
                }
                .setNegativeButton("Cancelar", null)
                .show()
        }

        binding.recyclerViewExpenses.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = expenseAdapter
        }
    }

    private fun confirmDeleteExpense(expense: com.example.aprendiendo.data.entities.Expense) {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("⚠️ Confirmar eliminación")
            .setMessage("¿Estás seguro de eliminar este gasto?\n\n${expense.description}\n${NumberFormat.getCurrencyInstance(Locale.getDefault()).format(expense.amount)}")
            .setPositiveButton("Eliminar") { _, _ ->
                expenseViewModel.deleteExpense(expense)
                Snackbar.make(binding.root, "Gasto eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        expenseViewModel.insertExpense(expense)
                    }
                    .show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun observeData() {
        // Observar todos los gastos - un solo observer
        expenseViewModel.allExpenses.observe(viewLifecycleOwner) { expenses ->
            // Aplicar filtro si existe
            val filteredExpenses = if (currentFilter != null) {
                expenses.filter { it.category == currentFilter }
            } else {
                expenses
            }
            updateUI(filteredExpenses)
        }

        expenseViewModel.totalExpenses.observe(viewLifecycleOwner) { total ->
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            binding.tvTotalAmount.text = formatter.format(total ?: 0.0)
        }
    }

    private fun updateUI(expenses: List<com.example.aprendiendo.data.entities.Expense>) {
        if (expenses.isEmpty()) {
            binding.emptyState.visibility = View.VISIBLE
            binding.recyclerViewExpenses.visibility = View.GONE
        } else {
            binding.emptyState.visibility = View.GONE
            binding.recyclerViewExpenses.visibility = View.VISIBLE
            expenseAdapter.submitList(expenses)
        }
    }

    private fun setupClickListeners() {
        binding.chipAll.setOnClickListener {
            if (!binding.chipAll.isChecked) {
                binding.chipAll.isChecked = true
                return@setOnClickListener
            }
            currentFilter = null
            // Trigger data update
            expenseViewModel.allExpenses.value?.let { updateUI(it) }
        }

        binding.chipFood.setOnClickListener {
            if (!binding.chipFood.isChecked) {
                binding.chipFood.isChecked = true
                return@setOnClickListener
            }
            currentFilter = "Alimentación"
            expenseViewModel.allExpenses.value?.let { expenses ->
                updateUI(expenses.filter { it.category == currentFilter })
            }
        }

        binding.chipTransport.setOnClickListener {
            if (!binding.chipTransport.isChecked) {
                binding.chipTransport.isChecked = true
                return@setOnClickListener
            }
            currentFilter = "Transporte"
            expenseViewModel.allExpenses.value?.let { expenses ->
                updateUI(expenses.filter { it.category == currentFilter })
            }
        }

        // Listener del ChipGroup para manejar selección única
        binding.chipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            if (checkedIds.isEmpty()) {
                // Si no hay nada seleccionado, seleccionar "Todas"
                binding.chipAll.isChecked = true
                currentFilter = null
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
