Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host "PROBANDO NUEVA API KEY" -ForegroundColor Cyan
Write-Host "=====================================================================" -ForegroundColor Cyan
Write-Host ""

$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"
$url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + $apiKey

Write-Host "API Key: $($apiKey.Substring(0,20))..." -ForegroundColor Yellow
Write-Host "Modelo: gemini-1.5-flash" -ForegroundColor Yellow
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
          "text": "Di 'Hola, tu API key funciona perfectamente!' en español"
        }
      ]
    }
  ]
}
"@

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $jsonBody -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "EXITO - TU NUEVA API KEY FUNCIONA CORRECTAMENTE" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini AI:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "El Asistente de IA ya esta listo para usar en tu app!" -ForegroundColor Cyan

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

