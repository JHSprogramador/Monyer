Write-Host "Consultando modelos disponibles con tu API key..." -ForegroundColor Yellow
Write-Host ""

$apiKey = "AIzaSyDGnXSVqVkxy9zfFqBuUXgGHXpPOnC6FsE"

# Probar diferentes versiones de la API
$versions = @("v1beta", "v1", "v1alpha")

foreach ($version in $versions) {
    Write-Host "Probando version: $version" -ForegroundColor Cyan
    $url = "https://generativelanguage.googleapis.com/$version/models?key=$apiKey"

    try {
        $response = Invoke-RestMethod -Uri $url -Method Get -ErrorAction Stop

        Write-Host "=====================================================================" -ForegroundColor Green
        Write-Host "MODELOS DISPONIBLES EN $version" -ForegroundColor Green
        Write-Host "=====================================================================" -ForegroundColor Green
        Write-Host ""

        foreach ($model in $response.models) {
            if ($model.supportedGenerationMethods -contains "generateContent") {
                Write-Host "Nombre: $($model.name)" -ForegroundColor White
                Write-Host "  Display: $($model.displayName)" -ForegroundColor Gray
                Write-Host "  Soporta: $($model.supportedGenerationMethods -join ', ')" -ForegroundColor Gray
                Write-Host ""
            }
        }

        break

    } catch {
        Write-Host "  No disponible o error" -ForegroundColor Red
        Write-Host ""
    }
}

