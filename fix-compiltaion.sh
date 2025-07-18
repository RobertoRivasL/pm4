#!/bin/bash

# ================================================================
# Script para solucionar errores de compilación
# Desarrollado por: Roberto Rivas Lopez
# ================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
GRAY='\033[0;37m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== SOLUCIONANDO ERRORES DE COMPILACIÓN ===${NC}"

# Verificar que estamos en el directorio correcto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}Error: Ejecuta este script desde la raíz del proyecto${NC}"
    exit 1
fi

# 1. Limpiar proyecto
echo -e "${CYAN}1. Limpiando proyecto...${NC}"
if mvn clean > /dev/null 2>&1; then
    echo -e "${GREEN}  ✓ Proyecto limpiado${NC}"
else
    echo -e "${YELLOW}  ⚠️ Error limpiando proyecto${NC}"
fi

# 2. Verificar estructura de directorios
echo -e "${CYAN}2. Verificando estructura de directorios...${NC}"

declare -a directorios=(
    "src/main/java/com/robertorivas/automatizacion/utilidades"
    "src/test/java/com/robertorivas/automatizacion/pruebas"
    "src/test/resources/datos"
    "src/test/resources/configuracion"
)

for dir in "${directorios[@]}"; do
    if [ ! -d "$dir" ]; then
        mkdir -p "$dir"
        echo -e "${GREEN}  ✓ Creado: $dir${NC}"
    else
        echo -e "${GRAY}  ✓ Existe: $dir${NC}"
    fi
done

# 3. Verificar archivos críticos
echo -e "${CYAN}3. Verificando archivos críticos...${NC}"

declare -A archivos_criticos=(
    ["src/main/java/com/robertorivas/automatizacion/utilidades/GestorDatos.java"]="Archivo principal de gestión de datos"
    ["src/main/java/com/robertorivas/automatizacion/utilidades/GestorEvidencias.java"]="Archivo de gestión de evidencias"
    ["src/test/java/com/robertorivas/automatizacion/pruebas/PruebasBase.java"]="Clase base para pruebas"
    ["src/test/resources/configuracion/config.properties"]="Archivo de configuración principal"
    ["src/test/resources/configuracion/testng.xml"]="Configuración de TestNG"
)

archivos_faltantes=()

for archivo in "${!archivos_criticos[@]}"; do
    if [ ! -f "$archivo" ]; then
        echo -e "${RED}  ❌ FALTANTE: $archivo${NC}"
        echo -e "${YELLOW}     Descripción: ${archivos_criticos[$archivo]}${NC}"
        archivos_faltantes+=("$archivo")
    else
        # Verificar si el archivo está vacío
        if [ ! -s "$archivo" ]; then
            echo -e "${YELLOW}  ⚠️ VACÍO: $archivo${NC}"
            archivos_faltantes+=("$archivo")
        else
            echo -e "${GREEN}  ✓ OK: $archivo${NC}"
        fi
    fi
done

# 4. Intentar compilación
echo -e "${CYAN}4. Intentando compilación...${NC}"

