#!/bin/bash

# =============================================================================
# SCRIPT DE ACTUALIZACIÓN DE ARCHIVOS CSV PARA EXPANDTESTING
# Desarrollado por: Roberto Rivas Lopez
# Propósito: Actualizar credenciales para ExpandTesting
# =============================================================================

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Función para imprimir mensajes con colores
print_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

print_separator() {
    echo -e "${BLUE}========================================${NC}"
}

# Banner
clear
print_separator
echo -e "${GREEN}   ACTUALIZADOR DE CSV PARA EXPANDTESTING${NC}"
echo -e "${GREEN}   Suite de Automatización Funcional${NC}"
echo -e "${GREEN}   Desarrollado por: Roberto Rivas Lopez${NC}"
print_separator
echo ""

# Variables de rutas
BASE_DIR="$(pwd)"
DATA_DIR="src/test/resources/datos"
CONFIG_DIR="src/test/resources/configuracion"
BACKUP_DIR="backup_csv_$(date +%Y%m%d_%H%M%S)"

# Archivos a crear/actualizar
USUARIOS_LOGIN_FILE="$DATA_DIR/usuarios_login_expandtesting.csv"
CREDENCIALES_INVALIDAS_FILE="$DATA_DIR/credenciales_invalidas_expandtesting.csv"
CONFIG_FILE="$CONFIG_DIR/config.properties"

print_info "Directorio base: $BASE_DIR"
print_info "Directorio de datos: $DATA_DIR"
print_info "Directorio de backup: $BACKUP_DIR"

# Verificar que estamos en el directorio correcto del proyecto
if [ ! -f "pom.xml" ]; then
    print_error "No se encontró pom.xml. ¿Estás en el directorio raíz del proyecto?"
    print_error "Ejecuta este script desde el directorio que contiene pom.xml"
    exit 1
fi

print_success "Proyecto Maven detectado correctamente"

# Crear directorio de backup
print_info "Creando directorio de backup..."
mkdir -p "$BACKUP_DIR"

# Crear directorios necesarios si no existen
print_info "Verificando/creando directorios necesarios..."
mkdir -p "$DATA_DIR"
mkdir -p "$CONFIG_DIR"

# Función para hacer backup de un archivo si existe
backup_file() {
    local file="$1"
    if [ -f "$file" ]; then
        local backup_name="$(basename "$file").backup"
        cp "$file" "$BACKUP_DIR/$backup_name"
        print_warning "Backup creado: $BACKUP_DIR/$backup_name"
    fi
}

# Hacer backup de archivos existentes
print_info "Haciendo backup de archivos existentes..."
backup_file "$USUARIOS_LOGIN_FILE"
backup_file "$CREDENCIALES_INVALIDAS_FILE"
backup_file "$CONFIG_FILE"

# Crear archivo usuarios_login_expandtesting.csv
print_info "Creando usuarios_login_expandtesting.csv..."
cat > "$USUARIOS_LOGIN_FILE" << 'EOF'
email,password,nombre,apellido,telefono
practice,SuperSecretPassword!,Practice,User,+34600111111
practice,SuperSecretPassword!,Test,User,+34600222222
practice,SuperSecretPassword!,Demo,User,+34600333333
practice,SuperSecretPassword!,Automation,Tester,+34600444444
practice,SuperSecretPassword!,Quality,Assurance,+34600555555
practice,SuperSecretPassword!,Selenium,WebDriver,+34600666666
practice,SuperSecretPassword!,TestNG,Framework,+34600777777
practice,SuperSecretPassword!,Maven,Build,+34600888888
EOF

if [ $? -eq 0 ]; then
    print_success "Archivo usuarios_login_expandtesting.csv creado exitosamente"
    print_info "Líneas creadas: $(wc -l < "$USUARIOS_LOGIN_FILE")"
else
    print_error "Error creando usuarios_login_expandtesting.csv"
    exit 1
fi

# Crear archivo credenciales_invalidas_expandtesting.csv
print_info "Creando credenciales_invalidas_expandtesting.csv..."
cat > "$CREDENCIALES_INVALIDAS_FILE" << 'EOF'
email,password,descripcion
invaliduser,SuperSecretPassword!,Usuario que no existe en el sistema
practice,wrongpassword,Password incorrecto para usuario válido
practice,invalid,Password muy corto
wronguser,wrongpass,Ambos inválidos
,SuperSecretPassword!,Username vacío
practice,,Password vacío
,,"Ambos vacíos"
practice,short,Password demasiado corto
PRACTICE,SuperSecretPassword!,Username en mayúsculas
practice,SUPERSECRETPASSWORD!,Password en mayúsculas
usuario.con.puntos,SuperSecretPassword!,Username con puntos
practice,Pass123,Password demasiado simple
practice,password,Password sin mayúsculas ni números
test@invalid.com,SuperSecretPassword!,Email formato válido pero usuario inexistente
practice,SuperSecretPassword,Password sin signo de exclamación
EOF

if [ $? -eq 0 ]; then
    print_success "Archivo credenciales_invalidas_expandtesting.csv creado exitosamente"
    print_info "Líneas creadas: $(wc -l < "$CREDENCIALES_INVALIDAS_FILE")"
else
    print_error "Error creando credenciales_invalidas_expandtesting.csv"
    exit 1
fi

# Verificar si existe config.properties y actualizarlo
print_info "Verificando configuración en config.properties..."

