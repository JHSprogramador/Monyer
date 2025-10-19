Write-Host "Consultando modelos disponibles en la API de Gemini..." -ForegroundColor Yellow
Write-Host ""

$apiKey = "AIzaSyCyLGMKIYFBOeWluN-LbiVAEt-hYwa4nqU"
$url = "https://generativelanguage.googleapis.com/v1beta/models?key=" + $apiKey

try {
    $response = Invoke-RestMethod -Uri $url -Method Get -ErrorAction Stop

    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host "MODELOS DISPONIBLES:" -ForegroundColor Green
    Write-Host "=====================================================================" -ForegroundColor Green
    Write-Host ""

    foreach ($model in $response.models) {
        if ($model.supportedGenerationMethods -contains "generateContent") {
            Write-Host "Nombre: $($model.name)" -ForegroundColor Cyan
            Write-Host "Nombre corto: $($model.displayName)" -ForegroundColor White
            Write-Host "Soporta: $($model.supportedGenerationMethods -join ', ')" -ForegroundColor Gray
            Write-Host ""
        }
    }

} catch {
    Write-Host "Error al consultar modelos: " $_.Exception.Message -ForegroundColor Red
}

