package com.example.aprendiendo.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey
    val name: String,
    val color: String = "#2196F3",
    val icon: String = "shopping_cart"
)