#!/bin/bash

# =========================================================================
# Script Completo de Corrección de Errores de Sintaxis
# Autor: Roberto Rivas Lopez
# Descripción: Corrige TODOS los errores de sintaxis del proyecto
# =========================================================================

# Colores
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
MAGENTA='\033[0;35m'
CYAN='\033[0;36m'
NC='\033[0m'

echo -e "${CYAN}========================================${NC}"
echo -e "${CYAN} Corrector Completo del Proyecto       ${NC}"
echo -e "${CYAN} Suite de Automatización Funcional     ${NC}"
echo -e "${CYAN}========================================${NC}"
echo ""

# Verificar que estamos en el directorio correcto
if [[ ! -f "pom.xml" ]]; then
    echo -e "${RED}❌ Error: No se encontró pom.xml${NC}"
    echo "Ejecute desde la raíz del proyecto"
    exit 1
fi

# Crear backup completo
BACKUP_DIR="backup_completo_$(date +%Y%m%d_%H%M%S)"
echo -e "${YELLOW}📁 Creando backup completo en: $BACKUP_DIR${NC}"
mkdir -p "$BACKUP_DIR"
cp -r src "$BACKUP_DIR/" 2>/dev/null
echo -e "${GREEN}✅ Backup creado${NC}"
echo ""

# Función para contar llaves en un archivo
contar_llaves() {
    local archivo="$1"
    local abrir=0
    local cerrar=0
    
    # Verificar que el archivo existe y no está vacío
    if [[ ! -f "$archivo" ]] || [[ ! -s "$archivo" ]]; then
        echo "0 0"
        return
    fi
    
    while IFS= read -r linea || [[ -n "$linea" ]]; do
        # Eliminar comentarios de línea
        linea_limpia=$(echo "$linea" | sed 's|//.*$||g' | sed 's|/\*.*\*/||g')
        
        # Contar llaves
        abrir=$(( abrir + $(echo "$linea_limpia" | tr -cd '{' | wc -c) ))
        cerrar=$(( cerrar + $(echo "$linea_limpia" | tr -cd '}' | wc -c) ))
    done < "$archivo"
    
    echo "$abrir $cerrar"
}

# Función para verificar y corregir un archivo
corregir_archivo() {
    local archivo="$1"
    local nombre_archivo=$(basename "$archivo")
    
    echo -e "${BLUE}🔍 Analizando: $nombre_archivo${NC}"
    
    # Verificar que el archivo existe
    if [[ ! -f "$archivo" ]]; then
        echo -e "${RED}   ❌ Archivo no encontrado${NC}"
        return 1
    fi
    
    # Verificar que no esté vacío
    if [[ ! -s "$archivo" ]]; then
        echo -e "${YELLOW}   ⚠️  Archivo vacío${NC}"
        return 1
    fi
    
    # Contar llaves
    local conteo=($(contar_llaves "$archivo"))
    local abrir=${conteo[0]}
    local cerrar=${conteo[1]}
    local diferencia=$((abrir - cerrar))
    
    echo "   Llaves: $abrir ↔ $cerrar (diferencia: $diferencia)"
    
    if [[ $diferencia -gt 0 ]]; then
        echo -e "${YELLOW}   🔧 Corrigiendo: agregando $diferencia llave(s)${NC}"
        
        # Agregar llaves faltantes al final
        for ((i=1; i<=diferencia; i++)); do
            echo "}" >> "$archivo"
        done
        
        echo -e "${GREEN}   ✅ Corregido${NC}"
        return 0
        
    elif [[ $diferencia -lt 0 ]]; then
        echo -e "${RED}   ❌ Sobran ${diferencia#-} llave(s)${NC}"
        return 1
        
    else
        echo -e "${GREEN}   ✅ Balanceado${NC}"
        return 0
    fi
}

# Función para obtener todos los archivos Java del proyecto
obtener_archivos_java() {
    find src -name "*.java" -type f | sort
}

