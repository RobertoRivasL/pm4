# Suite de Automatización Funcional - ExpandTesting

**Desarrollado por:** Roberto Rivas Lopez  
**Curso:** Automatización de Pruebas  
**Tecnología:** Java 17 + Selenium WebDriver + TestNG + Maven  
**Sitio de Pruebas:** https://practice.expandtesting.com/

## 📋 Descripción del Proyecto

Suite completa de automatización funcional enfocada en la validación de formularios de registro y login utilizando **ExpandTesting**, una plataforma diseñada específicamente para testing automation. Este proyecto implementa las mejores prácticas de automatización de pruebas, incluyendo el patrón Page Object Model (POM), Data Driven Testing, y generación de reportes avanzados.

### 🎯 Objetivos Actualizados

1. **Validación del formulario de registro**: Campos obligatorios, passwords coincidentes, mensajes de error específicos de ExpandTesting
2. **Validación de inicio de sesión**: Credenciales válidas/inválidas con mensajes específicos de la plataforma
3. **Generación de evidencias**: Capturas de pantalla, logs, reportes HTML detallados
4. **Cross-browser testing**: Compatibilidad con Chrome, Firefox y Edge
5. **Data Driven Testing**: Pruebas con múltiples conjuntos de datos específicos para ExpandTesting

## 🌐 URLs y Casos de Prueba de ExpandTesting

### URLs Principales
- **Base URL**: https://practice.expandtesting.com/
- **Login**: https://practice.expandtesting.com/login
- **Register**: https://practice.expandtesting.com/register
- **Secure Area**: https://practice.expandtesting.com/secure

### Casos de Prueba de Login

#### ✅ Test Case 1: Successful Login
- **Username**: `practice`
- **Password**: `SuperSecretPassword!`
- **Resultado esperado**: Redirección a `/secure` con mensaje "You logged into a secure area!"

#### ❌ Test Case 2: Invalid Username
- **Username**: `wrongUser` (o cualquier username incorrecto)
- **Password**: `SuperSecretPassword!`
- **Error esperado**: "Invalid username."

#### ❌ Test Case 3: Invalid Password
- **Username**: `practice`
- **Password**: `WrongPassword` (o cualquier password incorrecto)
- **Error esperado**: "Invalid password."

### Casos de Prueba de Register

#### ✅ Test Case 1: Successful Registration
- **Username**: Cualquier username válido único
- **Password**: Cualquier password válido
- **Confirm Password**: Mismo password
- **Resultado esperado**: Redirección a `/login` con mensaje "Successfully registered, you can log in now."

#### ❌ Test Case 2: Missing Username
- **Username**: (vacío)
- **Password**: Válido
- **Confirm Password**: Válido
- **Error esperado**: "All fields are required."

#### ❌ Test Case 3: Missing Password
- **Username**: Válido
- **Password**: (vacío)
- **Confirm Password**: Cualquier valor
- **Error esperado**: "All fields are required."

#### ❌ Test Case 4: Non-matching Passwords
- **Username**: Válido
- **Password**: `Password123`
- **Confirm Password**: `DifferentPassword456`
- **Error esperado**: "Passwords do not match."

## 🛠️ Tecnologías y Herramientas

| Tecnología | Versión | Propósito |
|-----------|---------|-----------|
| **Java** | 17 | Lenguaje principal de desarrollo |
| **Maven** | 3.9.10 | Gestión de dependencias y construcción |
| **Selenium WebDriver** | 4.15.0 | Automatización de navegadores web |
| **TestNG** | 7.8.0 | Framework de pruebas y organización de suites |
| **WebDriverManager** | 5.6.2 | Gestión automática de drivers de navegadores |
| **ExtentReports** | 5.1.1 | Generación de reportes HTML avanzados |
| **OpenCSV** | 5.9 | Manejo de archivos CSV para datos de prueba |
| **SLF4J + Logback** | 2.0.9 / 1.4.14 | Sistema de logging estructurado |

## 🚀 Instalación y Configuración

### Prerrequisitos

1. **Java Development Kit (JDK) 17 o superior**
   ```bash
   java -version
   # Debería mostrar version 17.x.x
   ```

2. **Apache Maven 3.9.10 o superior**
   ```bash
   mvn -version
   # Debería mostrar version 3.9.x
   ```

3. **Git** (opcional, para clonar el repositorio)

### Instalación

1. **Clonar el repositorio**
   ```bash
   git clone https://github.com/usuario/suite-automatizacion-funcional.git
   cd suite-automatizacion-funcional
   ```

2. **Instalar dependencias**
   ```bash
   mvn clean install
   ```

3. **Verificar instalación**
   ```bash
   mvn test -DskipTests
   # Debería compilar sin errores
   ```

## 🏃‍♂️ Ejecución de Pruebas

### Comandos Básicos para ExpandTesting

