#!/bin/bash

# ================================================================
# Script para actualizar archivos CSV para ExpandTesting
# Desarrollado por: Roberto Rivas Lopez
# Curso: Automatización de Pruebas
# Plataforma: https://practice.expandtesting.com/
# ================================================================

# Configuración
DATOS_PATH="src/test/resources/datos"
BACKUP_PATH="backup_csv_$(date +%Y%m%d_%H%M%S)"

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
CYAN='\033[0;36m'
GRAY='\033[0;37m'
WHITE='\033[1;37m'
NC='\033[0m' # No Color

echo -e "${GREEN}=== ACTUALIZANDO CSV PARA EXPANDTESTING ===${NC}"
echo -e "${CYAN}Plataforma: https://practice.expandtesting.com/${NC}"

# Verificar que estamos en el directorio correcto del proyecto
if [ ! -f "pom.xml" ]; then
    echo -e "${RED}Error: Ejecuta este script desde la raíz del proyecto (donde está pom.xml)${NC}"
    exit 1
fi

# Crear directorio de datos si no existe
if [ ! -d "$DATOS_PATH" ]; then
    echo -e "${YELLOW}Creando directorio: $DATOS_PATH${NC}"
    mkdir -p "$DATOS_PATH"
fi

# Crear backup de archivos existentes
echo -e "${YELLOW}Creando backup en: $BACKUP_PATH${NC}"
mkdir -p "$BACKUP_PATH"

# Backup de archivos existentes
declare -a archivos_backup=("usuarios_login.csv" "usuarios_registro.csv" "credenciales_invalidas.csv")
for archivo in "${archivos_backup[@]}"; do
    if [ -f "$DATOS_PATH/$archivo" ]; then
        cp "$DATOS_PATH/$archivo" "$BACKUP_PATH/"
        echo -e "${GRAY}  ✓ Backup: $archivo${NC}"
    fi
done

echo -e "\n${GREEN}=== CREANDO ARCHIVOS CSV PARA EXPANDTESTING ===${NC}"

# 1. USUARIOS LOGIN EXPANDTESTING
echo -e "${CYAN}1. Creando usuarios_login_expandtesting.csv...${NC}"
cat > "$DATOS_PATH/usuarios_login_expandtesting.csv" << 'EOF'
username,password,nombre,apellido,descripcion
practice,SuperSecretPassword!,Practice,User,Usuario principal válido para ExpandTesting
practice,SuperSecretPassword!,Test,User,Usuario de prueba principal (duplicado para data-driven)
practice,SuperSecretPassword!,Demo,User,Usuario demo para testing
practice,SuperSecretPassword!,Automation,Tester,Usuario para automatización
practice,SuperSecretPassword!,Quality,Assurance,Usuario QA
practice,SuperSecretPassword!,Selenium,Driver,Usuario Selenium
practice,SuperSecretPassword!,TestNG,Framework,Usuario TestNG
practice,SuperSecretPassword!,Maven,Build,Usuario Maven
practice,SuperSecretPassword!,Java,Developer,Usuario Java
practice,SuperSecretPassword!,WebDriver,User,Usuario WebDriver
EOF
echo -e "${GREEN}  ✓ Creado: usuarios_login_expandtesting.csv${NC}"

# 2. CREDENCIALES INVÁLIDAS EXPANDTESTING
echo -e "${CYAN}2. Creando credenciales_invalidas_expandtesting.csv...${NC}"
cat > "$DATOS_PATH/credenciales_invalidas_expandtesting.csv" << 'EOF'
username,password,descripcion,error_esperado
wrongUser,SuperSecretPassword!,Username incorrecto con password válido,Invalid username.
invalidUser,SuperSecretPassword!,Username que no existe,Invalid username.
testUser,SuperSecretPassword!,Username de prueba inexistente,Invalid username.
fakeUser,SuperSecretPassword!,Username falso,Invalid username.
practice,WrongPassword,Username válido con password incorrecto,Invalid password.
practice,InvalidPass123,Password incorrecto formato válido,Invalid password.
practice,wrongpassword,Password incorrecto en minúsculas,Invalid password.
practice,WRONGPASSWORD,Password incorrecto en mayúsculas,Invalid password.
practice,123456,Password numérico incorrecto,Invalid password.
practice,password,Password común incorrecto,Invalid password.
practice,,Username válido con password vacío,All fields are required.
,SuperSecretPassword!,Username vacío con password válido,All fields are required.
,,Ambos campos vacíos,All fields are required.
wrongUser,WrongPassword,Ambos campos incorrectos,Invalid username.
user@test.com,SuperSecretPassword!,Email como username,Invalid username.
admin,SuperSecretPassword!,Username admin inexistente,Invalid username.
root,SuperSecretPassword!,Username root inexistente,Invalid username.
practice,12345,Password muy corto,Invalid password.
practice,Pass,Password demasiado corto,Invalid password.
practice,super secret password,Password con espacios,Invalid password.
EOF
echo -e "${GREEN}  ✓ Creado: credenciales_invalidas_expandtesting.csv${NC}"

