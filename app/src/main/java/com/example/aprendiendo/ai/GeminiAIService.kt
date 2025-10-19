package com.example.aprendiendo.ai

import android.util.Log
import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class GeminiAIService {

    private val TAG = "GeminiAIService"

    // Tu API key configurada
    private val apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"

    private val generativeModel = GenerativeModel(
        modelName = "gemini-2.5-flash",  // Modelo actualizado disponible
        apiKey = apiKey
    )

    // Función de prueba simple para verificar la API key
    suspend fun testConnection(): String = withContext(Dispatchers.IO) {
        return@withContext try {
            Log.d(TAG, "Iniciando prueba de conexión con Gemini AI...")

            val response = generativeModel.generateContent("Di 'Hola, tu API key funciona correctamente!' en español")

            val result = response.text ?: "No se recibió respuesta"
            Log.d(TAG, "Prueba exitosa: $result")

            "✅ CONEXIÓN EXITOSA\n\nRespuesta de Gemini:\n$result"

        } catch (e: Exception) {
            val errorMsg = when {
                e.message?.contains("API key not valid") == true ->
                    "❌ ERROR: API key no válida\n\nVerifica que tu API key sea correcta en GeminiAIService.kt"

                e.message?.contains("timeout") == true ->
                    "❌ ERROR: Tiempo de espera agotado\n\nVerifica tu conexión a Internet"

                e.message?.contains("network") == true || e.message?.contains("Unable to resolve host") == true ->
                    "❌ ERROR: Sin conexión a Internet\n\nAsegúrate de estar conectado a Internet"

                e.message?.contains("403") == true ->
                    "❌ ERROR: Acceso denegado (403)\n\nPuede que tu API key no tenga permisos o esté deshabilitada"

                else ->
                    "❌ ERROR DESCONOCIDO:\n\n${e.javaClass.simpleName}\n${e.message}\n\nStack: ${e.stackTraceToString().take(500)}"
            }

            Log.e(TAG, "Error en prueba de conexión: ${e.message}", e)
            errorMsg
        }
    }

    suspend fun generateFinancialAdvice(
        prompt: String,
        totalExpenses: Double,
        totalSaved: Double,
        totalGoals: Double,
        expensesByCategory: Map<String, Double>,
        activeGoalsCount: Int,
        completedGoalsCount: Int
    ): String = withContext(Dispatchers.IO) {
        try {
            Log.d(TAG, "Generando consejo financiero...")

            // Construir contexto financiero
            val context = buildFinancialContext(
                totalExpenses,
                totalSaved,
                totalGoals,
                expensesByCategory,
                activeGoalsCount,
                completedGoalsCount
            )

            // Crear el prompt completo con contexto
            val fullPrompt = """
                Eres un asesor financiero personal experto. Aquí está el resumen financiero del usuario:
                
                $context
                
                Pregunta del usuario: $prompt
                
                Por favor, proporciona una respuesta útil, práctica y personalizada basada en sus datos financieros.
                Mantén la respuesta concisa (máximo 300 palabras) y en español.
                Incluye consejos específicos y accionables cuando sea relevante.
            """.trimIndent()

            val response = generativeModel.generateContent(fullPrompt)
            val result = response.text ?: "No se pudo generar una respuesta. Por favor, intenta de nuevo."

            Log.d(TAG, "Consejo generado exitosamente")
            result

        } catch (e: Exception) {
            Log.e(TAG, "Error al generar consejo: ${e.message}", e)

            val errorMsg = when {
                e.message?.contains("API key") == true ->
                    "Error con la API key. Verifica tu configuración."
                e.message?.contains("network") == true ->
                    "Error de conexión. Verifica tu Internet."
                else ->
                    "Error: ${e.message}"
            }

            "❌ $errorMsg\n\nPor favor, intenta de nuevo o verifica la configuración."
        }
    }

    suspend fun askQuestion(prompt: String): String = withContext(Dispatchers.IO) {
        try {
            val response = generativeModel.generateContent(prompt)
            response.text ?: "No se pudo generar una respuesta."
        } catch (e: Exception) {
            Log.e(TAG, "Error en askQuestion: ${e.message}", e)
            "Error: ${e.message}"
        }
    }

    private fun buildFinancialContext(
        totalExpenses: Double,
        totalSaved: Double,
        totalGoals: Double,
        expensesByCategory: Map<String, Double>,
        activeGoalsCount: Int,
        completedGoalsCount: Int
    ): String {
        val context = StringBuilder()

        context.append("RESUMEN FINANCIERO:\n")
        context.append("- Total gastado: $${String.format("%.2f", totalExpenses)}\n")
        context.append("- Total ahorrado: $${String.format("%.2f", totalSaved)}\n")
        context.append("- Meta de ahorro total: $${String.format("%.2f", totalGoals)}\n")
        context.append("- Objetivos activos: $activeGoalsCount\n")
        context.append("- Objetivos completados: $completedGoalsCount\n")

        if (expensesByCategory.isNotEmpty()) {
            context.append("\nGASTOS POR CATEGORÍA:\n")
            expensesByCategory.entries
                .sortedByDescending { it.value }
                .take(5)
                .forEach { (category, amount) ->
                    val percentage = if (totalExpenses > 0) {
                        (amount / totalExpenses * 100).toInt()
                    } else 0
                    context.append("- $category: $${String.format("%.2f", amount)} ($percentage%)\n")
                }
        }

        // Calcular ratio de ahorro
        val savingsRate = if (totalGoals > 0) {
            (totalSaved / totalGoals * 100).toInt()
        } else 0

        context.append("\nPROGRESO DE AHORRO: $savingsRate%\n")

        return context.toString()
    }

    fun getSuggestedQuestions(): List<String> {
        return listOf(
            "🧪 PROBAR CONEXIÓN",
            "¿Cómo puedo reducir mis gastos?",
            "¿En qué categorías estoy gastando más?",
            "Dame consejos para alcanzar mis objetivos de ahorro",
            "¿Qué porcentaje de mis ingresos debería ahorrar?",
            "Sugerencias de inversión para principiantes",
            "¿Cómo puedo crear un presupuesto efectivo?",
            "¿Dónde puedo invertir mi dinero ahorrado?"
        )
    }
}
