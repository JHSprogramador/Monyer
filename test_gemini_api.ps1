# Test de API Key de Gemini
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "PRUEBA DE API KEY DE GEMINI AI" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

$apiKey = "AIzaSyCyLGMKIYFBOeWluN-LbiVAEt-hYwa4nqU"
$url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=$apiKey"

Write-Host "API Key: $($apiKey.Substring(0,20))...$($apiKey.Substring($apiKey.Length-5))" -ForegroundColor Yellow
Write-Host "URL: https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent" -ForegroundColor Yellow
Write-Host ""
Write-Host "Enviando solicitud de prueba..." -ForegroundColor Yellow
Write-Host ""

$headers = @{
    "Content-Type" = "application/json"
}

$body = @{
    contents = @(
        @{
            parts = @(
                @{
                    text = "Di 'Hola, tu API key funciona correctamente!' en español"
                }
            )
        }
    )
} | ConvertTo-Json -Depth 10

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $body -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "✅ ¡ÉXITO! LA API KEY FUNCIONA CORRECTAMENTE" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "🤖 Respuesta de Gemini AI:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""
    Write-Host "=====================================================================" -ForegroundColor Green

} catch {
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host "❌ ERROR DETECTADO" -ForegroundColor Red
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host ""

    $statusCode = $_.Exception.Response.StatusCode.value__
    $statusDescription = $_.Exception.Response.StatusDescription

    Write-Host "🔴 Código de error HTTP: $statusCode" -ForegroundColor Red
    Write-Host "🔴 Descripción: $statusDescription" -ForegroundColor Red
    Write-Host ""

    # Intentar leer el cuerpo de la respuesta de error
    try {
        $reader = New-Object System.IO.StreamReader($_.Exception.Response.GetResponseStream())
        $responseBody = $reader.ReadToEnd()
        $reader.Close()

        Write-Host "📋 Detalles del error:" -ForegroundColor Yellow
        Write-Host $responseBody -ForegroundColor White
        Write-Host ""
    } catch {
        Write-Host "No se pudo leer el cuerpo del error" -ForegroundColor Yellow
    }

    Write-Host "💡 Diagnóstico:" -ForegroundColor Cyan

    if ($statusCode -eq 400) {
        Write-Host "   ⚠️  Error 400 - Solicitud incorrecta" -ForegroundColor Yellow
        Write-Host "   💡 La API key puede ser inválida o el formato de la solicitud está mal" -ForegroundColor White
    }
    elseif ($statusCode -eq 403) {
        Write-Host "   ⚠️  Error 403 - Acceso denegado" -ForegroundColor Yellow
        Write-Host "   💡 Posibles causas:" -ForegroundColor White
        Write-Host "      1. La API key no tiene permisos habilitados" -ForegroundColor White
        Write-Host "      2. La API de Gemini no está activada en tu proyecto" -ForegroundColor White
        Write-Host "      3. Hay restricciones de uso en la API key" -ForegroundColor White
        Write-Host "      4. La API key puede estar deshabilitada o revocada" -ForegroundColor White
    }
    elseif ($statusCode -eq 404) {
        Write-Host "   ⚠️  Error 404 - Recurso no encontrado" -ForegroundColor Yellow
        Write-Host "   💡 El modelo gemini-pro puede no estar disponible" -ForegroundColor White
    }
    elseif ($statusCode -eq 429) {
        Write-Host "   ⚠️  Error 429 - Demasiadas solicitudes" -ForegroundColor Yellow
        Write-Host "   💡 Has excedido el límite de solicitudes. Espera un momento." -ForegroundColor White
    }
    else {
        Write-Host "   ⚠️  Error inesperado" -ForegroundColor Yellow
    }

    Write-Host ""
    Write-Host "📝 Pasos recomendados:" -ForegroundColor Cyan
    Write-Host "   1. Ve a: https://aistudio.google.com/app/apikey" -ForegroundColor White
    Write-Host "   2. Verifica que tu API key esté activa y sin restricciones" -ForegroundColor White
    Write-Host "   3. Si es necesario, genera una nueva API key" -ForegroundColor White
    Write-Host "   4. Asegúrate de que la 'Generative Language API' esté habilitada" -ForegroundColor White
    Write-Host ""
}

Write-Host ""
Write-Host "Presiona cualquier tecla para salir..." -ForegroundColor Gray
$null = $Host.UI.RawUI.ReadKey("NoEcho,IncludeKeyDown")

