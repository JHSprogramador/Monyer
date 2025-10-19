package com.example.aprendiendo

import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking
import org.junit.Test

class GeminiAPITest {

    @Test
    fun testGeminiAPIKey() = runBlocking {
        println("\n" + "=".repeat(70))
        println("PRUEBA DE API KEY DE GEMINI AI")
        println("=".repeat(70))

        val apiKey = "AIzaSyAgS-SAxxsN8u2S-pm9VP7LzjJ4SQpc2tY"

        println("\n📍 Paso 1: API Key configurada")
        println("   Key: ${apiKey.take(20)}...${apiKey.takeLast(5)}")

        println("\n📍 Paso 2: Creando modelo Gemini Pro...")

        try {
            val generativeModel = GenerativeModel(
                modelName = "gemini-pro",
                apiKey = apiKey
            )

            println("   ✅ Modelo creado exitosamente")

            println("\n📍 Paso 3: Enviando solicitud de prueba...")
            println("   Pregunta: 'Di Hola mundo en español'")

            val response = generativeModel.generateContent("Di 'Hola mundo' en español")

            println("\n" + "=".repeat(70))
            println("✅ ¡ÉXITO! LA API KEY FUNCIONA CORRECTAMENTE")
            println("=".repeat(70))
            println("\n🤖 Respuesta de Gemini AI:")
            println("   ${response.text}")
            println("\n" + "=".repeat(70))

            assert(response.text != null) { "La respuesta no debe ser null" }

        } catch (e: Exception) {
            println("\n" + "=".repeat(70))
            println("❌ ERROR DETECTADO")
            println("=".repeat(70))

            println("\n🔴 Tipo de error: ${e.javaClass.simpleName}")
            println("🔴 Mensaje: ${e.message}")

            println("\n📋 Diagnóstico:")
            when {
                e.message?.contains("API key not valid", ignoreCase = true) == true -> {
                    println("   ⚠️  La API key no es válida o está mal configurada")
                    println("   💡 Solución: Verifica que la key sea correcta en Google AI Studio")
                }
                e.message?.contains("403", ignoreCase = true) == true -> {
                    println("   ⚠️  Error 403 - Acceso denegado")
                    println("   💡 Posibles causas:")
                    println("      - La API key no tiene permisos habilitados")
                    println("      - La API de Gemini no está activada en tu proyecto")
                    println("      - Restricciones de uso de la API")
                }
                e.message?.contains("404", ignoreCase = true) == true -> {
                    println("   ⚠️  Error 404 - Recurso no encontrado")
                    println("   💡 El modelo gemini-pro puede no estar disponible")
                }
                e.message?.contains("timeout", ignoreCase = true) == true -> {
                    println("   ⚠️  Tiempo de espera agotado")
                    println("   💡 Verifica tu conexión a Internet")
                }
                e.message?.contains("Unable to resolve host", ignoreCase = true) == true -> {
                    println("   ⚠️  No se puede resolver el host")
                    println("   💡 Problema de conexión a Internet o DNS")
                }
                else -> {
                    println("   ⚠️  Error desconocido")
                }
            }

            println("\n📚 Stack trace completo:")
            println("-".repeat(70))
            e.printStackTrace()
            println("-".repeat(70))

            // Re-lanzar el error para que el test falle
            throw e
        }
    }
}

