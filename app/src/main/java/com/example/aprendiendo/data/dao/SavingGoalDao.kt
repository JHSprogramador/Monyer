package com.example.aprendiendo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aprendiendo.data.entities.SavingGoal

@Dao
interface SavingGoalDao {
    @Query("SELECT * FROM saving_goals ORDER BY createdDate DESC")
    fun getAllSavingGoals(): LiveData<List<SavingGoal>>

    @Query("SELECT * FROM saving_goals ORDER BY createdDate DESC")
    suspend fun getAllSavingGoalsSync(): List<SavingGoal>

    @Query("SELECT * FROM saving_goals WHERE isCompleted = 0 ORDER BY createdDate DESC")
    fun getActiveSavingGoals(): LiveData<List<SavingGoal>>

    @Query("SELECT * FROM saving_goals WHERE id = :id")
    suspend fun getSavingGoalById(id: Long): SavingGoal?

    @Insert
    suspend fun insertSavingGoal(savingGoal: SavingGoal)

    @Update
    suspend fun updateSavingGoal(savingGoal: SavingGoal)

    @Delete
    suspend fun deleteSavingGoal(savingGoal: SavingGoal)

    @Query("UPDATE saving_goals SET currentAmount = :newAmount WHERE id = :id")
    suspend fun updateCurrentAmount(id: Long, newAmount: Double)

    @Query("UPDATE saving_goals SET isCompleted = 1 WHERE id = :id")
    suspend fun markAsCompleted(id: Long)
}