# Función principal de corrección
corregir_proyecto() {
    echo -e "${MAGENTA}🔄 Iniciando corrección del proyecto completo...${NC}"
    echo ""
    
    local total_archivos=0
    local archivos_corregidos=0
    local archivos_con_errores=0
    
    # Obtener todos los archivos Java
    local archivos_java=($(obtener_archivos_java))
    total_archivos=${#archivos_java[@]}
    
    echo -e "${BLUE}📊 Encontrados $total_archivos archivos Java${NC}"
    echo ""
    
    # Procesar cada archivo
    for archivo in "${archivos_java[@]}"; do
        if corregir_archivo "$archivo"; then
            if [[ $(contar_llaves "$archivo") != *"0 0"* ]]; then
                local diferencia_actual=$(( $(contar_llaves "$archivo" | cut -d' ' -f1) - $(contar_llaves "$archivo" | cut -d' ' -f2) ))
                if [[ $diferencia_actual -eq 0 ]]; then
                    archivos_corregidos=$((archivos_corregidos + 1))
                fi
            fi
        else
            archivos_con_errores=$((archivos_con_errores + 1))
        fi
        echo ""
    done
    
    # Mostrar resumen
    echo -e "${CYAN}========================================${NC}"
    echo -e "${CYAN} Resumen de Corrección                 ${NC}"
    echo -e "${CYAN}========================================${NC}"
    echo -e "${BLUE}📊 Total de archivos:    $total_archivos${NC}"
    echo -e "${GREEN}✅ Archivos corregidos:  $archivos_corregidos${NC}"
    echo -e "${RED}❌ Archivos con errores: $archivos_con_errores${NC}"
    echo ""
}

# Función para verificar compilación
verificar_compilacion() {
    echo -e "${YELLOW}🔍 Verificando compilación completa...${NC}"
    echo ""
    
    # Limpiar primero
    mvn clean >/dev/null 2>&1
    
    # Intentar compilación completa
    echo -e "${BLUE}Ejecutando: mvn clean compile test-compile${NC}"
    
    if mvn clean compile test-compile -q; then
        echo ""
        echo -e "${GREEN}🎉 ¡COMPILACIÓN COMPLETAMENTE EXITOSA!${NC}"
        echo -e "${GREEN}✅ Proyecto sin errores de sintaxis${NC}"
        return 0
    else
        echo ""
        echo -e "${RED}❌ Aún hay errores de compilación${NC}"
        echo ""
        echo -e "${YELLOW}📋 Errores restantes:${NC}"
        mvn clean compile test-compile 2>&1 | grep -E "(ERROR|error|reached end)" | head -10
        return 1
    fi
}

# Función para manejo de archivos problemáticos específicos
manejar_archivos_problematicos() {
    echo -e "${YELLOW}🔧 Revisando archivos problemáticos específicos...${NC}"
    
    # Lista de archivos que suelen tener problemas
    local archivos_problematicos=(
        "src/test/java/com/automatizacion/proyecto/pruebas/PruebaLogin.java"
        "src/test/java/com/automatizacion/proyecto/paginas/PaginaBase.java"
        "src/test/java/com/automatizacion/proyecto/base/BaseTest.java"
        "src/test/java/com/automatizacion/proyecto/datos/ModeloDatos.java"
    )
    
    for archivo in "${archivos_problematicos[@]}"; do
        if [[ -f "$archivo" ]]; then
            echo -e "${BLUE}🔍 Revisando específicamente: $(basename "$archivo")${NC}"
            
            # Verificar si el archivo termina correctamente
            local ultima_linea=$(tail -1 "$archivo")
            
            # Si la última línea no es una llave de cierre y hay diferencia de llaves
            if [[ "$ultima_linea" != "}" ]]; then
                local conteo=($(contar_llaves "$archivo"))
                local diferencia=$((${conteo[0]} - ${conteo[1]}))
                
                if [[ $diferencia -gt 0 ]]; then
                    echo -e "${YELLOW}   Agregando $diferencia llave(s) al final${NC}"
                    for ((i=1; i<=diferencia; i++)); do
                        echo "}" >> "$archivo"
                    done
                fi
            fi
        fi
    done
    echo ""
}

# Función principal
main() {
    echo -e "${CYAN}🚀 Iniciando corrección completa del proyecto...${NC}"
    echo ""
    
    # Corrección principal
    corregir_proyecto
    
    # Manejo de archivos problemáticos específicos
    manejar_archivos_problematicos
    
    # Verificar compilación
    if verificar_compilacion; then
        echo ""
        echo -e "${GREEN}========================================${NC}"
        echo -e "${GREEN} ¡PROYECTO COMPLETAMENTE CORREGIDO!    ${NC}"
        echo -e "${GREEN}========================================${NC}"
        echo ""
        echo -e "${YELLOW}📋 Siguientes pasos sugeridos:${NC}"
        echo -e "1. ${GREEN}mvn clean test${NC} - Ejecutar todas las pruebas"
        echo -e "2. ${GREEN}mvn allure:serve${NC} - Ver reportes detallados"
        echo -e "3. Continuar con implementación de casos de prueba"
        echo ""
        
        # Ofrecer eliminar backup si todo está bien
        echo -e "${YELLOW}¿Eliminar backup? El proyecto compila perfectamente (s/N):${NC}"
        read -r respuesta
        if [[ "$respuesta" =~ ^[SsYy]$ ]]; then
            rm -rf "$BACKUP_DIR"
            echo -e "${GREEN}✅ Backup eliminado${NC}"
        else
            echo -e "${YELLOW}📁 Backup mantenido en: $BACKUP_DIR${NC}"
        fi
        
    else
        echo ""
        echo -e "${YELLOW}========================================${NC}"
        echo -e "${YELLOW} CORRECCIÓN PARCIAL APLICADA           ${NC}"
        echo -e "${YELLOW}========================================${NC}"
        echo ""
        echo -e "${YELLOW}💡 El backup está disponible en: $BACKUP_DIR${NC}"
        echo -e "${YELLOW}🔄 Para revertir: rm -rf src && cp -r $BACKUP_DIR/src .${NC}"
        echo ""
        echo -e "${CYAN}📋 Opciones para continuar:${NC}"
        echo -e "1. Revisar errores manualmente"
        echo -e "2. Ejecutar nuevamente el script"
        echo -e "3. Contactar soporte técnico"
    fi
}

# Mostrar ayuda
if [[ "$1" == "--help" ]] || [[ "$1" == "-h" ]]; then
    echo "Uso: $0 [opciones]"
    echo ""
    echo "Corrige todos los errores de sintaxis del proyecto Java"
    echo ""
    echo "Opciones:"
    echo "  --help, -h     Mostrar esta ayuda"
    echo "  --dry-run      Mostrar qué archivos necesitan corrección"
    echo "  --force        Ejecutar sin confirmación"
    echo ""
    echo "El script:"
    echo "  - Crea backup completo automáticamente"
    echo "  - Corrige todos los archivos .java del proyecto"
    echo "  - Verifica compilación completa (main + test)"
    echo "  - Proporciona resumen detallado"
    exit 0
fi

# Dry run
if [[ "$1" == "--dry-run" ]]; then
    echo -e "${MAGENTA}=== MODO DRY RUN - ANÁLISIS SIN CAMBIOS ===${NC}"
    echo ""
    
    local archivos_java=($(obtener_archivos_java))
    local archivos_problematicos=0
    
    for archivo in "${archivos_java[@]}"; do
        if [[ -f "$archivo" ]]; then
            local conteo=($(contar_llaves "$archivo"))
            local diferencia=$((${conteo[0]} - ${conteo[1]}))
            
            if [[ $diferencia -ne 0 ]]; then
                echo -e "${YELLOW}⚠️  $archivo (diferencia: $diferencia)${NC}"
                archivos_problematicos=$((archivos_problematicos + 1))
            else
                echo -e "${GREEN}✅ $archivo${NC}"
            fi
        fi
    done
    
    echo ""
    echo -e "${BLUE}📊 Resumen: $archivos_problematicos archivos necesitan corrección${NC}"
    exit 0
fi

# Confirmación o ejecución directa
if [[ "$1" != "--force" ]]; then
    echo -e "${YELLOW}¿Proceder con la corrección completa del proyecto? (s/N):${NC}"
    read -r respuesta
    
    if [[ ! "$respuesta" =~ ^[SsYy]$ ]]; then
        echo -e "${BLUE}Operación cancelada${NC}"
        exit 0
    fi
fi

# Ejecutar corrección
main