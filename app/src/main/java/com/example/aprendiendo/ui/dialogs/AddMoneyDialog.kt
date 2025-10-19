package com.example.aprendiendo.ui.dialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.example.aprendiendo.data.entities.SavingGoal
import com.example.aprendiendo.ui.viewmodel.SavingGoalViewModel
import com.google.android.material.snackbar.Snackbar

class AddMoneyDialog(
    private val savingGoal: SavingGoal,
    private val onAmountAdded: () -> Unit
) : DialogFragment() {
    
    private lateinit var savingGoalViewModel: SavingGoalViewModel
    
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        savingGoalViewModel = ViewModelProvider(this)[SavingGoalViewModel::class.java]
        
        val editText = EditText(requireContext()).apply {
            hint = "Cantidad a agregar"
            inputType = android.text.InputType.TYPE_CLASS_NUMBER or android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL
        }
        
        return AlertDialog.Builder(requireContext())
            .setTitle("Agregar dinero a ${savingGoal.name}")
            .setMessage("Monto actual: $${savingGoal.currentAmount}")
            .setView(editText)
            .setPositiveButton("Agregar") { _, _ ->
                val amountText = editText.text.toString()
                if (amountText.isNotEmpty()) {
                    try {
                        val amount = amountText.toDouble()
                        if (amount > 0) {
                            val newAmount = savingGoal.currentAmount + amount
                            savingGoalViewModel.updateCurrentAmount(savingGoal.id, newAmount)
                            
                            // Check if goal is completed
                            if (newAmount >= savingGoal.targetAmount) {
                                savingGoalViewModel.markGoalAsCompleted(savingGoal.id)
                            }
                            
                            onAmountAdded()
                        }
                    } catch (e: NumberFormatException) {
                        // Handle invalid number
                    }
                }
            }
            .setNegativeButton("Cancelar", null)
            .create()
    }
}