package com.example.aprendiendo.ui.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.aprendiendo.R
import com.example.aprendiendo.data.entities.Expense
import com.example.aprendiendo.databinding.FragmentAddExpenseBinding
import com.example.aprendiendo.ui.viewmodel.ExpenseViewModel
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat
import java.util.*

class AddExpenseFragment : Fragment() {
    
    private var _binding: FragmentAddExpenseBinding? = null
    private val binding get() = _binding!!
    
    private lateinit var expenseViewModel: ExpenseViewModel
    private var selectedDate = Date()
    
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddExpenseBinding.inflate(inflater, container, false)
        return binding.root
    }
    
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        expenseViewModel = ViewModelProvider(this)[ExpenseViewModel::class.java]
        
        setupCategoryDropdown()
        setupDatePicker()
        setupClickListeners()
        
        // Set default date to today
        updateDateField()
    }
    
    private fun setupCategoryDropdown() {
        val categories = resources.getStringArray(R.array.expense_categories)
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_dropdown_item_1line,
            categories
        )
        binding.actvCategory.setAdapter(adapter)
    }
    
    private fun setupDatePicker() {
        binding.etDate.setOnClickListener {
            val calendar = Calendar.getInstance()
            calendar.time = selectedDate
            
            DatePickerDialog(
                requireContext(),
                { _, year, month, dayOfMonth ->
                    calendar.set(year, month, dayOfMonth)
                    selectedDate = calendar.time
                    updateDateField()
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }
    
    private fun updateDateField() {
        val dateFormatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        binding.etDate.setText(dateFormatter.format(selectedDate))
    }
    
    private fun setupClickListeners() {
        binding.btnSave.setOnClickListener {
            saveExpense()
        }
        
        binding.btnCancel.setOnClickListener {
            findNavController().navigateUp()
        }
    }
    
    private fun saveExpense() {
        val amountText = binding.etAmount.text.toString()
        val description = binding.etDescription.text.toString()
        val category = binding.actvCategory.text.toString()
        
        // Validation
        if (amountText.isEmpty()) {
            binding.etAmount.error = "Ingresa el monto"
            return
        }
        
        if (description.isEmpty()) {
            binding.etDescription.error = "Ingresa una descripción"
            return
        }
        
        if (category.isEmpty()) {
            binding.actvCategory.error = "Selecciona una categoría"
            return
        }
        
        try {
            val amount = amountText.toDouble()
            if (amount <= 0) {
                binding.etAmount.error = "El monto debe ser mayor a 0"
                return
            }
            
            val expense = Expense(
                amount = amount,
                description = description,
                category = category,
                date = selectedDate
            )
            
            expenseViewModel.insertExpense(expense)
            
            Snackbar.make(binding.root, "Gasto guardado exitosamente", Snackbar.LENGTH_SHORT).show()
            findNavController().navigateUp()
            
        } catch (e: NumberFormatException) {
            binding.etAmount.error = "Monto inválido"
        }
    }
    
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}