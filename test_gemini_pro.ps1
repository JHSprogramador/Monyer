Write-Host "Probando con gemini-pro..." -ForegroundColor Yellow
Write-Host ""

$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"
$url = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + $apiKey

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
    Write-Host "EXITO - TU API KEY FUNCIONA CON GEMINI-PRO" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini AI:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""
    Write-Host "El modelo correcto es: gemini-pro" -ForegroundColor Cyan

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

