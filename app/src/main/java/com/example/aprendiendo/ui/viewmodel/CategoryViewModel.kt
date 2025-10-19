package com.example.aprendiendo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.aprendiendo.data.database.ExpenseDatabase
import com.example.aprendiendo.data.entities.Category
import com.example.aprendiendo.data.repository.ExpenseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val repository: ExpenseRepository
    val allCategories: LiveData<List<Category>>
    
    init {
        val database = ExpenseDatabase.getDatabase(application)
        repository = ExpenseRepository(
            database.expenseDao(),
            database.categoryDao(),
            database.savingGoalDao()
        )
        allCategories = repository.getAllCategories()
    }
    
    fun insertCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertCategory(category)
    }
    
    fun updateCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateCategory(category)
    }
    
    fun deleteCategory(category: Category) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteCategory(category)
    }
    
    suspend fun getCategoryByName(name: String): Category? {
        return repository.getCategoryByName(name)
    }
}