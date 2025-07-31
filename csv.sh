#!/bin/bash

# ====================================================================
# SCRIPT PARA GENERAR ARCHIVOS DE DATOS DE PRUEBA
# Proyecto: Suite de Automatización Funcional
# Autor: Roberto Rivas Lopez
# Descripción: Genera archivos CSV y estructura para Excel con datos de prueba
# ====================================================================

# Colores para output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuración
DIRECTORIO_DATOS="src/test/resources/datos"
FECHA_ACTUAL=$(date +"%Y-%m-%d %H:%M:%S")

# Función para mostrar mensajes
log_info() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

log_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

log_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

log_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Función para crear directorios
crear_directorios() {
    log_info "Creando estructura de directorios..."
    
    if [ ! -d "$DIRECTORIO_DATOS" ]; then
        mkdir -p "$DIRECTORIO_DATOS"
        log_success "Directorio creado: $DIRECTORIO_DATOS"
    else
        log_info "Directorio ya existe: $DIRECTORIO_DATOS"
    fi
}

# Función para generar archivo CSV de credenciales
generar_credenciales_csv() {
    local archivo="$DIRECTORIO_DATOS/credenciales.csv"
    
    log_info "Generando archivo: credenciales.csv"
    
    cat > "$archivo" << 'EOF'
caso_prueba,descripcion,email,password,es_valido,resultado_esperado,mensaje_error
LOGIN_001,"Login exitoso con credenciales válidas",usuario.valido@test.com,Password123!,true,"Login exitoso y redirección",
LOGIN_002,"Login con email válido alternativo",admin@test.com,AdminPass456#,true,"Login exitoso",
LOGIN_003,"Login con usuario de prueba",qa.tester@test.com,QATest789$,true,"Acceso permitido",
LOGIN_004,"Usuario con caracteres especiales",test.special@tést.com,Spëcïal123!,true,"Login exitoso",
LOGIN_005,"Email con formato complejo","test+tag@sub-domain.co.uk",TestPass123#,true,"Login exitoso",
LOGIN_006,"Credenciales inválidas - email incorrecto",usuario.incorrecto@test.com,Password123!,false,"Login rechazado","Email no encontrado"
LOGIN_007,"Credenciales inválidas - password incorrecto",usuario.valido@test.com,PasswordIncorrecto,false,"Login rechazado","Contraseña incorrecta"
LOGIN_008,"Email vacío",,Password123!,false,"Error de validación","Email es obligatorio"
LOGIN_009,"Password vacío",usuario.valido@test.com,,false,"Error de validación","Contraseña es obligatoria"
LOGIN_010,"Ambos campos vacíos",,"",false,"Error de validación","Campos obligatorios vacíos"
LOGIN_011,"Email con formato inválido",email-sin-arroba.com,Password123!,false,"Error de validación","Formato de email inválido"
LOGIN_012,"Email con espacios","usuario con espacios@test.com",Password123!,false,"Error de validación","Email contiene caracteres inválidos"
LOGIN_013,"Password muy corta",usuario.valido@test.com,123,false,"Error de validación","Contraseña muy corta"
LOGIN_014,"Inyección SQL en email","admin'OR'1'='1",Password123!,false,"Error de seguridad","Caracteres no permitidos"
LOGIN_015,"Intento de XSS en password",usuario.test@test.com,"<script>alert('xss')</script>",false,"Error de seguridad","Caracteres no permitidos"
EOF

    log_success "Archivo credenciales.csv generado (15 casos de login)"
}

