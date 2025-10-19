package com.example.aprendiendo.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aprendiendo.data.entities.SavingGoal
import com.example.aprendiendo.databinding.FragmentAddSavingGoalBinding
import com.example.aprendiendo.ui.viewmodel.SavingGoalViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddSavingGoalFragment : Fragment() {
    
    private var _binding: FragmentAddSavingGoalBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var savingGoalViewModel: SavingGoalViewModel
    private var selectedDeadline: Date? = null
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddSavingGoalBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        savingGoalViewModel = ViewModelProvider(this)[SavingGoalViewModel::class.java]
        
        setupDatePicker()
        setupClickListeners()
    }
    
    private fun setupDatePicker() {
        binding.etDeadline.setOnClickListener {
            val calendar = Calendar.getInstance()
            selectedDeadline?.let { calendar.time = it }
            
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDeadline = calendar.time
                    updateDeadlineField()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    
    private fun updateDeadlineField() {
        selectedDeadline?.let { date ->
            val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
            binding.etDeadline.setText(dateFormatter.format(date))
        }
    }
    
    private fun setupClickListeners() {
        binding.btnSaveGoal.setOnClickListener {
            saveSavingGoal()
        }
        
        binding.btnCancelGoal.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun saveSavingGoal() {
        val name = binding.etGoalName.text.toString()
        val description = binding.etGoalDescription.text.toString()
        val targetAmountText = binding.etTargetAmount.text.toString()
        val currentAmountText = binding.etCurrentAmount.text.toString()
        
        // Validation
        if (name.isEmpty()) {
            binding.etGoalName.error = "Ingresa el nombre del objetivo"
            return
        }
        
        if (targetAmountText.isEmpty()) {
            binding.etTargetAmount.error = "Ingresa el monto objetivo"
            return
        }
        
        try {
            val targetAmount = targetAmountText.toDouble()
            if (targetAmount <= 0) {
                binding.etTargetAmount.error = "El monto debe ser mayor a 0"
                return
            }
            
            val currentAmount = if (currentAmountText.isEmpty()) {
                0.0
            } else {
                currentAmountText.toDouble()
            }
            
            if (currentAmount < 0) {
                binding.etCurrentAmount.error = "El monto no puede ser negativo"
                return
            }
            
            val savingGoal = SavingGoal(
                name = name,
                description = description,
                targetAmount = targetAmount,
                currentAmount = currentAmount,
                deadline = selectedDeadline,
                createdDate = Date()
            )
            
            savingGoalViewModel.insertSavingGoal(savingGoal)
            
            Snackbar.make(binding.root, "Objetivo creado exitosamente", Snackbar.LENGTH_SHORT).show()
            findNavController().navigateUp()
            
        } catch (e: NumberFormatException) {
            binding.etTargetAmount.error = "Monto inválido"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}