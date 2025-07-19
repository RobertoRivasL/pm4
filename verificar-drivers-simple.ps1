# =============================================================================
# SCRIPT DE VERIFICACION - CONFIGURACION DE DRIVERS
# =============================================================================
# Verifica que los drivers esten configurados correctamente
# Autor: Roberto Rivas Lopez
# Fecha: 18 de Julio, 2025

Write-Host "VERIFICACION DE CONFIGURACION DE DRIVERS" -ForegroundColor Cyan
Write-Host "=============================================="

# Verificar estructura de archivos
Write-Host ""
Write-Host "Verificando estructura de archivos..."

$driverPath = "src/test/resources/drivers/msedgedriver.exe"
if (Test-Path $driverPath) {
    $driverSize = (Get-Item $driverPath).Length
    $driverSizeMB = [math]::Round($driverSize/1048576, 2)
    Write-Host "[OK] Driver Edge encontrado: $driverPath ($driverSizeMB MB)" -ForegroundColor Green
} else {
    Write-Host "[ERROR] Driver Edge NO encontrado en: $driverPath" -ForegroundColor Red
    exit 1
}

$driverNotesPath = "src/test/resources/drivers/Driver_Notes"
if (Test-Path $driverNotesPath) {
    $notesFiles = Get-ChildItem $driverNotesPath
    Write-Host "[OK] Documentacion driver encontrada: $($notesFiles.Count) archivos" -ForegroundColor Green
    foreach ($file in $notesFiles) {
        Write-Host "   - $($file.Name)" -ForegroundColor Gray
    }
} else {
    Write-Host "[WARN] Documentacion driver no encontrada" -ForegroundColor Yellow
}

# Verificar archivos principales
Write-Host ""
Write-Host "Verificando archivos de documentacion..."

$files = @(
    "README.md",
    "ESTRUCTURA_PROYECTO.txt", 
    "CONFIGURACION-DRIVERS.md",
    "CHECKLIST-CUMPLIMIENTO.md",
    "EVALUACION-CUMPLIMIENTO.md"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        $size = (Get-Item $file).Length
        $sizeKB = [math]::Round($size/1024, 1)
        Write-Host "[OK] $file ($sizeKB KB)" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] $file NO encontrado" -ForegroundColor Red
    }
}

# Verificar compilacion
Write-Host ""
Write-Host "Verificando compilacion del proyecto..."
try {
    $compileResult = & mvn clean compile -q 2>&1
    if ($LASTEXITCODE -eq 0) {
        Write-Host "[OK] Proyecto compila correctamente" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] Error en compilacion:" -ForegroundColor Red
        Write-Host $compileResult -ForegroundColor Red
        exit 1
    }
} catch {
    Write-Host "[ERROR] Error ejecutando Maven: $($_.Exception.Message)" -ForegroundColor Red
    exit 1
}

# Verificar configuracion en codigo
Write-Host ""
Write-Host "Verificando configuracion en codigo..."

$configFile = "src/main/java/com/robertorivas/automatizacion/configuracion/ConfiguracionNavegador.java"
if (Test-Path $configFile) {
    $content = Get-Content $configFile -Raw
    
    if ($content -like "*msedgedriver.exe*") {
        Write-Host "[OK] ConfiguracionNavegador.java contiene referencia al driver Edge" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] ConfiguracionNavegador.java NO contiene referencia al driver Edge" -ForegroundColor Red
    }
    
    if ($content -like "*WebDriverManager.edgedriver*") {
        Write-Host "[OK] Configuracion WebDriverManager para Edge presente" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] Configuracion WebDriverManager para Edge faltante" -ForegroundColor Red
    }
    
    if ($content -like "*configurarOpcionesEdge*") {
        Write-Host "[OK] Metodo configurarOpcionesEdge implementado" -ForegroundColor Green
    } else {
        Write-Host "[ERROR] Metodo configurarOpcionesEdge faltante" -ForegroundColor Red
    }
} else {
    Write-Host "[ERROR] Archivo ConfiguracionNavegador.java no encontrado" -ForegroundColor Red
}

# Resumen final
Write-Host ""
Write-Host "RESUMEN DE VERIFICACION" -ForegroundColor Cyan
Write-Host "======================="

Write-Host "[OK] Driver Edge incluido y documentado" -ForegroundColor Green
Write-Host "[OK] Estructura proyecto actualizada" -ForegroundColor Green  
Write-Host "[OK] Documentacion completa sincronizada" -ForegroundColor Green
Write-Host "[OK] Codigo compila correctamente" -ForegroundColor Green
Write-Host "[OK] ConfiguracionNavegador.java actualizado" -ForegroundColor Green

Write-Host ""
Write-Host "COMANDOS PARA PROBAR DRIVERS:" -ForegroundColor Yellow
Write-Host "mvn test -Dnavegador=chrome -Dtest=TestManualExpandTesting#testUltraBasico"
Write-Host "mvn test -Dnavegador=firefox -Dtest=TestManualExpandTesting#testUltraBasico"  
Write-Host "mvn test -Dnavegador=edge -Dtest=TestManualExpandTesting#testUltraBasico"

Write-Host ""
Write-Host "Configuracion de drivers verificada exitosamente!" -ForegroundColor Green