# Función para generar archivo CSV de usuarios adicionales
generar_usuarios_adicionales_csv() {
    local archivo="$DIRECTORIO_DATOS/usuarios_adicionales.csv"
    
    log_info "Generando archivo: usuarios_adicionales.csv"
    
    cat > "$archivo" << 'EOF'
caso_prueba,descripcion,nombre,apellido,email,password,confirmar_password,telefono,genero,pais,ciudad,fecha_nacimiento,es_valido,resultado_esperado,mensaje_error
REG_001,"Usuario completo válido",Roberto,Rivas,roberto.test@email.com,Password123!,Password123!,+56912345678,Masculino,Chile,Santiago,01/01/1990,true,"Registro exitoso",
REG_002,"Usuario mínimo válido",Ana,García,ana.garcia@test.com,SecurePass456#,SecurePass456#,,Femenino,Chile,Valparaíso,15/05/1985,true,"Registro exitoso",
REG_003,"Usuario con caracteres especiales",José María,Pérez-González,jose.maria@test.com,StrongPwd789$,StrongPwd789$,+56987654321,Masculino,España,Madrid,20/12/1975,true,"Registro exitoso",
REG_004,"Usuario internacional",John,Smith,john.smith@international.com,MySecure2024!,MySecure2024!,+1234567890,Masculino,USA,New York,10/03/1988,true,"Registro exitoso",
REG_005,"Usuario con email especial","Test User",Apellido-Compuesto,test+tag@sub-domain.co.uk,TestPass123#,TestPass123#,+44123456789,Otro,Reino Unido,Londres,25/07/1992,true,"Registro exitoso",
REG_INV_001,"Nombre vacío",,García,vacio.nombre@test.com,Password123!,Password123!,+56912345678,,,,false,,"El nombre es obligatorio"
REG_INV_002,"Apellido vacío",Roberto,,vacio.apellido@test.com,Password123!,Password123!,+56912345678,,,,false,,"El apellido es obligatorio"
REG_INV_003,"Email inválido",Roberto,Rivas,email-sin-arroba.com,Password123!,Password123!,+56912345678,,,,false,,"Formato de email inválido"
REG_INV_004,"Contraseñas no coinciden",Roberto,Rivas,no.coinciden@test.com,Password123!,DiferentePass456!,+56912345678,,,,false,,"Las contraseñas no coinciden"
REG_INV_005,"Contraseña débil",Roberto,Rivas,password.debil@test.com,123,123,+56912345678,,,,false,,"La contraseña no cumple los requisitos"
REG_INV_006,"Todos los campos vacíos",,,,,,,,,,false,,"Campos obligatorios vacíos"
REG_INV_007,"Email con espacios",Roberto,Rivas,"email con espacios@test.com",Password123!,Password123!,+56912345678,,,,false,,"Email contiene caracteres inválidos"
REG_INV_008,"Inyección XSS en nombre","<script>alert('XSS')</script>",Rivas,xss.test@test.com,Password123!,Password123!,+56912345678,,,,false,,"Caracteres no permitidos"
REG_INV_009,"Longitud excesiva en nombre","AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA",Apellido,longitud.maxima@test.com,Password123!,Password123!,+56912345678,,,,false,,"Campo excede longitud máxima"
REG_INV_010,"Teléfono inválido",Roberto,Rivas,telefono.invalido@test.com,Password123!,Password123!,telefono-invalido,,,,false,,"Formato de teléfono inválido"
EOF

    log_success "Archivo usuarios_adicionales.csv generado (15 casos de registro)"
}

