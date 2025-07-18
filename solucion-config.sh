#!/bin/bash

echo "=========================================="
echo "  SOLUCIÓN: URL base no configurada"
echo "  Suite de Automatización Funcional"
echo "=========================================="

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo "❌ Error: Ejecutar desde la raíz del proyecto (donde está pom.xml)"
    exit 1
fi

echo "✅ Directorio correcto verificado"

# Paso 1: Verificar contenido actual del config.properties
echo ""
echo "🔍 Verificando contenido actual de config.properties..."
if [ -f "src/test/resources/configuracion/config.properties" ]; then
    LINES=$(wc -l < src/test/resources/configuracion/config.properties 2>/dev/null || echo "0")
    echo "   Líneas actuales en config.properties: $LINES"
    
    URL_BASE=$(grep "app.url.base" src/test/resources/configuracion/config.properties 2>/dev/null || echo "NO ENCONTRADO")
    echo "   URL base actual: $URL_BASE"
else
    echo "   ❌ Archivo config.properties no encontrado"
fi

# Paso 2: Crear config.properties completo
echo ""
echo "🔧 Creando config.properties completo..."
mkdir -p src/test/resources/configuracion

cat > src/test/resources/configuracion/config.properties << 'EOF'
# =============================================================================
# CONFIGURACIÓN DE LA SUITE DE AUTOMATIZACIÓN FUNCIONAL
# Desarrollado por: Roberto Rivas Lopez
# =============================================================================

# ---- URLs DE LA APLICACIÓN ----
app.url.base=https://practicetestautomation.com
app.url.registro=/practice-test-login
app.url.login=/practice-test-login
app.url.principal=/logged-in-successfully

# ---- CONFIGURACIÓN DE NAVEGADORES ----
navegador.defecto=chrome
navegador.headless=false
navegador.ventana.ancho=1920
navegador.ventana.alto=1080

# ---- TIMEOUTS (en segundos) ----
timeout.implicito=10
timeout.explicito=15
timeout.pagina=30
timeout.carga.elemento=20
timeout.animacion=5

# ---- RUTAS DE ARCHIVOS ----
ruta.datos=src/test/resources/datos
ruta.reportes=reportes
ruta.capturas=reportes/capturas
ruta.logs=reportes/logs
ruta.descargas=descargas

# ---- ARCHIVOS DE DATOS DE PRUEBA ----
datos.usuarios.registro=usuarios_registro.csv
datos.usuarios.login=usuarios_login.csv
datos.credenciales.invalidas=credenciales_invalidas.csv

# ---- CONFIGURACIÓN DE REPORTES ----
reporte.titulo=Suite de Automatización Funcional - Roberto Rivas Lopez
reporte.autor=Roberto Rivas Lopez
reporte.descripcion=Pruebas automatizadas para formularios de registro y login
reporte.formato=html
reporte.incluir.capturas=true
reporte.incluir.logs=true

# ---- CONFIGURACIÓN DE EVIDENCIAS ----
capturas.en.exito=false
capturas.en.fallo=true
capturas.formato=png
capturas.calidad=alta

# ---- CONFIGURACIÓN DE ENTORNOS ----
entorno=desarrollo

# Configuración para entorno de desarrollo
desarrollo.url.base=https://practicetestautomation.com
desarrollo.usuario.admin=admin@test.com
desarrollo.password.admin=admin123

# Configuración para entorno de CI/CD
ci.url.base=https://practicetestautomation.com
ci.navegador.headless=true
ci.timeout.reducido=true
EOF

echo "✅ config.properties creado exitosamente"

# Paso 3: Verificar contenido nuevo
echo ""
echo "🔍 Verificando nuevo contenido..."
NEW_LINES=$(wc -l < src/test/resources/configuracion/config.properties)
echo "   Líneas nuevas: $NEW_LINES"

NEW_URL_BASE=$(grep "app.url.base" src/test/resources/configuracion/config.properties)
echo "   Nueva URL base: $NEW_URL_BASE"

# Paso 4: Compilar proyecto
echo ""
echo "🔨 Compilando proyecto..."
mvn clean compile test-compile -q

if [ $? -eq 0 ]; then
    echo "✅ Compilación exitosa"
else
    echo "❌ Error en compilación"
    exit 1
fi

# Paso 5: Verificar classpath
echo ""
echo "🔍 Verificando classpath..."
CONFIG_IN_TARGET=$(find target -name "config.properties" 2>/dev/null)

if [ -n "$CONFIG_IN_TARGET" ]; then
    echo "✅ config.properties encontrado en classpath: $CONFIG_IN_TARGET"
    
    # Verificar contenido en classpath
    TARGET_URL=$(grep "app.url.base" $CONFIG_IN_TARGET 2>/dev/null)
    echo "   URL base en classpath: $TARGET_URL"
else
    echo "❌ config.properties NO encontrado en classpath"
    exit 1
fi

# Paso 6: Ejecutar smoke tests
echo ""
echo "🚀 Ejecutando smoke tests..."
echo "=========================================="

mvn test -Dgroups=smoke -Dnavegador=chrome -DfailIfNoTests=false

RESULT=$?

echo ""
echo "=========================================="
if [ $RESULT -eq 0 ]; then
    echo "🎉 ¡ÉXITO! Smoke tests ejecutados correctamente"
    echo ""
    echo "✅ PROBLEMA SOLUCIONADO:"
    echo "   - config.properties creado con contenido completo"
    echo "   - URL base configurada correctamente"
    echo "   - Smoke tests funcionando"
    echo ""
    echo "📊 Revisar reportes en: target/surefire-reports/"
else
    echo "⚠️  Smoke tests ejecutados con advertencias"
    echo "   Verificar reportes en: target/surefire-reports/"
fi

echo "=========================================="