# 3. USUARIOS REGISTRO EXPANDTESTING
echo -e "${CYAN}3. Creando usuarios_registro_expandtesting.csv...${NC}"
cat > "$DATOS_PATH/usuarios_registro_expandtesting.csv" << 'EOF'
username,password,confirmPassword,descripcion,caso_prueba
newuser001,TestPassword123,TestPassword123,Registro exitoso con datos válidos,successful_registration
testuser002,SecurePass456,SecurePass456,Usuario de prueba válido,successful_registration
automation003,AutoPass789,AutoPass789,Usuario para automatización,successful_registration
selenium004,WebDriver123,WebDriver123,Usuario Selenium válido,successful_registration
quality005,QATest456,QATest456,Usuario QA válido,successful_registration
demo006,DemoPass789,DemoPass789,Usuario demo válido,successful_registration
expand007,TestingPass123,TestingPass123,Usuario ExpandTesting válido,successful_registration
practice008,ValidPass456,ValidPass456,Usuario practice válido,successful_registration
robot009,FrameworkPass789,FrameworkPass789,Usuario robot válido,successful_registration
maven010,BuildPass123,BuildPass123,Usuario maven válido,successful_registration
,ValidPassword123,ValidPassword123,Username vacío para test negativo,missing_username
validuser011,,ValidPassword123,Password vacío para test negativo,missing_password
validuser012,TestPassword123,,Confirm password vacío para test negativo,missing_password
validuser013,Password123,DifferentPass456,Passwords que no coinciden,passwords_not_match
validuser014,ValidPass123,InvalidPass789,Passwords diferentes,passwords_not_match
validuser015,TestPass456,WrongPass123,Contraseñas no coinciden,passwords_not_match
EOF
echo -e "${GREEN}  ✓ Creado: usuarios_registro_expandtesting.csv${NC}"

# 4. ACTUALIZAR ARCHIVOS ORIGINALES (opcional, mantener compatibilidad)
echo -e "\n${GREEN}=== ACTUALIZANDO ARCHIVOS ORIGINALES ===${NC}"

# Actualizar usuarios_login.csv original
echo -e "${CYAN}4. Actualizando usuarios_login.csv...${NC}"
cp "$DATOS_PATH/usuarios_login_expandtesting.csv" "$DATOS_PATH/usuarios_login.csv"
echo -e "${GREEN}  ✓ Actualizado: usuarios_login.csv${NC}"

# Actualizar credenciales_invalidas.csv original
echo -e "${CYAN}5. Actualizando credenciales_invalidas.csv...${NC}"
cp "$DATOS_PATH/credenciales_invalidas_expandtesting.csv" "$DATOS_PATH/credenciales_invalidas.csv"
echo -e "${GREEN}  ✓ Actualizado: credenciales_invalidas.csv${NC}"

# Actualizar usuarios_registro.csv original
echo -e "${CYAN}6. Actualizando usuarios_registro.csv...${NC}"
cp "$DATOS_PATH/usuarios_registro_expandtesting.csv" "$DATOS_PATH/usuarios_registro.csv"
echo -e "${GREEN}  ✓ Actualizado: usuarios_registro.csv${NC}"

# Resumen
echo -e "\n${GREEN}=== RESUMEN DE ACTUALIZACIÓN ===${NC}"
echo -e "${YELLOW}✓ Backup creado en: $BACKUP_PATH${NC}"
echo -e "${GREEN}✓ Archivos CSV actualizados para ExpandTesting:${NC}"
echo -e "${GRAY}  - usuarios_login_expandtesting.csv (10 registros)${NC}"
echo -e "${GRAY}  - credenciales_invalidas_expandtesting.csv (20 registros)${NC}"
echo -e "${GRAY}  - usuarios_registro_expandtesting.csv (16 registros)${NC}"
echo -e "${GRAY}  - usuarios_login.csv (actualizado)${NC}"
echo -e "${GRAY}  - credenciales_invalidas.csv (actualizado)${NC}"
echo -e "${GRAY}  - usuarios_registro.csv (actualizado)${NC}"

echo -e "\n${CYAN}=== CREDENCIALES PARA EXPANDTESTING ===${NC}"
echo -e "${WHITE}Username válido: practice${NC}"
echo -e "${WHITE}Password válido: SuperSecretPassword!${NC}"
echo -e "${WHITE}URL Login: https://practice.expandtesting.com/login${NC}"
echo -e "${WHITE}URL Register: https://practice.expandtesting.com/register${NC}"

echo -e "\n${YELLOW}=== SIGUIENTE PASO ===${NC}"
echo -e "${WHITE}Ejecuta las pruebas con:${NC}"
echo -e "${CYAN}mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas${NC}"

echo -e "\n${GREEN}¡Actualización completada exitosamente! 🚀${NC}"

# Mostrar archivos creados
echo -e "\n${YELLOW}Archivos en $DATOS_PATH:${NC}"
ls -la "$DATOS_PATH"/*.csv | while read line; do
    echo -e "${GRAY}  $line${NC}"
done