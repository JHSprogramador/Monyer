import com.google.ai.client.generativeai.GenerativeModel
import kotlinx.coroutines.runBlocking

fun main() = runBlocking {
    println("=".repeat(60))
    println("PRUEBA DE API KEY DE GEMINI")
    println("=".repeat(60))

    val apiKey = "AIzaSyAgS-SAxxsN8u2S-pm9VP7LzjJ4SQpc2tY"

    println("\n1. API Key configurada: ${apiKey.take(20)}...")
    println("2. Intentando conectar con Gemini AI...")

    try {
        val generativeModel = GenerativeModel(
            modelName = "gemini-pro",
            apiKey = apiKey
        )

        println("3. Modelo creado exitosamente")
        println("4. Enviando mensaje de prueba...")

        val response = generativeModel.generateContent("Di 'Hola mundo' en español")

        println("\n" + "=".repeat(60))
        println("✅ ¡ÉXITO! La API key funciona correctamente")
        println("=".repeat(60))
        println("\nRespuesta de Gemini:")
        println(response.text)
        println("\n" + "=".repeat(60))

    } catch (e: Exception) {
        println("\n" + "=".repeat(60))
        println("❌ ERROR DETECTADO")
        println("=".repeat(60))
        println("\nTipo de error: ${e.javaClass.simpleName}")
        println("Mensaje: ${e.message}")
        println("\nStack trace completo:")
        e.printStackTrace()
        println("=".repeat(60))
    }
}

