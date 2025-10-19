package com.example.aprendiendo.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.aprendiendo.databinding.FragmentSavingGoalsBinding
import com.example.aprendiendo.ui.adapters.SavingGoalAdapter
import com.example.aprendiendo.ui.viewmodel.SavingGoalViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import java.text.NumberFormat
import java.util.*

class SavingGoalsFragment : Fragment() {

    private var _binding: FragmentSavingGoalsBinding? = null
    private val binding get() = _binding!!

    private lateinit var savingGoalViewModel: SavingGoalViewModel
    private lateinit var goalAdapter: SavingGoalAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavingGoalsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        savingGoalViewModel = ViewModelProvider(this)[SavingGoalViewModel::class.java]

        setupRecyclerView()
        observeData()
    }

    private fun setupRecyclerView() {
        goalAdapter = SavingGoalAdapter(
            onGoalClick = { goal ->
                // Mostrar detalles del objetivo
                val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
                val progress = if (goal.targetAmount > 0) {
                    ((goal.currentAmount / goal.targetAmount) * 100).toInt()
                } else 0

                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("📊 ${goal.name}")
                    .setMessage(
                        "Descripción: ${goal.description}\n\n" +
                                "Progreso: $progress%\n" +
                                "Actual: ${formatter.format(goal.currentAmount)}\n" +
                                "Objetivo: ${formatter.format(goal.targetAmount)}\n" +
                                "Falta: ${formatter.format(goal.targetAmount - goal.currentAmount)}"
                    )
                    .setPositiveButton("Cerrar", null)
                    .show()
            },
            onAddMoneyClick = { goal ->
                // Show dialog to add money to goal
                val dialog = com.example.aprendiendo.ui.dialogs.AddMoneyDialog(goal) {
                    // Refresh the data after adding money - no need to update, LiveData handles it
                }
                dialog.show(parentFragmentManager, "AddMoneyDialog")
            },
            onEditClick = { goal ->
                // Mostrar opciones de edición
                MaterialAlertDialogBuilder(requireContext())
                    .setTitle("Editar: ${goal.name}")
                    .setItems(arrayOf("Modificar monto actual", "Eliminar objetivo")) { _, which ->
                        when (which) {
                            0 -> showEditAmountDialog(goal)
                            1 -> confirmDeleteGoal(goal)
                        }
                    }
                    .setNegativeButton("Cancelar", null)
                    .show()
            }
        )

        binding.recyclerViewGoals.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = goalAdapter
        }
    }

    private fun showEditAmountDialog(goal: com.example.aprendiendo.data.entities.SavingGoal) {
        val editText = android.widget.EditText(requireContext()).apply {
            hint = "Nuevo monto actual"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
            setText(goal.currentAmount.toString())
        }

        MaterialAlertDialogBuilder(requireContext())
            .setTitle("Modificar monto actual")
            .setMessage("Objetivo: ${goal.name}")
            .setView(editText)
            .setPositiveButton("Actualizar") { _, _ ->
                val newAmount = editText.text.toString().toDoubleOrNull()
                if (newAmount != null && newAmount >= 0) {
                    savingGoalViewModel.updateCurrentAmount(goal.id, newAmount)
                    if (newAmount >= goal.targetAmount) {
                        savingGoalViewModel.markGoalAsCompleted(goal.id)
                    }
                    Snackbar.make(binding.root, "Monto actualizado", Snackbar.LENGTH_SHORT).show()
                } else {
                    Snackbar.make(binding.root, "Monto inválido", Snackbar.LENGTH_SHORT).show()
                }
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun confirmDeleteGoal(goal: com.example.aprendiendo.data.entities.SavingGoal) {
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("⚠️ Eliminar objetivo")
            .setMessage("¿Estás seguro de eliminar este objetivo?\n\n${goal.name}\nMonto actual: ${formatter.format(goal.currentAmount)}")
            .setPositiveButton("Eliminar") { _, _ ->
                savingGoalViewModel.deleteSavingGoal(goal)
                Snackbar.make(binding.root, "Objetivo eliminado", Snackbar.LENGTH_LONG)
                    .setAction("Deshacer") {
                        savingGoalViewModel.insertSavingGoal(goal)
                    }
                    .show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun observeData() {
        savingGoalViewModel.allSavingGoals.observe(viewLifecycleOwner) { goals ->
            if (goals.isEmpty()) {
                binding.emptyStateGoals.visibility = View.VISIBLE
                binding.recyclerViewGoals.visibility = View.GONE
            } else {
                binding.emptyStateGoals.visibility = View.GONE
                binding.recyclerViewGoals.visibility = View.VISIBLE
                goalAdapter.submitList(goals)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
