package com.example.aprendiendo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.aprendiendo.data.database.ExpenseDatabase
import com.example.aprendiendo.data.entities.SavingGoal
import com.example.aprendiendo.data.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SavingGoalViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ExpenseRepository
    val allSavingGoals: LiveData<List<SavingGoal>>
    val activeSavingGoals: LiveData<List<SavingGoal>>
    
    init {
        val database = ExpenseDatabase.getDatabase(application)
        repository = ExpenseRepository(
            database.expenseDao(),
            database.categoryDao(),
            database.savingGoalDao()
        )
        allSavingGoals = repository.getAllSavingGoals()
        activeSavingGoals = repository.getActiveSavingGoals()
    }
    
    fun insertSavingGoal(savingGoal: SavingGoal) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertSavingGoal(savingGoal)
    }
    
    fun updateSavingGoal(savingGoal: SavingGoal) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateSavingGoal(savingGoal)
    }
    
    fun deleteSavingGoal(savingGoal: SavingGoal) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteSavingGoal(savingGoal)
    }
    
    fun updateCurrentAmount(id: Long, newAmount: Double) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCurrentAmount(id, newAmount)
    }
    
    fun markGoalAsCompleted(id: Long) = viewModelScope.launch(Dispatchers.IO) {
        repository.markGoalAsCompleted(id)
    }
    
    suspend fun getSavingGoalById(id: Long): SavingGoal? {
        return repository.getSavingGoalById(id)
    }
}