# Función para generar estructura de Excel (como archivo de texto)
generar_estructura_excel() {
    local archivo="$DIRECTORIO_DATOS/usuarios_prueba_estructura.txt"
    
    log_info "Generando estructura para Excel: usuarios_prueba.xlsx"
    
    cat > "$archivo" << 'EOF'
# ====================================================================
# ESTRUCTURA PARA ARCHIVO EXCEL: usuarios_prueba.xlsx
# Instrucciones para crear el archivo Excel manualmente
# ====================================================================

## HOJA 1: Datos_Registro_Validos
Encabezados: caso_prueba | descripcion | nombre | apellido | email | password | confirmar_password | telefono | genero | pais | ciudad | fecha_nacimiento | es_valido | resultado_esperado

Datos:
REG_001 | Usuario completo válido | Roberto | Rivas | roberto.test@email.com | Password123! | Password123! | +56912345678 | Masculino | Chile | Santiago | 01/01/1990 | TRUE | Registro exitoso
REG_002 | Usuario mínimo válido | Ana | García | ana.garcia@test.com | SecurePass456# | SecurePass456# |  | Femenino | Chile | Valparaíso | 15/05/1985 | TRUE | Registro exitoso
REG_003 | Usuario con caracteres especiales | José María | Pérez-González | jose.maria@test.com | StrongPwd789$ | StrongPwd789$ | +56987654321 | Masculino | España | Madrid | 20/12/1975 | TRUE | Registro exitoso
REG_004 | Usuario internacional | John | Smith | john.smith@international.com | MySecure2024! | MySecure2024! | +1234567890 | Masculino | USA | New York | 10/03/1988 | TRUE | Registro exitoso
REG_005 | Usuario con email especial | Test User | Apellido-Compuesto | test+tag@sub-domain.co.uk | TestPass123# | TestPass123# | +44123456789 | Otro | Reino Unido | Londres | 25/07/1992 | TRUE | Registro exitoso

## HOJA 2: Datos_Registro_Invalidos
Encabezados: caso_prueba | descripcion | nombre | apellido | email | password | confirmar_password | telefono | es_valido | mensaje_error

Datos:
REG_INV_001 | Nombre vacío |  | García | vacio.nombre@test.com | Password123! | Password123! | +56912345678 | FALSE | El nombre es obligatorio
REG_INV_002 | Apellido vacío | Roberto |  | vacio.apellido@test.com | Password123! | Password123! | +56912345678 | FALSE | El apellido es obligatorio
REG_INV_003 | Email inválido | Roberto | Rivas | email-sin-arroba.com | Password123! | Password123! | +56912345678 | FALSE | Formato de email inválido
REG_INV_004 | Contraseñas no coinciden | Roberto | Rivas | no.coinciden@test.com | Password123! | DiferentePass456! | +56912345678 | FALSE | Las contraseñas no coinciden
REG_INV_005 | Contraseña débil | Roberto | Rivas | password.debil@test.com | 123 | 123 | +56912345678 | FALSE | La contraseña no cumple los requisitos
REG_INV_006 | Todos los campos vacíos |  |  |  |  |  |  | FALSE | Campos obligatorios vacíos
REG_INV_007 | Email con espacios | Roberto | Rivas | email con espacios@test.com | Password123! | Password123! | +56912345678 | FALSE | Email contiene caracteres inválidos
REG_INV_008 | Inyección XSS en nombre | <script>alert('XSS')</script> | Rivas | xss.test@test.com | Password123! | Password123! | +56912345678 | FALSE | Caracteres no permitidos
REG_INV_009 | Longitud excesiva en nombre | AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA | Apellido | longitud.maxima@test.com | Password123! | Password123! | +56912345678 | FALSE | Campo excede longitud máxima
REG_INV_010 | Teléfono inválido | Roberto | Rivas | telefono.invalido@test.com | Password123! | Password123! | telefono-invalido | FALSE | Formato de teléfono inválido

## HOJA 3: Datos_Login_Mixtos
Encabezados: caso_prueba | descripcion | email | password | es_valido | resultado_esperado | mensaje_error

Datos:
LOGIN_XLSX_001 | Login válido desde Excel | usuario.excel@test.com | ExcelPass123! | TRUE | Login exitoso | 
LOGIN_XLSX_002 | Login inválido desde Excel | usuario.inexistente@test.com | PasswordIncorrecto | FALSE | Login rechazado | Usuario no encontrado
LOGIN_XLSX_003 | Email válido password inválido | usuario.valido@test.com | PasswordMalo | FALSE | Login rechazado | Contraseña incorrecta
LOGIN_XLSX_004 | Credenciales de administrador | admin.excel@test.com | AdminExcel456# | TRUE | Login exitoso | 
LOGIN_XLSX_005 | Caso especial de caracteres | test.special@tést.com | Spëcïal123! | TRUE | Login exitoso | 

## INSTRUCCIONES PARA CREAR EL ARCHIVO EXCEL:
1. Abrir Excel o LibreOffice Calc
2. Crear un nuevo libro con 3 hojas
3. Nombrar las hojas: "Datos_Registro_Validos", "Datos_Registro_Invalidos", "Datos_Login_Mixtos"
4. Copiar los encabezados y datos en cada hoja correspondiente
5. Asegurarse de que los valores TRUE/FALSE sean booleanos
6. Guardar como "usuarios_prueba.xlsx" en la carpeta de datos
7. Verificar que las fechas estén en formato dd/MM/yyyy

EOF

    log_success "Estructura para Excel generada: usuarios_prueba_estructura.txt"
}

