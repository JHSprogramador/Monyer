package com.example.aprendiendo.data.database

import android.content.Context
import androidx.room.*
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.aprendiendo.data.converters.DateConverter
import com.example.aprendiendo.data.dao.CategoryDao
import com.example.aprendiendo.data.dao.ExpenseDao
import com.example.aprendiendo.data.dao.SavingGoalDao
import com.example.aprendiendo.data.dao.AIConversationDao
import com.example.aprendiendo.data.entities.Category
import com.example.aprendiendo.data.entities.Expense
import com.example.aprendiendo.data.entities.SavingGoal
import com.example.aprendiendo.data.entities.AIConversation
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [Expense::class, Category::class, SavingGoal::class, AIConversation::class],
    version = 2, // Incrementada la versión de la base de datos
    exportSchema = false
)
@TypeConverters(DateConverter::class)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun expenseDao(): ExpenseDao
    abstract fun categoryDao(): CategoryDao
    abstract fun savingGoalDao(): SavingGoalDao
    abstract fun aiConversationDao(): AIConversationDao

    companion object {
        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getDatabase(context: Context): ExpenseDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_database"
                )
                .addCallback(DatabaseCallback())
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }

        private class DatabaseCallback : RoomDatabase.Callback() {
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let { database ->
                    CoroutineScope(Dispatchers.IO).launch {
                        populateDatabase(database)
                    }
                }
            }
        }

        private suspend fun populateDatabase(database: ExpenseDatabase) {
            val categoryDao = database.categoryDao()

            // Insertar categorías predeterminadas
            val defaultCategories = listOf(
                Category("Alimentación", "#FF5722", "restaurant"),
                Category("Transporte", "#2196F3", "directions_car"),
                Category("Entretenimiento", "#9C27B0", "movie"),
                Category("Compras", "#FF9800", "shopping_cart"),
                Category("Salud", "#4CAF50", "local_hospital"),
                Category("Educación", "#607D8B", "school"),
                Category("Servicios", "#FFC107", "build"),
                Category("Otros", "#795548", "category")
            )

            defaultCategories.forEach { category ->
                categoryDao.insertCategory(category)
            }
        }
    }
}
