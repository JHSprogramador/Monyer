package com.example.aprendiendo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "ai_conversations")
data class AIConversation(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val message: String,
    val response: String,
    val timestamp: Date = Date(),
    val isFavorite: Boolean = false
)