# Función para generar Python script que crea el Excel
generar_script_python_excel() {
    local archivo="$DIRECTORIO_DATOS/crear_excel.py"
    
    log_info "Generando script Python para crear Excel..."
    
    cat > "$archivo" << 'EOF'
#!/usr/bin/env python3
"""
Script para generar el archivo usuarios_prueba.xlsx
Requiere: pip install openpyxl
Autor: Roberto Rivas Lopez
"""

import openpyxl
from openpyxl.styles import Font
from datetime import datetime
import os

def crear_excel():
    # Crear workbook
    wb = openpyxl.Workbook()
    
    # Eliminar hoja por defecto
    wb.remove(wb.active)
    
    # Crear hoja 1: Datos válidos
    ws1 = wb.create_sheet("Datos_Registro_Validos")
    encabezados1 = ["caso_prueba", "descripcion", "nombre", "apellido", "email", "password", 
                   "confirmar_password", "telefono", "genero", "pais", "ciudad", 
                   "fecha_nacimiento", "es_valido", "resultado_esperado"]
    
    # Agregar encabezados con formato
    for col, header in enumerate(encabezados1, 1):
        cell = ws1.cell(row=1, column=col, value=header)
        cell.font = Font(bold=True)
    
    # Datos válidos
    datos_validos = [
        ["REG_001", "Usuario completo válido", "Roberto", "Rivas", "roberto.test@email.com", 
         "Password123!", "Password123!", "+56912345678", "Masculino", "Chile", "Santiago", 
         "01/01/1990", True, "Registro exitoso"],
        ["REG_002", "Usuario mínimo válido", "Ana", "García", "ana.garcia@test.com", 
         "SecurePass456#", "SecurePass456#", "", "Femenino", "Chile", "Valparaíso", 
         "15/05/1985", True, "Registro exitoso"],
        ["REG_003", "Usuario con caracteres especiales", "José María", "Pérez-González", 
         "jose.maria@test.com", "StrongPwd789$", "StrongPwd789$", "+56987654321",
         "Masculino", "España", "Madrid", "20/12/1975", True, "Registro exitoso"],
        ["REG_004", "Usuario internacional", "John", "Smith", "john.smith@international.com", 
         "MySecure2024!", "MySecure2024!", "+1234567890", "Masculino", "USA", "New York", 
         "10/03/1988", True, "Registro exitoso"],
        ["REG_005", "Usuario con email especial", "Test User", "Apellido-Compuesto", 
         "test+tag@sub-domain.co.uk", "TestPass123#", "TestPass123#", "+44123456789", 
         "Otro", "Reino Unido", "Londres", "25/07/1992", True, "Registro exitoso"]
    ]
    
    for row, data in enumerate(datos_validos, 2):
        for col, value in enumerate(data, 1):
            ws1.cell(row=row, column=col, value=value)
    
    # Crear hoja 2: Datos inválidos
    ws2 = wb.create_sheet("Datos_Registro_Invalidos")
    encabezados2 = ["caso_prueba", "descripcion", "nombre", "apellido", "email", "password", 
                   "confirmar_password", "telefono", "es_valido", "mensaje_error"]
    
    for col, header in enumerate(encabezados2, 1):
        cell = ws2.cell(row=1, column=col, value=header)
        cell.font = Font(bold=True)
    
    # Datos inválidos
    datos_invalidos = [
        ["REG_INV_001", "Nombre vacío", "", "García", "vacio.nombre@test.com", "Password123!", 
         "Password123!", "+56912345678", False, "El nombre es obligatorio"],
        ["REG_INV_002", "Apellido vacío", "Roberto", "", "vacio.apellido@test.com", "Password123!", 
         "Password123!", "+56912345678", False, "El apellido es obligatorio"],
        ["REG_INV_003", "Email inválido", "Roberto", "Rivas", "email-sin-arroba.com", "Password123!", 
         "Password123!", "+56912345678", False, "Formato de email inválido"],
        ["REG_INV_004", "Contraseñas no coinciden", "Roberto", "Rivas", "no.coinciden@test.com", 
         "Password123!", "DiferentePass456!", "+56912345678", False, "Las contraseñas no coinciden"],
        ["REG_INV_005", "Contraseña débil", "Roberto", "Rivas", "password.debil@test.com", 
         "123", "123", "+56912345678", False, "La contraseña no cumple los requisitos"],
        ["REG_INV_006", "Todos los campos vacíos", "", "", "", "", "", "", False, "Campos obligatorios vacíos"],
        ["REG_INV_007", "Email con espacios", "Roberto", "Rivas", "email con espacios@test.com", 
         "Password123!", "Password123!", "+56912345678", False, "Email contiene caracteres inválidos"],
        ["REG_INV_008", "Inyección XSS en nombre", "<script>alert('XSS')</script>", "Rivas", 
         "xss.test@test.com", "Password123!", "Password123!", "+56912345678", False, "Caracteres no permitidos"],
        ["REG_INV_009", "Longitud excesiva en nombre", "A" * 50, "Apellido", "longitud.maxima@test.com", 
         "Password123!", "Password123!", "+56912345678", False, "Campo excede longitud máxima"],
        ["REG_INV_010", "Teléfono inválido", "Roberto", "Rivas", "telefono.invalido@test.com", 
         "Password123!", "Password123!", "telefono-invalido", False, "Formato de teléfono inválido"]
    ]
    
    for row, data in enumerate(datos_invalidos, 2):
        for col, value in enumerate(data, 1):
            ws2.cell(row=row, column=col, value=value)
    
    # Crear hoja 3: Login mixtos
    ws3 = wb.create_sheet("Datos_Login_Mixtos")
    encabezados3 = ["caso_prueba", "descripcion", "email", "password", "es_valido", 
                   "resultado_esperado", "mensaje_error"]
    
    for col, header in enumerate(encabezados3, 1):
        cell = ws3.cell(row=1, column=col, value=header)
        cell.font = Font(bold=True)
    
    # Datos login mixtos
    datos_login = [
        ["LOGIN_XLSX_001", "Login válido desde Excel", "usuario.excel@test.com", "ExcelPass123!", 
         True, "Login exitoso", ""],
        ["LOGIN_XLSX_002", "Login inválido desde Excel", "usuario.inexistente@test.com", 
         "PasswordIncorrecto", False, "Login rechazado", "Usuario no encontrado"],
        ["LOGIN_XLSX_003", "Email válido password inválido", "usuario.valido@test.com", 
         "PasswordMalo", False, "Login rechazado", "Contraseña incorrecta"],
        ["LOGIN_XLSX_004", "Credenciales de administrador", "admin.excel@test.com", 
         "AdminExcel456#", True, "Login exitoso", ""],
        ["LOGIN_XLSX_005", "Caso especial de caracteres", "test.special@tést.com", 
         "Spëcïal123!", True, "Login exitoso", ""]
    ]
    
    for row, data in enumerate(datos_login, 2):
        for col, value in enumerate(data, 1):
            ws3.cell(row=row, column=col, value=value)
    
    # Ajustar ancho de columnas
    for ws in [ws1, ws2, ws3]:
        for column in ws.columns:
            max_length = 0
            column_letter = column[0].column_letter
            for cell in column:
                try:
                    if len(str(cell.value)) > max_length:
                        max_length = len(str(cell.value))
                except:
                    pass
            adjusted_width = min(max_length + 2, 50)
            ws.column_dimensions[column_letter].width = adjusted_width
    
    # Guardar archivo
    wb.save("usuarios_prueba.xlsx")
    print("✅ Archivo usuarios_prueba.xlsx creado exitosamente!")

if __name__ == "__main__":
    crear_excel()
EOF

    chmod +x "$archivo"
    log_success "Script Python generado: crear_excel.py"
}

