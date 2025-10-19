Write-Host "Probando con v1beta y gemini-pro..." -ForegroundColor Yellow
Write-Host ""

$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"
$url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-pro:generateContent?key=" + $apiKey

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
    Write-Host "EXITO - TU API KEY FUNCIONA CORRECTAMENTE" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini AI:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Version de API: v1beta" -ForegroundColor Cyan
    Write-Host "Modelo: gemini-pro" -ForegroundColor Cyan
    Write-Host ""
    Write-Host "Tu Asistente de IA esta listo para usar!" -ForegroundColor Green

} catch {
    if ($_.Exception.Response) {
        $statusCode = [int]$_.Exception.Response.StatusCode
        $stream = $_.Exception.Response.GetResponseStream()
        $reader = New-Object System.IO.StreamReader($stream)
        $errorBody = $reader.ReadToEnd()
        $reader.Close()

        Write-Host "Error HTTP $statusCode" -ForegroundColor Red
        Write-Host $errorBody
    }
}

