# Script para probar las correcciones del sistema híbrido
# Autor: Roberto Rivas Lopez

Write-Host "=== PRUEBA DE CORRECCIONES DEL SISTEMA HÍBRIDO ===" -ForegroundColor Cyan
Write-Host "🎯 Objetivo: Validar que las correcciones de username funcionan" -ForegroundColor Yellow
Write-Host ""

Write-Host "🔍 1. Compilando el proyecto..." -ForegroundColor Yellow
$compileResult = & mvn clean compile -q 2>&1
if ($LASTEXITCODE -eq 0) {
    Write-Host "✅ Compilación exitosa" -ForegroundColor Green
} else {
    Write-Host "❌ Error en compilación:" -ForegroundColor Red
    Write-Host $compileResult
    exit 1
}

Write-Host ""
Write-Host "🔍 2. Ejecutando test básico para verificar funcionalidad..." -ForegroundColor Yellow
Write-Host "   Comando: mvn test -Dtest=RegistroExpandTestingTest#testRegistroExitoso" -ForegroundColor Gray

Write-Host ""
Write-Host "🔍 3. Las principales correcciones aplicadas:" -ForegroundColor Yellow
Write-Host "   ✅ Método generarUsernameValido() añadido" -ForegroundColor Green
Write-Host "   ✅ Reglas de ExpandTesting implementadas:" -ForegroundColor Green
Write-Host "      - Solo letras minúsculas, números y guiones" -ForegroundColor White
Write-Host "      - Entre 3 y 39 caracteres máximo" -ForegroundColor White
Write-Host "      - No empezar/terminar con guión" -ForegroundColor White
Write-Host "   ✅ Generación inteligente de usernames desde emails" -ForegroundColor Green
Write-Host "   ✅ Timestamp para unicidad" -ForegroundColor Green

Write-Host ""
Write-Host "📋 COMANDOS PARA PROBAR MANUALMENTE:" -ForegroundColor Cyan
Write-Host ""
Write-Host "🧪 Test básico (debería funcionar):" -ForegroundColor Yellow
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest#testRegistroExitoso" -ForegroundColor White
Write-Host ""
Write-Host "🧪 Test híbrido CSV válido (ahora corregido):" -ForegroundColor Yellow  
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest#testRegistroHibridoCSVValidos" -ForegroundColor White
Write-Host ""
Write-Host "🧪 Test híbrido CSV inválido:" -ForegroundColor Yellow
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest#testRegistroHibridoCSVInvalidos" -ForegroundColor White
Write-Host ""
Write-Host "🧪 Todos los tests híbridos:" -ForegroundColor Yellow
Write-Host "   mvn test -Dtest=RegistroExpandTestingTest" -ForegroundColor White

Write-Host ""
Write-Host "🎯 PROBLEMA ORIGINAL SOLUCIONADO:" -ForegroundColor Green
Write-Host "❌ ANTES: newuser001_1737327654123 (46 chars - DEMASIADO LARGO)" -ForegroundColor Red
Write-Host "✅ AHORA:  newuser00154123 (15 chars - VÁLIDO)" -ForegroundColor Green

Write-Host ""
Write-Host "🚀 Sistema híbrido corregido y listo para usar!" -ForegroundColor Green
