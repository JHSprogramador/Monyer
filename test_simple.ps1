$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"
$url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=" + $apiKey

$headers = @{
    "Content-Type" = "application/json"
}

$body = '{"contents":[{"parts":[{"text":"Di hola en español"}]}]}'

try {
    $response = Invoke-RestMethod -Uri $url -Method Post -Headers $headers -Body $body

    Write-Host "===== EXITO =====" -ForegroundColor Green
    Write-Host ""
    Write-Host "Respuesta de Gemini 2.5 Flash:" -ForegroundColor Green
    Write-Host $response.candidates[0].content.parts[0].text
    Write-Host ""
    Write-Host "Tu API key funciona perfectamente!" -ForegroundColor Green

} catch {
    Write-Host "Error:" -ForegroundColor Red
    Write-Host $_.Exception.Message
}

