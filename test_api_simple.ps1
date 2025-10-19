Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "PRUEBA DE API KEY DE GEMINI AI" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

$apiKey = "AIzaSyCyLGMKIYFBOeWluN-LbiVAEt-hYwa4nqU"
$url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + $apiKey

Write-Host "Probando API Key..." -ForegroundColor Yellow
Write-Host ""

$headers = @{
    "Content-Type" = "application/json"
}

$jsonBody = @"
{
  "contents": [
    {
      "parts": [
        {
          "text": "Di hola en español"
        }
      ]
    }
  ]
}
"@

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $jsonBody -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "EXITO - LA API KEY FUNCIONA CORRECTAMENTE" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini AI:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text
    Write-Host ""

} catch {
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host "ERROR DETECTADO" -ForegroundColor Red
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host ""

    if ($_.Exception.Response) {
        $statusCode = [int]$_.Exception.Response.StatusCode
        Write-Host "Codigo HTTP: $statusCode" -ForegroundColor Red

        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        $reader.Close()

        Write-Host ""
        Write-Host "Detalles del error:" -ForegroundColor Yellow
        Write-Host $errorBody
        Write-Host ""

        Write-Host "Diagnostico:" -ForegroundColor Cyan
        if ($statusCode -eq 400) {
            Write-Host "  Error 400: API key invalida o solicitud mal formada"
        }
        elseif ($statusCode -eq 403) {
            Write-Host "  Error 403: Acceso denegado"
            Write-Host "  - La API key no tiene permisos"
            Write-Host "  - La API de Gemini no esta activada"
            Write-Host "  - Hay restricciones en la API key"
        }
        elseif ($statusCode -eq 404) {
            Write-Host "  Error 404: Modelo no encontrado"
        }

    } else {
        Write-Host "Error: " $_.Exception.Message -ForegroundColor Red
    }

    Write-Host ""
    Write-Host "Soluciones:" -ForegroundColor Cyan
    Write-Host "1. Ve a: https://aistudio.google.com/app/apikey"
    Write-Host "2. Verifica que tu API key este activa"
    Write-Host "3. Genera una nueva si es necesario"
    Write-Host ""
}