if [ -f "$CONFIG_FILE" ]; then
    print_warning "config.properties existe. Actualizando configuraciones específicas..."
    
    # Crear copia temporal
    cp "$CONFIG_FILE" "$CONFIG_FILE.tmp"
    
    # Actualizar configuraciones específicas
    sed -i.bak \
        -e 's|app\.url\.base=.*|app.url.base=https://practice.expandtesting.com|' \
        -e 's|app\.url\.login=.*|app.url.login=/login|' \
        -e 's|app\.url\.principal=.*|app.url.principal=/secure|' \
        -e 's|datos\.usuarios\.login=.*|datos.usuarios.login=usuarios_login_expandtesting.csv|' \
        -e 's|datos\.credenciales\.invalidas=.*|datos.credenciales.invalidas=credenciales_invalidas_expandtesting.csv|' \
        "$CONFIG_FILE"
    
    print_success "config.properties actualizado para ExpandTesting"
else
    print_warning "config.properties no existe. Creando uno básico..."
    cat > "$CONFIG_FILE" << 'EOF'
# =============================================================================
# CONFIGURACIÓN PARA EXPANDTESTING
# Actualizado automáticamente por script
# =============================================================================

# URLs DE LA APLICACIÓN (EXPANDTESTING)
app.url.base=https://practice.expandtesting.com
app.url.registro=/register
app.url.login=/login
app.url.principal=/secure

# CONFIGURACIÓN DE NAVEGADORES
navegador.defecto=chrome
navegador.headless=false

# TIMEOUTS (en segundos)
timeout.implicito=10
timeout.explicito=15
timeout.pagina=30

# RUTAS DE ARCHIVOS
ruta.datos=src/test/resources/datos
ruta.reportes=reportes
ruta.capturas=reportes/capturas
ruta.logs=reportes/logs

# ARCHIVOS DE DATOS DE PRUEBA (EXPANDTESTING)
datos.usuarios.registro=usuarios_registro.csv
datos.usuarios.login=usuarios_login_expandtesting.csv
datos.credenciales.invalidas=credenciales_invalidas_expandtesting.csv

# CREDENCIALES VÁLIDAS PARA EXPANDTESTING
expandtesting.usuario.valido=practice
expandtesting.password.valido=SuperSecretPassword!
EOF
    print_success "config.properties básico creado"
fi

# Mostrar resumen de archivos creados
print_separator
print_success "RESUMEN DE ARCHIVOS ACTUALIZADOS:"
print_separator

echo -e "${GREEN}✅ Usuarios válidos:${NC} $USUARIOS_LOGIN_FILE"
echo -e "   - Credenciales: practice / SuperSecretPassword!"
echo -e "   - Registros: $(tail -n +2 "$USUARIOS_LOGIN_FILE" | wc -l) usuarios"

echo -e "${GREEN}✅ Credenciales inválidas:${NC} $CREDENCIALES_INVALIDAS_FILE"
echo -e "   - Registros: $(tail -n +2 "$CREDENCIALES_INVALIDAS_FILE" | wc -l) casos de prueba"

echo -e "${GREEN}✅ Configuración:${NC} $CONFIG_FILE"
echo -e "   - URL base: https://practice.expandtesting.com"
echo -e "   - Login URL: /login"
echo -e "   - Secure URL: /secure"

if [ -d "$BACKUP_DIR" ] && [ "$(ls -A $BACKUP_DIR)" ]; then
    echo -e "${YELLOW}📋 Backups creados en:${NC} $BACKUP_DIR"
    ls -la "$BACKUP_DIR"
fi

print_separator
print_success "CREDENCIALES DE REFERENCIA PARA EXPANDTESTING:"
print_separator
echo -e "${GREEN}Username:${NC} practice"
echo -e "${GREEN}Password:${NC} SuperSecretPassword!"
echo -e "${GREEN}Login URL:${NC} https://practice.expandtesting.com/login"
echo -e "${GREEN}Success URL:${NC} https://practice.expandtesting.com/secure"
echo -e "${GREEN}Success Message:${NC} 'You logged into a secure area!'"

print_separator
print_success "COMANDOS PARA PROBAR:"
print_separator
echo -e "${BLUE}# Compilar proyecto${NC}"
echo -e "mvn clean compile"
echo ""
echo -e "${BLUE}# Test básico de login${NC}"
echo -e "mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas -e"
echo ""
echo -e "${BLUE}# Test de debug (recomendado primero)${NC}"
echo -e "mvn test -Dtest=PruebasDebug#debugVerificarCredenciales -e"
echo ""
echo -e "${BLUE}# Ejecutar solo pruebas smoke${NC}"
echo -e "mvn test -Dgroups=smoke"

print_separator
print_success "¡Archivos CSV actualizados exitosamente para ExpandTesting!"
print_info "Ahora puedes ejecutar las pruebas con las credenciales correctas."
print_separator

# Verificar permisos de archivos
chmod 644 "$USUARIOS_LOGIN_FILE" "$CREDENCIALES_INVALIDAS_FILE" "$CONFIG_FILE"

# Mostrar contenido de archivos (primeras líneas para verificación)
echo ""
print_info "Vista previa de usuarios_login_expandtesting.csv:"
echo -e "${YELLOW}$(head -n 3 "$USUARIOS_LOGIN_FILE")${NC}"

echo ""
print_info "Vista previa de credenciales_invalidas_expandtesting.csv:"
echo -e "${YELLOW}$(head -n 4 "$CREDENCIALES_INVALIDAS_FILE")${NC}"

echo ""
print_success "¡Script completado exitosamente! 🚀"
echo -e "${GREEN}Ahora ejecuta: mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas -e${NC}"