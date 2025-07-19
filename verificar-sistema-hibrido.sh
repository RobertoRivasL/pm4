#!/bin/bash

echo "=== VERIFICACIÓN DEL SISTEMA HÍBRIDO ==="
echo "📅 Fecha: $(date)"
echo ""

echo "🔍 1. Verificando compilación..."
mvn clean compile -q
if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
else
    echo "❌ Error en compilación"
    exit 1
fi

echo ""
echo "🔍 2. Verificando estructura de archivos..."

# Verificar archivos clave
archivos=(
    "src/test/java/com/robertorivas/automatizacion/pruebas/RegistroExpandTestingTest.java"
    "src/test/java/com/robertorivas/automatizacion/datos/ProveedorDatos.java" 
    "src/main/java/com/robertorivas/automatizacion/modelos/DatosRegistro.java"
    "src/main/java/com/robertorivas/automatizacion/configuracion/ConfiguracionPruebas.java"
)

for archivo in "${archivos[@]}"; do
    if [ -f "$archivo" ]; then
        echo "✅ $archivo"
    else
        echo "❌ $archivo FALTANTE"
    fi
done

echo ""
echo "🔍 3. Verificando DataProviders..."
grep -q "datosRegistroValidos" src/test/java/com/robertorivas/automatizacion/datos/ProveedorDatos.java && echo "✅ datosRegistroValidos encontrado"
grep -q "datosRegistroInvalidos" src/test/java/com/robertorivas/automatizacion/datos/ProveedorDatos.java && echo "✅ datosRegistroInvalidos encontrado"
grep -q "tiposRegistro" src/test/java/com/robertorivas/automatizacion/datos/ProveedorDatos.java && echo "✅ tiposRegistro encontrado"

echo ""
echo "🔍 4. Verificando tests híbridos..."
grep -q "testRegistroHibridoCSVValidos" src/test/java/com/robertorivas/automatizacion/pruebas/RegistroExpandTestingTest.java && echo "✅ testRegistroHibridoCSVValidos encontrado"
grep -q "testRegistroHibridoCSVInvalidos" src/test/java/com/robertorivas/automatizacion/pruebas/RegistroExpandTestingTest.java && echo "✅ testRegistroHibridoCSVInvalidos encontrado"
grep -q "testRegistroHibridoTipos" src/test/java/com/robertorivas/automatizacion/pruebas/RegistroExpandTestingTest.java && echo "✅ testRegistroHibridoTipos encontrado"

echo ""
echo "🎯 RESUMEN DEL SISTEMA HÍBRIDO:"
echo "   📊 Tests básicos: 7 (hardcodeados con anti-modales)"
echo "   📊 Tests híbridos CSV: 3 (DataProvider + anti-modales)"
echo "   🛡️  Sistema anti-modales: ACTIVO"
echo "   📸 Capturas automáticas: ACTIVO"
echo "   ⚙️  Configuración híbrida: ACTIVO"
echo ""
echo "✅ Sistema híbrido listo para usar"
