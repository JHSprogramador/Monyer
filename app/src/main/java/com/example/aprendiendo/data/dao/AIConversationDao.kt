package com.example.aprendiendo.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.aprendiendo.data.entities.AIConversation

@Dao
interface AIConversationDao {
    @Query("SELECT * FROM ai_conversations ORDER BY timestamp DESC")
    fun getAllConversations(): LiveData<List<AIConversation>>

    @Query("SELECT * FROM ai_conversations WHERE isFavorite = 1 ORDER BY timestamp DESC")
    fun getFavoriteConversations(): LiveData<List<AIConversation>>

    @Query("SELECT * FROM ai_conversations WHERE id = :id")
    suspend fun getConversationById(id: Long): AIConversation?

    @Insert
    suspend fun insertConversation(conversation: AIConversation): Long

    @Update
    suspend fun updateConversation(conversation: AIConversation)

    @Delete
    suspend fun deleteConversation(conversation: AIConversation)

    @Query("DELETE FROM ai_conversations")
    suspend fun deleteAllConversations()

    @Query("UPDATE ai_conversations SET isFavorite = :isFavorite WHERE id = :id")
    suspend fun toggleFavorite(id: Long, isFavorite: Boolean)
}

