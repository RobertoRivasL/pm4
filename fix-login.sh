#!/bin/bash

# Script para corregir automáticamente los errores de Duration en PaginaLogin.java
# Ejecutar desde la raíz del proyecto

PAGINA_LOGIN_FILE="src/main/java/com/robertorivas/automatizacion/paginas/PaginaLogin.java"

echo "🔧 Corrigiendo errores de Duration en PaginaLogin.java..."

# Verificar que el archivo existe
if [ ! -f "$PAGINA_LOGIN_FILE" ]; then
    echo "❌ Error: No se encuentra el archivo $PAGINA_LOGIN_FILE"
    exit 1
fi

# Hacer backup del archivo original
cp "$PAGINA_LOGIN_FILE" "${PAGINA_LOGIN_FILE}.backup"
echo "📁 Backup creado: ${PAGINA_LOGIN_FILE}.backup"

# Aplicar correcciones usando sed
echo "🔄 Aplicando correcciones..."

# Corregir pausar(número) por pausar(Duration.ofMillis(número))
sed -i 's/pausar(\([0-9]\+\))/pausar(Duration.ofMillis(\1))/g' "$PAGINA_LOGIN_FILE"

echo "✅ Correcciones aplicadas exitosamente"

# Verificar que las correcciones se aplicaron
echo "🔍 Verificando correcciones..."
if grep -q "pausar(Duration.ofMillis(" "$PAGINA_LOGIN_FILE"; then
    echo "✅ Correcciones verificadas - Duration.ofMillis encontrado"
else
    echo "⚠️  Advertencia: No se encontraron las correcciones esperadas"
fi

# Mostrar líneas corregidas
echo "📋 Líneas corregidas:"
grep -n "pausar(Duration.ofMillis(" "$PAGINA_LOGIN_FILE"

echo ""
echo "🎉 Proceso completado"
echo "💡 Ahora ejecuta: mvn clean compile"
echo "🔄 Si hay problemas, restaura con: cp ${PAGINA_LOGIN_FILE}.backup $PAGINA_LOGIN_FILE"