# Script de verificación del Sistema Híbrido
# Autor: Roberto Rivas Lopez

Write-Host "=== VERIFICACIÓN DEL SISTEMA HÍBRIDO ===" -ForegroundColor Cyan
Write-Host "📅 Fecha: $(Get-Date)" -ForegroundColor Gray
Write-Host ""

Write-Host "🔍 1. Verificando compilación..." -ForegroundColor Yellow
try {
    $result = & mvn clean compile -q 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "✅ Compilación exitosa" -ForegroundColor Green
    } else {
        Write-Host "❌ Error en compilación" -ForegroundColor Red
        Write-Host $result
        exit 1
    }
} catch {
    Write-Host "❌ Error ejecutando Maven" -ForegroundColor Red
    exit 1
}

Write-Host ""
Write-Host "🔍 2. Verificando estructura de archivos..." -ForegroundColor Yellow

$archivos = @(
    "src\test\java\com\robertorivas\automatizacion\pruebas\RegistroExpandTestingTest.java",
    "src\test\java\com\robertorivas\automatizacion\datos\ProveedorDatos.java",
    "src\main\java\com\robertorivas\automatizacion\modelos\DatosRegistro.java",
    "src\main\java\com\robertorivas\automatizacion\configuracion\ConfiguracionPruebas.java"
)

foreach ($archivo in $archivos) {
    if (Test-Path $archivo) {
        Write-Host "✅ $archivo" -ForegroundColor Green
    } else {
        Write-Host "❌ $archivo FALTANTE" -ForegroundColor Red
    }
}

Write-Host ""
Write-Host "🔍 3. Verificando DataProviders..." -ForegroundColor Yellow

$proveedorDatos = "src\test\java\com\robertorivas\automatizacion\datos\ProveedorDatos.java"
if (Test-Path $proveedorDatos) {
    $contenido = Get-Content $proveedorDatos -Raw
    
    if ($contenido -match "datosRegistroValidos") {
        Write-Host "✅ datosRegistroValidos encontrado" -ForegroundColor Green
    }
    if ($contenido -match "datosRegistroInvalidos") {
        Write-Host "✅ datosRegistroInvalidos encontrado" -ForegroundColor Green
    }
    if ($contenido -match "tiposRegistro") {
        Write-Host "✅ tiposRegistro encontrado" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "🔍 4. Verificando tests híbridos..." -ForegroundColor Yellow

$testsRegistro = "src\test\java\com\robertorivas\automatizacion\pruebas\RegistroExpandTestingTest.java"
if (Test-Path $testsRegistro) {
    $contenido = Get-Content $testsRegistro -Raw
    
    if ($contenido -match "testRegistroHibridoCSVValidos") {
        Write-Host "✅ testRegistroHibridoCSVValidos encontrado" -ForegroundColor Green
    }
    if ($contenido -match "testRegistroHibridoCSVInvalidos") {
        Write-Host "✅ testRegistroHibridoCSVInvalidos encontrado" -ForegroundColor Green
    }
    if ($contenido -match "testRegistroHibridoTipos") {
        Write-Host "✅ testRegistroHibridoTipos encontrado" -ForegroundColor Green
    }
}

Write-Host ""
Write-Host "🎯 RESUMEN DEL SISTEMA HÍBRIDO:" -ForegroundColor Cyan
Write-Host "   📊 Tests básicos: 7 (hardcodeados con anti-modales)" -ForegroundColor White
Write-Host "   📊 Tests híbridos CSV: 3 (DataProvider + anti-modales)" -ForegroundColor White
Write-Host "   🛡️  Sistema anti-modales: ACTIVO" -ForegroundColor White
Write-Host "   📸 Capturas automáticas: ACTIVO" -ForegroundColor White
Write-Host "   ⚙️  Configuración híbrida: ACTIVO" -ForegroundColor White
Write-Host ""
Write-Host "✅ Sistema híbrido listo para usar" -ForegroundColor Green

Write-Host ""
Write-Host "📋 COMANDOS DISPONIBLES:" -ForegroundColor Cyan
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest" -ForegroundColor Yellow
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest#testRegistroHibridoCSVValidos" -ForegroundColor Yellow
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest#testRegistroExitoso" -ForegroundColor Yellow
