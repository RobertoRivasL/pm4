#!/bin/bash

echo "=== SOLUCIONANDO PROBLEMA TESTNG.XML ==="

# 1. Verificar archivo actual
echo "1. Verificando archivo actual..."
if [ -f "src/test/resources/configuracion/testng.xml" ]; then
    echo "Archivo existe. Tamaño:"
    ls -la src/test/resources/configuracion/testng.xml
    echo "Contenido:"
    cat src/test/resources/configuracion/testng.xml
else
    echo "❌ Archivo no existe"
fi

# 2. Crear directorio si no existe
echo "2. Creando estructura de directorios..."
mkdir -p src/test/resources/configuracion

# 3. Verificar XML con xmllint (si está disponible)
echo "3. Verificando XML..."
if command -v xmllint &> /dev/null; then
    xmllint --noout src/test/resources/configuracion/testng.xml 2>&1 || echo "❌ XML inválido"
else
    echo "xmllint no disponible, saltando validación"
fi

# 4. Alternativa: usar testng.xml desde raíz
echo "4. Alternativa - usar testng.xml desde raíz del proyecto..."
if [ -f "testng.xml" ]; then
    echo "Copiando testng.xml desde raíz..."
    cp testng.xml src/test/resources/configuracion/testng.xml
    echo "✅ Archivo copiado"
else
    echo "❌ No hay testng.xml en raíz"
fi

# 5. Verificar resultado
echo "5. Verificando resultado final..."
if [ -s "src/test/resources/configuracion/testng.xml" ]; then
    echo "✅ Archivo testng.xml corregido"
    echo "Primeras líneas:"
    head -5 src/test/resources/configuracion/testng.xml
else
    echo "❌ Archivo sigue estando vacío o no existe"
fi

echo "=== VERIFICACIÓN COMPLETADA ==="