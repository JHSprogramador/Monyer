package com.example.aprendiendo.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.aprendiendo.ai.GeminiAIService
import com.example.aprendiendo.data.database.ExpenseDatabase
import com.example.aprendiendo.data.entities.AIConversation
import kotlinx.coroutines.launch

data class ChatMessage(
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

class AIAssistantViewModel(application: Application) : AndroidViewModel(application) {

    private val aiService = GeminiAIService()
    private val expenseDao = ExpenseDatabase.getDatabase(application).expenseDao()
    private val savingGoalDao = ExpenseDatabase.getDatabase(application).savingGoalDao()
    private val conversationDao = ExpenseDatabase.getDatabase(application).aiConversationDao()

    private val _chatMessages = MutableLiveData<List<ChatMessage>>(emptyList())
    val chatMessages: LiveData<List<ChatMessage>> = _chatMessages

    private val _isLoading = MutableLiveData<Boolean>(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    // LiveData para ver el historial de conversaciones guardadas
    val savedConversations: LiveData<List<AIConversation>> = conversationDao.getAllConversations()

    init {
        // Mensaje de bienvenida
        addMessage(
            ChatMessage(
                text = "¡Hola! Soy tu asistente financiero personal con IA. Puedo ayudarte a analizar tus gastos, " +
                        "sugerir formas de ahorrar, y darte ideas de inversión basadas en tu situación financiera. " +
                        "\n\n💡 Tip: Usa el botón '🧪 PROBAR CONEXIÓN' para verificar que tu API key funcione correctamente.\n\n" +
                        "Todas tus conversaciones se guardan automáticamente para que puedas consultarlas después.\n\n" +
                        "¿En qué puedo ayudarte hoy?",
                isUser = false
            )
        )
    }

    fun sendMessage(userMessage: String) {
        if (userMessage.isBlank()) return

        // Verificar si es el comando de prueba
        if (userMessage.contains("PROBAR CONEXIÓN", ignoreCase = true) ||
            userMessage.contains("🧪", ignoreCase = false)) {
            testConnection()
            return
        }

        // Agregar mensaje del usuario
        addMessage(ChatMessage(text = userMessage, isUser = true))

        _isLoading.value = true

        viewModelScope.launch {
            try {
                // Obtener datos financieros
                val expenses = expenseDao.getAllExpensesSync()
                val savingGoals = savingGoalDao.getAllSavingGoalsSync()

                val totalExpenses = expenses.sumOf { it.amount }
                val totalSaved = savingGoals.sumOf { it.currentAmount }
                val totalGoals = savingGoals.sumOf { it.targetAmount }

                val expensesByCategory = expenses
                    .groupBy { it.category }
                    .mapValues { entry -> entry.value.sumOf { it.amount } }

                val activeGoalsCount = savingGoals.count { !it.isCompleted }
                val completedGoalsCount = savingGoals.count { it.isCompleted }

                // Generar respuesta con IA
                val aiResponse = aiService.generateFinancialAdvice(
                    prompt = userMessage,
                    totalExpenses = totalExpenses,
                    totalSaved = totalSaved,
                    totalGoals = totalGoals,
                    expensesByCategory = expensesByCategory,
                    activeGoalsCount = activeGoalsCount,
                    completedGoalsCount = completedGoalsCount
                )

                // Agregar respuesta de la IA
                addMessage(ChatMessage(text = aiResponse, isUser = false))

                // GUARDAR LA CONVERSACIÓN EN LA BASE DE DATOS
                saveConversationToDatabase(userMessage, aiResponse)

            } catch (e: Exception) {
                _errorMessage.value = "Error al procesar tu pregunta: ${e.message}"
                addMessage(
                    ChatMessage(
                        text = "Lo siento, hubo un error al procesar tu pregunta.\n\nError: ${e.message}\n\nPor favor, intenta de nuevo o usa '🧪 PROBAR CONEXIÓN' para diagnosticar el problema.",
                        isUser = false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    private suspend fun saveConversationToDatabase(message: String, response: String) {
        try {
            val conversation = AIConversation(
                message = message,
                response = response
            )
            conversationDao.insertConversation(conversation)
        } catch (e: Exception) {
            // Silenciosamente falla si hay error al guardar
            android.util.Log.e("AIAssistantViewModel", "Error al guardar conversación: ${e.message}")
        }
    }

    private fun testConnection() {
        addMessage(ChatMessage(text = "🧪 Probando conexión con Gemini AI...", isUser = true))

        _isLoading.value = true

        viewModelScope.launch {
            try {
                val result = aiService.testConnection()
                addMessage(ChatMessage(text = result, isUser = false))
            } catch (e: Exception) {
                addMessage(
                    ChatMessage(
                        text = "❌ Error en la prueba de conexión:\n\n${e.message}",
                        isUser = false
                    )
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun getSuggestedQuestions(): List<String> {
        return aiService.getSuggestedQuestions()
    }

    fun clearChat() {
        _chatMessages.value = listOf(
            ChatMessage(
                text = "Chat reiniciado. ¿En qué más puedo ayudarte?",
                isUser = false
            )
        )
    }

    fun deleteConversation(conversation: AIConversation) {
        viewModelScope.launch {
            conversationDao.deleteConversation(conversation)
        }
    }

    fun toggleFavorite(conversationId: Long, isFavorite: Boolean) {
        viewModelScope.launch {
            conversationDao.toggleFavorite(conversationId, isFavorite)
        }
    }

    private fun addMessage(message: ChatMessage) {
        val currentMessages = _chatMessages.value.orEmpty().toMutableList()
        currentMessages.add(message)
        _chatMessages.value = currentMessages
    }

    fun clearError() {
        _errorMessage.value = null
    }
}