if [ ${#archivos_faltantes[@]} -eq 0 ]; then
    if mvn compile > /tmp/maven_output.log 2>&1; then
        echo -e "${GREEN}  ✅ COMPILACIÓN EXITOSA${NC}"
    else
        echo -e "${RED}  ❌ COMPILACIÓN FALLÓ${NC}"
        echo -e "${YELLOW}Salida del error:${NC}"
        cat /tmp/maven_output.log | while read line; do
            echo -e "${GRAY}    $line${NC}"
        done
    fi
else
    echo -e "${YELLOW}  ⚠️ Compilación omitida debido a archivos faltantes${NC}"
fi

# 5. Diagnóstico adicional
echo -e "${CYAN}5. Diagnóstico adicional...${NC}"

# Verificar Java
if command -v java &> /dev/null; then
    java_version=$(java -version 2>&1 | head -n 1 | cut -d'"' -f2)
    echo -e "${GREEN}  ✓ Java: $java_version${NC}"
else
    echo -e "${RED}  ❌ Java no encontrado${NC}"
fi

# Verificar Maven
if command -v mvn &> /dev/null; then
    maven_version=$(mvn -version 2>&1 | head -n 1 | grep -o '[0-9]\+\.[0-9]\+\.[0-9]\+')
    echo -e "${GREEN}  ✓ Maven: $maven_version${NC}"
else
    echo -e "${RED}  ❌ Maven no encontrado${NC}"
fi

# Verificar encoding
encoding=$(locale charmap 2>/dev/null || echo "unknown")
echo -e "${CYAN}  ℹ️ Encoding del sistema: $encoding${NC}"

# 6. Resumen y recomendaciones
echo -e "\n${GREEN}=== RESUMEN ===${NC}"

if [ ${#archivos_faltantes[@]} -eq 0 ]; then
    echo -e "${GREEN}✅ Todos los archivos críticos están presentes${NC}"
else
    echo -e "${RED}❌ Archivos faltantes o vacíos:${NC}"
    for archivo in "${archivos_faltantes[@]}"; do
        echo -e "${YELLOW}   • $archivo${NC}"
    done
fi

echo -e "\n${YELLOW}=== SIGUIENTES PASOS ===${NC}"

if [ ${#archivos_faltantes[@]} -gt 0 ]; then
    echo -e "${WHITE}1. Crea o actualiza los archivos faltantes con el contenido correcto${NC}"
    echo -e "${CYAN}2. Ejecuta: mvn clean compile${NC}"
    echo -e "${WHITE}3. Si hay errores, revisa la sintaxis de Java en los archivos${NC}"
else
    echo -e "${CYAN}1. Ejecuta: mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas${NC}"
    echo -e "${CYAN}2. Si funciona, ejecuta: mvn test${NC}"
fi

# 7. Crear archivo de estructura esperada
echo -e "\n${CYAN}7. Creando archivo de referencia...${NC}"

cat > ESTRUCTURA_PROYECTO.txt << 'EOF'
=== ESTRUCTURA ESPERADA DEL PROYECTO ===

suite-automatizacion-funcional/
├── pom.xml
├── src/
│   ├── main/java/com/robertorivas/automatizacion/
│   │   ├── configuracion/
│   │   │   ├── ConfiguracionNavegador.java
│   │   │   └── ConfiguracionPruebas.java
│   │   ├── modelos/
│   │   │   ├── DatosRegistro.java
│   │   │   └── Usuario.java
│   │   ├── paginas/
│   │   │   ├── PaginaBase.java
│   │   │   ├── PaginaLogin.java
│   │   │   ├── PaginaPrincipal.java
│   │   │   └── PaginaRegistro.java
│   │   └── utilidades/
│   │       ├── GestorDatos.java
│   │       └── GestorEvidencias.java
│   └── test/
│       ├── java/com/robertorivas/automatizacion/
│       │   ├── datos/
│       │   │   └── ProveedorDatos.java
│       │   └── pruebas/
│       │       ├── PruebasBase.java
│       │       ├── PruebasIntegracion.java
│       │       ├── PruebasLogin.java
│       │       └── PruebasRegistro.java
│       └── resources/
│           ├── configuracion/
│           │   ├── config.properties
│           │   └── testng.xml
│           └── datos/
│               ├── usuarios_login_expandtesting.csv
│               ├── usuarios_registro_expandtesting.csv
│               └── credenciales_invalidas_expandtesting.csv

=== ARCHIVOS CRÍTICOS PARA COMPILACIÓN ===

1. GestorDatos.java - Manejo de datos CSV
2. GestorEvidencias.java - Capturas de pantalla
3. PruebasBase.java - Clase base para todas las pruebas
4. config.properties - Configuración de URLs y parámetros
5. testng.xml - Configuración de suites de prueba

=== COMANDOS ÚTILES ===

Limpiar y compilar:
mvn clean compile

Ejecutar prueba específica:
mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas

Ejecutar todas las pruebas:
mvn test

Ejecutar con navegador específico:
mvn test -Dnavegador=chrome

Ejecutar en modo debug:
mvn test -Ddebug.activar=true

EOF

echo -e "${GREEN}  ✓ Creado: ESTRUCTURA_PROYECTO.txt${NC}"

echo -e "\n${GREEN}🚀 Diagnóstico completado!${NC}"
echo -e "${CYAN}Revisa el archivo ESTRUCTURA_PROYECTO.txt para más detalles.${NC}"

# Limpiar archivos temporales
rm -f /tmp/maven_output.log