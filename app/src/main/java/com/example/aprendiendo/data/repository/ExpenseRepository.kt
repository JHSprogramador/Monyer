package com.example.aprendiendo.data.repository

import androidx.lifecycle.LiveData
import com.example.aprendiendo.data.dao.CategoryDao
import com.example.aprendiendo.data.dao.ExpenseDao
import com.example.aprendiendo.data.dao.SavingGoalDao
import com.example.aprendiendo.data.entities.Category
import com.example.aprendiendo.data.entities.Expense
import com.example.aprendiendo.data.entities.SavingGoal
import java.util.Date

class ExpenseRepository(
    private val expenseDao: ExpenseDao,
    private val categoryDao: CategoryDao,
    private val savingGoalDao: SavingGoalDao
) {
    
    // Expense operations
    fun getAllExpenses(): LiveData<List<Expense>> = expenseDao.getAllExpenses()
    
    fun getExpensesByCategory(category: String): LiveData<List<Expense>> = 
        expenseDao.getExpensesByCategory(category)
    
    fun getExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<List<Expense>> =
        expenseDao.getExpensesBetweenDates(startDate, endDate)
    
    fun getTotalExpenses(): LiveData<Double?> = expenseDao.getTotalExpenses()
    
    fun getTotalExpensesByCategory(category: String): LiveData<Double?> = 
        expenseDao.getTotalExpensesByCategory(category)
    
    fun getTotalExpensesBetweenDates(startDate: Date, endDate: Date): LiveData<Double?> =
        expenseDao.getTotalExpensesBetweenDates(startDate, endDate)
    
    suspend fun insertExpense(expense: Expense) = expenseDao.insertExpense(expense)
    
    suspend fun updateExpense(expense: Expense) = expenseDao.updateExpense(expense)
    
    suspend fun deleteExpense(expense: Expense) = expenseDao.deleteExpense(expense)
    
    suspend fun deleteExpenseById(id: Long) = expenseDao.deleteExpenseById(id)
    
    // Category operations
    fun getAllCategories(): LiveData<List<Category>> = categoryDao.getAllCategories()
    
    suspend fun getCategoryByName(name: String): Category? = categoryDao.getCategoryByName(name)
    
    suspend fun insertCategory(category: Category) = categoryDao.insertCategory(category)
    
    suspend fun updateCategory(category: Category) = categoryDao.updateCategory(category)
    
    suspend fun deleteCategory(category: Category) = categoryDao.deleteCategory(category)
    
    // Saving goal operations
    fun getAllSavingGoals(): LiveData<List<SavingGoal>> = savingGoalDao.getAllSavingGoals()
    
    fun getActiveSavingGoals(): LiveData<List<SavingGoal>> = savingGoalDao.getActiveSavingGoals()
    
    suspend fun getSavingGoalById(id: Long): SavingGoal? = savingGoalDao.getSavingGoalById(id)
    
    suspend fun insertSavingGoal(savingGoal: SavingGoal) = savingGoalDao.insertSavingGoal(savingGoal)
    
    suspend fun updateSavingGoal(savingGoal: SavingGoal) = savingGoalDao.updateSavingGoal(savingGoal)
    
    suspend fun deleteSavingGoal(savingGoal: SavingGoal) = savingGoalDao.deleteSavingGoal(savingGoal)
    
    suspend fun updateCurrentAmount(id: Long, newAmount: Double) = 
        savingGoalDao.updateCurrentAmount(id, newAmount)
    
    suspend fun markGoalAsCompleted(id: Long) = savingGoalDao.markAsCompleted(id)
}