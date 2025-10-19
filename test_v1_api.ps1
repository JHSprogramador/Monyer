Write-Host "Probando con API v1 (no beta)..." -ForegroundColor Yellow
Write-Host ""

$apiKey = "AIzaSyCyLGMKIYFBOeWluN-LbiVAEt-hYwa4nqU"
$url = "https://generativelanguage.googleapis.com/v1/models/gemini-1.5-flash:generateContent?key=" + $apiKey

$headers = @{
    "Content-Type" = "application/json"
}

$jsonBody = @"
{
  "contents": [
    {
      "parts": [
        {
          "text": "Di 'Hola, tu API key funciona correctamente!' en español"
        }
      ]
    }
  ]
}
"@

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $jsonBody -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "EXITO CON API V1" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text -ForegroundColor White
    Write-Host ""

} catch {
    Write-Host "Error con v1, probando gemini-pro..." -ForegroundColor Yellow

    $url2 = "https://generativelanguage.googleapis.com/v1/models/gemini-pro:generateContent?key=" + $apiKey

    try {
        $response2 = Invoke-RestMethod -Uri $url2 -Method Post -Headers $headers -Body $jsonBody -ErrorAction Stop

        Write-Host "=====================================================================" -ForegroundColor Green
        Write-Host "EXITO CON GEMINI-PRO EN V1" -ForegroundColor Green
        Write-Host "=====================================================================" -ForegroundColor Green
        Write-Host ""
        Write-Host "Respuesta:" -ForegroundColor Green
        Write-Host $response2.candidates[0].content.parts[0].text -ForegroundColor White

    } catch {
        if ($_.Exception.Response) {
            $statusCode = [int]$_.Exception.Response.StatusCode
            $stream = $_.Exception.Response.GetResponseStream()
            $reader = New-Object System.IO.StreamReader($stream)
            $errorBody = $reader.ReadToEnd()
            $reader.Close()

            Write-Host "Error HTTP $statusCode" -ForegroundColor Red
            Write-Host $errorBody -ForegroundColor Yellow
        }
    }
}

