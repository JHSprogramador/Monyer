import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("=".repeat(70))
    println("PRUEBA DE API KEY DE GEMINI AI")
    println("=".repeat(70))

    val apiKey = "AIzaSyCyLGMKIYFBOeWluN-LbiVAEt-hYwa4nqU"

    println("\n📍 Paso 1: Configuración")
    println("   API Key: ${apiKey.take(20)}...${apiKey.takeLast(5)}")
    println("   Modelo: gemini-pro")

    println("\n📍 Paso 2: Creando modelo GenerativeModel...")

    try {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )

        println("   ✅ Modelo creado exitosamente")

        println("\n📍 Paso 3: Enviando solicitud de prueba a Gemini...")
        println("   Pregunta: 'Di hola en español'")

        val response = generativeModel.generateContent("Di 'Hola, tu API key funciona correctamente!' en español")

        println("\n" + "=".repeat(70))
        println("✅ ¡ÉXITO! LA API KEY FUNCIONA CORRECTAMENTE")
        println("=".repeat(70))
        println("\n🤖 Respuesta de Gemini AI:")
        println(response.text)
        println("\n" + "=".repeat(70))

    } catch (e: Exception) {
        println("\n" + "=".repeat(70))
        println("❌ ERROR DETECTADO")
        println("=".repeat(70))

        println("\n🔴 Tipo de error: ${e.javaClass.simpleName}")
        println("🔴 Mensaje completo: ${e.message}")

        println("\n📋 Diagnóstico automático:")
        when {
            e.message?.contains("API key not valid", ignoreCase = true) == true -> {
                println("   ⚠️  La API key NO es válida")
                println("   💡 Soluciones:")
                println("      1. Verifica que la key esté correcta (sin espacios extra)")
                println("      2. Genera una nueva API key en: https://aistudio.google.com/app/apikey")
                println("      3. Asegúrate de copiar la key completa")
            }
            e.message?.contains("403", ignoreCase = true) == true -> {
                println("   ⚠️  Error 403 - Acceso denegado")
                println("   💡 Posibles causas:")
                println("      1. La API key no tiene permisos habilitados")
                println("      2. La API de Gemini no está activada en tu proyecto de Google Cloud")
                println("      3. Hay restricciones de uso en la API key")
                println("      4. La API key puede estar deshabilitada")
            }
            e.message?.contains("404", ignoreCase = true) == true -> {
                println("   ⚠️  Error 404 - Recurso no encontrado")
                println("   💡 El modelo gemini-pro puede no estar disponible en tu región")
            }
            e.message?.contains("timeout", ignoreCase = true) == true -> {
                println("   ⚠️  Tiempo de espera agotado")
                println("   💡 Verifica tu conexión a Internet")
            }
            e.message?.contains("Unable to resolve host", ignoreCase = true) == true -> {
                println("   ⚠️  No se puede resolver el host")
                println("   💡 Problema de conexión a Internet o DNS")
            }
            e.message?.contains("PERMISSION_DENIED", ignoreCase = true) == true -> {
                println("   ⚠️  Permiso denegado")
                println("   💡 La API key no tiene permisos para usar Gemini API")
                println("      Ve a Google Cloud Console y habilita la API")
            }
            else -> {
                println("   ⚠️  Error desconocido - ver detalles abajo")
            }
        }

        println("\n📚 Stack trace completo:")
        println("-".repeat(70))
        e.printStackTrace()
        println("-".repeat(70))

        println("\n💡 Pasos recomendados:")
        println("   1. Ve a: https://aistudio.google.com/app/apikey")
        println("   2. Verifica que tu API key esté activa")
        println("   3. Genera una nueva si es necesario")
        println("   4. Asegúrate de no tener restricciones de IP/dominio")
        println("   5. Verifica que la 'Generative Language API' esté habilitada")
    }
}