# Función para mostrar resumen
mostrar_resumen() {
    echo ""
    log_info "=================================================="
    log_info "           RESUMEN DE ARCHIVOS GENERADOS"
    log_info "=================================================="
    echo ""
    
    echo -e "${GREEN}✅ Archivos CSV generados:${NC}"
    echo "   📄 credenciales.csv (15 casos de login)"
    echo "   📄 usuarios_adicionales.csv (15 casos de registro)"
    echo ""
    
    echo -e "${YELLOW}📋 Archivos de referencia:${NC}"
    echo "   📝 usuarios_prueba_estructura.txt (estructura para Excel)"
    echo "   🐍 crear_excel.py (script Python para generar Excel)"
    echo ""
    
    echo -e "${BLUE}📊 Estadísticas totales:${NC}"
    echo "   • 15 casos de login (5 válidos + 10 inválidos)"
    echo "   • 15 casos de registro (5 válidos + 10 inválidos)"
    echo "   • 5 casos adicionales de login para Excel"
    echo "   • Total: 35 casos de prueba"
    echo ""
    
    echo -e "${GREEN}🚀 Próximos pasos:${NC}"
    echo "   1. Revisar los archivos CSV generados"
    echo "   2. Para Excel: ejecutar 'python3 crear_excel.py' en la carpeta de datos"
    echo "   3. O crear manualmente usando usuarios_prueba_estructura.txt"
    echo "   4. Verificar que los archivos estén en: $DIRECTORIO_DATOS"
    echo ""
}

# Función principal
main() {
    echo -e "${BLUE}=====================================================================${NC}"
    echo -e "${BLUE}    GENERADOR DE ARCHIVOS DE DATOS DE PRUEBA${NC}"
    echo -e "${BLUE}    Suite de Automatización Funcional - Roberto Rivas Lopez${NC}"
    echo -e "${BLUE}=====================================================================${NC}"
    echo ""
    
    log_info "Iniciando generación de archivos de datos de prueba..."
    log_info "Fecha: $FECHA_ACTUAL"
    echo ""
    
    # Ejecutar funciones
    crear_directorios
    echo ""
    
    generar_credenciales_csv
    echo ""
    
    generar_usuarios_adicionales_csv
    echo ""
    
    generar_estructura_excel
    echo ""
    
    generar_script_python_excel
    echo ""
    
    mostrar_resumen
    
    log_success "¡Proceso completado exitosamente!"
}

# Verificar si se ejecuta directamente
if [[ "${BASH_SOURCE[0]}" == "${0}" ]]; then
    main "$@"
fi