#### Ejecutar todas las pruebas
```bash
mvn test
```

#### Ejecutar suite específica
```bash
# Suite completa
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/configuracion/testng.xml

# Solo smoke tests (casos críticos)
mvn test -Dgroups=smoke

# Solo pruebas de registro
mvn test -Dtest=PruebasRegistro

# Solo pruebas de login
mvn test -Dtest=PruebasLogin
```

#### Configuración de navegador
```bash
# Ejecutar en Chrome (por defecto)
mvn test -Dnavegador=chrome

# Ejecutar en Firefox
mvn test -Dnavegador=firefox

# Ejecutar en modo headless
mvn test -Dnavegador=chrome-headless
```

#### Ejecutar casos específicos de ExpandTesting
```bash
# Solo login exitoso con credenciales de ExpandTesting
mvn test -Dtest=PruebasLogin#loginExitosoConCredencialesValidas

# Solo registro exitoso
mvn test -Dtest=PruebasRegistro#registroExitosoConDatosCompletos

# Solo pruebas de validación
mvn test -Dgroups=validacion
```

## 📊 Datos de Prueba Específicos para ExpandTesting

### Archivos CSV Actualizados

El proyecto utiliza archivos CSV específicos para ExpandTesting:

- **`usuarios_login_expandtesting.csv`**: Credenciales válidas para login
- **`usuarios_registro_expandtesting.csv`**: Datos válidos e inválidos para registro
- **`credenciales_invalidas_expandtesting.csv`**: Credenciales inválidas para pruebas negativas

### Formato de Datos

#### usuarios_login_expandtesting.csv
```csv
username,password,nombre,apellido,descripcion
practice,SuperSecretPassword!,Practice,User,Usuario principal válido para ExpandTesting
practice,SuperSecretPassword!,Test,User,Usuario de prueba principal
```

#### usuarios_registro_expandtesting.csv
```csv
username,password,confirmPassword,descripcion,caso_prueba
newuser001,TestPassword123,TestPassword123,Registro exitoso con datos válidos,successful_registration
,ValidPassword123,ValidPassword123,Username vacío para test negativo,missing_username
validuser013,Password123,DifferentPass456,Passwords que no coinciden,passwords_not_match
```

#### credenciales_invalidas_expandtesting.csv
```csv
username,password,descripcion,error_esperado
wrongUser,SuperSecretPassword!,Username incorrecto con password válido,Invalid username.
practice,WrongPassword,Username válido con password incorrecto,Invalid password.
,SuperSecretPassword!,Username vacío con password válido,All fields are required.
```

## 🧪 Casos de Prueba Principales

### Pruebas de Login

1. **Login Exitoso** ✅
   - Credenciales: `practice` / `SuperSecretPassword!`
   - Verificación: Redirección a `/secure`
   - Mensaje esperado: "You logged into a secure area!"

2. **Login Fallido - Username Inválido** ❌
   - Username: `wrongUser`, `invalidUser`, etc.
   - Password: `SuperSecretPassword!`
   - Error esperado: "Invalid username."

3. **Login Fallido - Password Inválido** ❌
   - Username: `practice`
   - Password: `WrongPassword`, `InvalidPass123`, etc.
   - Error esperado: "Invalid password."

4. **Login Fallido - Campos Vacíos** ❌
   - Varios combinaciones de campos vacíos
   - Error esperado: "All fields are required."

### Pruebas de Registro

1. **Registro Exitoso** ✅
   - Username único válido
   - Password y confirmación coincidentes
   - Verificación: Redirección a `/login`
   - Mensaje esperado: "Successfully registered, you can log in now."

2. **Registro Fallido - Username Faltante** ❌
   - Username vacío
   - Error esperado: "All fields are required."

3. **Registro Fallido - Password Faltante** ❌
   - Password o confirmación vacíos
   - Error esperado: "All fields are required."

4. **Registro Fallido - Passwords No Coinciden** ❌
   - Passwords diferentes
   - Error esperado: "Passwords do not match."

### Pruebas de Integración

1. **Flujo Completo** 🔄
   - Registro → Login → Navegación en área segura → Logout
   - Verificación de persistencia de datos
   - Validación de estado de sesión

## ⚙️ Configuración Específica para ExpandTesting

### Archivo config.properties actualizado

```properties
# URLs específicas de ExpandTesting
app.url.base=https://practice.expandtesting.com
app.url.registro=/register
app.url.login=/login
app.url.principal=/secure

# Mensajes esperados en ExpandTesting
mensaje.login.exitoso=You logged into a secure area!
mensaje.registro.exitoso=Successfully registered, you can log in now.
mensaje.error.username.invalido=Invalid username.
mensaje.error.password.invalido=Invalid password.
mensaje.error.campos.requeridos=All fields are required.
mensaje.error.passwords.no.coinciden=Passwords do not match.

# Credenciales válidas para ExpandTesting
test.username.valido=practice
test.password.valido=SuperSecretPassword!
```

