package com.example.aprendiendo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.aprendiendo.data.database.ExpenseDatabase
import com.example.aprendiendo.data.entities.Expense
import com.example.aprendiendo.data.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class ExpenseViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ExpenseRepository
    val allExpenses: LiveData<List<Expense>>
    val totalExpenses: LiveData<Double?>
    
    init {
        val database = ExpenseDatabase.getDatabase(application)
        repository = ExpenseRepository(
            database.expenseDao(),
            database.categoryDao(),
            database.savingGoalDao()
        )
        allExpenses = repository.getAllExpenses()
        totalExpenses = repository.getTotalExpenses()
    }
    
    fun insertExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertExpense(expense)
    }
    
    fun updateExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateExpense(expense)
    }
    
    fun deleteExpense(expense: Expense) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteExpense(expense)
    }
    
    fun deleteExpenseById(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteExpenseById(id)
    }
    
    fun getExpensesByCategory(category: String): LiveData<List<Expense>> {
        return repository.getExpensesByCategory(category)
    }
    
    fun getExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<List<Expense>> {
        return repository.getExpensesBetweenDates(startDate, endDate)
    }
    
    fun getTotalExpensesByCategory(category: String): LiveData<Double?> {
        return repository.getTotalExpensesByCategory(category)
    }
    
    fun getTotalExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<Double?> {
        return repository.getTotalExpensesBetweenDates(startDate, endDate)
    }
}