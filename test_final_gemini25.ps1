Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "PRUEBA FINAL CON MODELO GEMINI-2.5-FLASH" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"
$url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + $apiKey

$headers = @{
    "Content-Type" = "application/json"
}

$jsonBody = @"
{
  "contents": [
    {
      "parts": [
        {
          "text": "Di 'Hola, tu API key funciona perfectamente con Gemini 2.5 Flash!' en español"
        }
      ]
    }
  ]
}
"@

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $jsonBody -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "EXITO TOTAL - TODO FUNCIONA CORRECTAMENTE" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini 2.5 Flash:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Configuracion correcta:" -ForegroundColor Cyan
    Write-Host "  API Key: ✓ Valida y funcionando" -ForegroundColor White
    Write-Host "  Modelo: gemini-2.5-flash" -ForegroundColor White
    Write-Host "  Version API: v1beta" -ForegroundColor White
    Write-Host ""
    Write-Host "Tu Asistente de IA esta 100% listo para usar!" -ForegroundColor Green

} catch {
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host "ERROR" -ForegroundColor Red
    Write-Host "=====================================================================" -ForegroundColor Red
    Write-Host ""

    if ($_.Exception.Response) {
        $statusCode = [int]$_.Exception.Response.StatusCode
        Write-Host "Codigo HTTP: $statusCode" -ForegroundColor Red

        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        $reader.Close()

        Write-Host "Detalles:" -ForegroundColor Yellow
        Write-Host $errorBody
    } else {
        Write-Host "Error: " $_.Exception.Message -ForegroundColor Red
    }
}