### Selectores CSS Específicos

```properties
# Selectores para formulario de login
selector.login.username=input[name='username'], #username
selector.login.password=input[name='password'], #password
selector.login.boton.submit=button[type='submit'], .btn-primary

# Selectores para formulario de registro
selector.registro.username=input[name='username'], #username
selector.registro.password=input[name='password'], #password
selector.registro.confirm.password=input[name='confirmPassword'], #confirmPassword
selector.registro.boton.submit=button[type='submit'], .btn-primary

# Selectores para mensajes
selector.mensaje.flash=#flash
selector.secure.page=h2, .secure-area-text
selector.logout.button=a[href='/logout'], .btn-secondary
```

## 📈 Reportes y Evidencias

### Tipos de Reportes Generados

1. **Reportes HTML (ExtentReports)**
   - Ubicación: `reportes/reporte_suite_automatizacion_expandtesting_[timestamp].html`
   - Casos específicos de ExpandTesting documentados
   - Screenshots de mensajes de error y éxito

2. **Capturas de Pantalla**
   - Login exitoso en área segura
   - Mensajes de error específicos
   - Flujos de registro completos

3. **Logs Detallados**
   - Interacciones con formularios de ExpandTesting
   - Verificaciones de mensajes específicos
   - Tiempos de respuesta de la plataforma

## 🔍 Casos de Prueba Específicos Implementados

### Para Desarrolladores QA

#### Smoke Tests
```bash
# Casos críticos básicos
mvn test -Dgroups=smoke
```

#### Pruebas de Validación
```bash
# Validaciones específicas de ExpandTesting
mvn test -Dgroups=validacion
```

#### Data Driven Tests
```bash
# Múltiples datasets específicos
mvn test -Dgroups=datadriven
```

### Comandos de Debugging

```bash
# Ejecutar con logs detallados
mvn test -Dlog.level=DEBUG

# Ejecutar con capturas en cada paso
mvn test -Ddebug.capturas.pasos=true

# Ejecutar sin headless para ver el navegador
mvn test -Dheadless=false -Ddebug.activar=true
```

## 🐛 Resolución de Problemas Específicos

### Problemas Comunes con ExpandTesting

#### 1. Error de Credenciales
```
Error: Invalid username. o Invalid password.
```
**Solución**: Verificar que se usan las credenciales exactas: `practice` / `SuperSecretPassword!`

#### 2. Timeout en Redirecciones
```
Error: Timeout waiting for page load
```
**Solución**: ExpandTesting puede tener latencia. Aumentar timeouts:
```properties
timeout.explicito=20
timeout.pagina=45
```

#### 3. Selectores No Encontrados
```
Error: Element not found
```
**Solución**: ExpandTesting usa estructura específica. Verificar selectores en config.properties

## 🤝 Valor Profesional del Proyecto

### Competencias Demostradas

- ✅ **Adaptabilidad**: Migración exitosa a nueva plataforma de testing
- ✅ **Automatización Real**: Casos de prueba de plataforma real de testing
- ✅ **Manejo de Datos**: CSV específicos para escenarios reales
- ✅ **Debugging**: Resolución de problemas con selectores y flujos específicos
- ✅ **Documentación**: README actualizado con casos específicos
- ✅ **Best Practices**: Implementación de patrones probados en entorno real

### Evidencias para Portfolio

1. **Screenshots de ejecución exitosa** en ExpandTesting
2. **Reportes HTML** con casos de la plataforma real
3. **Código adaptado** mostrando flexibilidad técnica
4. **Configuración específica** para entorno real de testing
5. **Casos edge documentados** y resueltos

## 📞 Contacto

**Desarrollador:** Roberto Rivas Lopez  
**Curso:** Automatización de Pruebas  
**Plataforma de Testing:** ExpandTesting (https://practice.expandtesting.com/)

---

## 📝 Notas de Migración

Este proyecto fue migrado exitosamente de una plataforma de testing básica a **ExpandTesting**, una plataforma más robusta y realista que proporciona casos de prueba específicos y mensajes de error consistentes, lo que lo convierte en una mejor herramienta de aprendizaje y demostración de competencias en automatización de pruebas.

La migración demuestra capacidades de:
- **Análisis de requerimientos** de nueva plataforma
- **Refactoring de código** manteniendo buenas prácticas
- **Actualización de datos de prueba** para nuevos escenarios
- **Documentación actualizada** con nuevos casos de uso
- **Debugging y resolución** de problemas de integración

---

**Nota**: Este proyecto representa una implementación completa y actualizada de suite de automatización funcional usando una plataforma real de testing, demostrando competencias avanzadas en testing automatizado con herramientas y escenarios del mundo real.