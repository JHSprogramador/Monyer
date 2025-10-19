# 🤖 Asistente IA Financiero - Configuración

## Configuración de la API Key de Gemini

Para que el Asistente de IA funcione, necesitas configurar tu API key de Google Gemini:

### Paso 1: Obtener tu API Key

1. Ve a [Google AI Studio](https://makersuite.google.com/app/apikey)
2. Inicia sesión con tu cuenta de Google
3. Haz clic en "Create API Key"
4. Copia la API key generada

### Paso 2: Configurar la API Key en la app

Abre el archivo:
```
app/src/main/java/com/example/aprendiendo/ai/GeminiAIService.kt
```

Busca la línea:
```kotlin
private val apiKey = "TU_API_KEY_AQUI"
```

Reemplázala con tu API key:
```kotlin
private val apiKey = "tu-api-key-real-aqui"
```

### ⚠️ Importante - Seguridad

**NO subas tu API key a repositorios públicos**. Para producción, considera:

1. Usar variables de entorno
2. Almacenar la key en `local.properties`
3. Usar Android Keystore para mayor seguridad

## 🎯 Funcionalidades del Asistente IA

El asistente puede ayudarte con:

### 📊 Análisis Financiero
- Analiza tus patrones de gastos
- Identifica áreas donde puedes ahorrar
- Proporciona estadísticas personalizadas

### 💡 Consejos Personalizados
- Recomendaciones basadas en tus datos reales
- Estrategias para alcanzar tus objetivos de ahorro
- Tips para optimizar tus finanzas

### 💰 Ideas de Inversión
- Sugerencias de inversión para principiantes
- Consejos sobre dónde invertir tu dinero ahorrado
- Estrategias según tu perfil financiero

## 📱 Cómo usar el Asistente

1. **Acceder al Asistente**: 
   - Desde el Dashboard, toca el card "🤖 Asistente IA Financiero"

2. **Hacer Preguntas**:
   - Escribe tu pregunta en el campo de texto
   - O selecciona una pregunta sugerida
   - Presiona enviar

3. **Preguntas Sugeridas**:
   - "¿Cómo puedo reducir mis gastos?"
   - "¿En qué categorías estoy gastando más?"
   - "Dame consejos para alcanzar mis objetivos de ahorro"
   - "¿Dónde puedo invertir mi dinero ahorrado?"

## 🔧 Características Técnicas

- **Modelo**: Gemini Pro de Google
- **Contexto**: Analiza tus gastos y objetivos en tiempo real
- **Respuestas**: Personalizadas según tus datos financieros
- **Interfaz**: Chat conversacional intuitivo

## 🚀 Próximas Mejoras

- [ ] Análisis de tendencias temporales
- [ ] Gráficos generados por IA
- [ ] Alertas inteligentes de gastos
- [ ] Comparación con promedios del mercado
- [ ] Planificación financiera a largo plazo

---

¿Tienes dudas? El asistente está diseñado para aprender de tus preguntas y mejorar con el tiempo.

