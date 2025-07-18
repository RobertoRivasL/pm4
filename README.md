# Suite de Automatización Funcional

**Desarrollado por:** Roberto Rivas Lopez
**Curso:** Automatización de Pruebas
**Tecnología:** Java 17 + Selenium WebDriver + TestNG + Maven

## 📋 Descripción del Proyecto

Suite completa de automatización funcional enfocada en la validación de formularios de registro y login. Este proyecto implementa las mejores prácticas de automatización de pruebas, incluyendo el patrón Page Object Model (POM), Data Driven Testing, y generación de reportes avanzados.

### 🎯 Objetivos

1. **Validación del formulario de registro**: Campos obligatorios, reglas de negocio, mensajes de error
2. **Validación de inicio de sesión**: Credenciales válidas/inválidas, bloqueos, cambios de contraseña
3. **Generación de evidencias**: Capturas de pantalla, logs, reportes HTML detallados
4. **Cross-browser testing**: Compatibilidad con Chrome, Firefox y Edge
5. **Data Driven Testing**: Pruebas con múltiples conjuntos de datos desde CSV

## 🛠️ Tecnologías y Herramientas

| Tecnología                  | Versión       | Propósito                                     |
| ---------------------------- | -------------- | ---------------------------------------------- |
| **Java**               | 17             | Lenguaje principal de desarrollo               |
| **Maven**              | 3.9.10         | Gestión de dependencias y construcción       |
| **Selenium WebDriver** | 4.15.0         | Automatización de navegadores web             |
| **TestNG**             | 7.8.0          | Framework de pruebas y organización de suites |
| **WebDriverManager**   | 5.6.2          | Gestión automática de drivers de navegadores |
| **ExtentReports**      | 5.1.1          | Generación de reportes HTML avanzados         |
| **OpenCSV**            | 5.9            | Manejo de archivos CSV para datos de prueba    |
| **SLF4J + Logback**    | 2.0.9 / 1.4.14 | Sistema de logging estructurado                |

## 🏗️ Arquitectura del Proyecto

```
suite-automatizacion-funcional/
├── src/
│   ├── main/java/com/robertorivas/automatizacion/
│   │   ├── configuracion/          # Configuración de navegadores y pruebas
│   │   ├── modelos/                # Modelos de datos (Usuario, DatosRegistro)
│   │   ├── paginas/                # Page Objects (POM)
│   │   ├── utilidades/             # Utilidades (datos, evidencias, reportes)
│   │   └── servicios/              # Servicios de navegación y validación
│   └── test/
│       ├── java/com/robertorivas/automatizacion/
│       │   ├── pruebas/            # Clases de pruebas
│       │   ├── datos/              # Proveedores de datos
│       │   └── suites/             # Suites de pruebas organizadas
│       └── resources/
│           ├── datos/              # Archivos CSV con datos de prueba
│           ├── configuracion/      # Archivos de configuración
│           └── testng.xml          # Configuración de suites TestNG
├── reportes/                       # Reportes generados
├── documentacion/                  # Documentación adicional
└── pom.xml                        # Configuración Maven
```

## 🔧 Principios de Diseño Aplicados

### SOLID

- **S** - Single Responsibility: Cada clase tiene una responsabilidad específica
- **O** - Open/Closed: Extensible para nuevos navegadores sin modificar código existente
- **L** - Liskov Substitution: Las implementaciones pueden sustituirse sin afectar funcionalidad
- **I** - Interface Segregation: Interfaces específicas según necesidades
- **D** - Dependency Inversion: Dependencias de abstracciones, no de concreciones

### Adicionales

- **Modularidad**: Separación clara de responsabilidades por módulos
- **Abstracción**: Page Object Model y capas de abstracción
- **Encapsulación**: Datos y métodos encapsulados apropiadamente
- **Separación de Intereses**: Configuración, datos, lógica y reportes separados

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

### Comandos Básicos

#### Ejecutar todas las pruebas

```bash
mvn test
```

#### Ejecutar suite específica

```bash
# Suite completa
mvn test -Dsurefire.suiteXmlFiles=src/test/resources/configuracion/testng.xml

# Solo smoke tests  
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

#### Cross-browser testing

```bash
mvn test -P cross-browser
```

### Configuración Avanzada

#### Variables de entorno soportadas

```bash
# Navegador a utilizar
export NAVEGADOR=chrome|firefox|edge|chrome-headless|firefox-headless

# Entorno de ejecución  
export ENTORNO=desarrollo|ci|produccion

# Modo headless
export HEADLESS=true|false

# URL base personalizada
export URL_BASE=https://mi-aplicacion.com
```

#### Ejecución con parámetros personalizados

```bash
mvn test \
  -Dnavegador=firefox \
  -Dentorno=ci \
  -Dheadless=true \
  -Dtest=PruebasRegistro#registroExitosoConDatosCompletos
```

## 📊 Datos de Prueba

### Archivos CSV

El proyecto utiliza archivos CSV para Data Driven Testing:

- **`usuarios_registro.csv`**: Datos válidos para registro de usuarios
- **`usuarios_login.csv`**: Credenciales válidas para login
- **`credenciales_invalidas.csv`**: Credenciales inválidas para pruebas negativas

### Formato de Datos

#### usuarios_registro.csv

```csv
email,password,confirmarPassword,nombre,apellido,telefono,genero,pais,ciudad,aceptaTerminos,notificaciones,tipo
juan.perez@testautomation.com,Password123,Password123,Juan,Pérez,+34600123456,Masculino,España,Madrid,true,true,NORMAL
```

#### usuarios_login.csv

```csv
email,password,nombre,apellido,telefono
student,Password123,Test,Student,+34600111111
admin,admin123,Admin,User,+34600222222
```

### Generación de Datos Aleatorios

El sistema puede generar datos aleatorios cuando los archivos CSV no están disponibles:

```java
// Generar usuarios aleatorios
List<Usuario> usuarios = gestorDatos.generarUsuariosAleatorios(10);

// Generar datos de registro aleatorios
List<DatosRegistro> datos = gestorDatos.generarDatosRegistroAleatorios(5);
```

## 📈 Reportes y Evidencias

### Tipos de Reportes Generados

1. **Reportes HTML (ExtentReports)**

   - Ubicación: `reportes/reporte_suite_automatizacion_[timestamp].html`
   - Características: Interactivo, con capturas, filtros, estadísticas
2. **Capturas de Pantalla**

   - Ubicación: `reportes/capturas/`
   - Automáticas en fallos y pasos importantes
3. **Logs Detallados**

   - Ubicación: `reportes/logs/`
   - Nivel configurable (INFO, DEBUG, ERROR)
4. **Resumen de Evidencias**

   - Ubicación: `reportes/resumen_evidencias_[timestamp].txt`
   - Estadísticas consolidadas

### Ejemplo de Estructura de Reporte

```
reportes/
├── reporte_suite_automatizacion_2024-01-15_14-30-45.html
├── capturas/
│   ├── captura_PruebasRegistro_exito_2024-01-15_14-31-20.png
│   └── captura_PruebasLogin_fallo_2024-01-15_14-32-15.png
├── logs/
│   ├── log_PruebasRegistro.txt
│   └── log_PruebasLogin.txt
└── resumen_evidencias_2024-01-15_14-35-00.txt
```

## 🧪 Suites de Pruebas Disponibles

### Organización por Grupos

| Grupo                  | Descripción                 | Pruebas Incluidas               |
| ---------------------- | ---------------------------- | ------------------------------- |
| **smoke**        | Pruebas críticas básicas   | Registro exitoso, Login exitoso |
| **positivo**     | Casos de éxito              | Todos los flujos válidos       |
| **negativo**     | Casos de fallo               | Validaciones, errores esperados |
| **validacion**   | Pruebas de validación       | Campos obligatorios, formatos   |
| **datadriven**   | Pruebas con múltiples datos | Proveedores de datos CSV        |
| **integracion**  | Pruebas end-to-end           | Flujos completos                |
| **crossbrowser** | Compatibilidad               | Múltiples navegadores          |

### Ejecución por Grupos

```bash
# Ejecutar solo smoke tests
mvn test -Dgroups=smoke

# Ejecutar pruebas positivas y negativas
mvn test -Dgroups=positivo,negativo

# Excluir pruebas de integración
mvn test -DexcludedGroups=integracion
```

## 🔍 Casos de Prueba Principales

### Pruebas de Registro

1. **Registro Exitoso**

   - Datos completos válidos
   - Datos mínimos requeridos
   - Múltiples conjuntos de datos (CSV)
2. **Validaciones**

   - Campos obligatorios vacíos
   - Email con formato inválido
   - Contraseñas que no coinciden
   - Contraseña demasiado corta
   - Sin aceptar términos y condiciones
3. **Funcionalidad**

   - Limpiar formulario
   - Navegación entre páginas

### Pruebas de Login

1. **Login Exitoso**

   - Credenciales válidas
   - Múltiples usuarios (CSV)
   - Opción "Recordar credenciales"
2. **Login Fallido**

   - Credenciales inválidas
   - Campos vacíos
   - Intentos múltiples (seguridad)
3. **Flujos Completos**

   - Login → Navegación → Logout
   - Manejo de sesiones

### Pruebas de Integración

1. **Flujo End-to-End**

   - Registro → Login → Logout
   - Navegación entre páginas
   - Persistencia de datos
2. **Compatibilidad**

   - Cross-browser testing
   - Resoluciones de pantalla
   - Manejo de errores

## ⚙️ Configuración Avanzada

### Archivo config.properties

```properties
# URLs de la aplicación
app.url.base=https://practicetestautomation.com
app.url.registro=/practice-test-login
app.url.login=/practice-test-login

# Configuración de navegadores
navegador.defecto=chrome
navegador.headless=false

# Timeouts (en segundos)
timeout.implicito=10
timeout.explicito=15
timeout.pagina=30

# Rutas de archivos
ruta.datos=src/test/resources/datos
ruta.reportes=reportes
ruta.capturas=reportes/capturas
```

### Personalización de Configuraciones

```java
// Cambiar navegador programáticamente
System.setProperty("navegador", "firefox");

// Cambiar entorno
System.setProperty("entorno", "ci");

// Modo headless
System.setProperty("headless", "true");
```

## 🐛 Resolución de Problemas

### Problemas Comunes

#### 1. Error de Driver de Navegador

```
Error: WebDriver not found
```

**Solución**: WebDriverManager maneja automáticamente los drivers. Verificar conectividad a internet.

#### 2. Timeout en Elementos

```
Error: Element not found within timeout
```

**Solución**: Aumentar timeouts en `config.properties` o verificar selectores CSS.

#### 3. Problemas de Datos CSV

```
Error: CSV file not found
```

**Solución**: Verificar que los archivos CSV están en `src/test/resources/datos/`.

#### 4. Errores de Compilación

```
Error: Java version mismatch
```

**Solución**: Verificar que se está usando Java 17+:

```bash
java -version
mvn -version
```

### Logs y Debugging

#### Habilitar logs detallados

```bash
# Ejecutar con logs DEBUG
mvn test -Dlog.level=DEBUG

# Ver logs en tiempo real
tail -f reportes/logs/log_[PruebaNombre].txt
```

#### Debugging de Selectores

```java
// En las clases Page Object, se pueden ajustar selectores
By.cssSelector("input[name='username'], #username, input[type='email']")
```

### Verificación de Configuración

```bash
# Verificar configuración de Maven
mvn help:system

# Verificar dependencias
mvn dependency:tree

# Limpiar y recompilar
mvn clean compile
```

## 📚 Documentación Adicional

### Estructura de Clases Principales

#### Page Objects

- **`PaginaBase.java`**: Clase base con funcionalidades comunes
- **`PaginaRegistro.java`**: Formulario de registro de usuarios
- **`PaginaLogin.java`**: Formulario de inicio de sesión
- **`PaginaPrincipal.java`**: Página después del login exitoso

#### Modelos de Datos

- **`Usuario.java`**: Representa un usuario del sistema
- **`DatosRegistro.java`**: Datos específicos del formulario de registro

#### Utilidades

- **`GestorDatos.java`**: Manejo de datos CSV y generación aleatoria
- **`GestorEvidencias.java`**: Capturas de pantalla y logs
- **`GeneradorReportes.java`**: Reportes HTML con ExtentReports

### Patrones de Diseño Utilizados

1. **Page Object Model (POM)**: Encapsulación de elementos de página
2. **Factory Pattern**: Creación de drivers y datos
3. **Singleton Pattern**: Configuraciones y reportes
4. **Builder Pattern**: Construcción de objetos complejos
5. **Template Method**: Estructura común de pruebas

### Buenas Prácticas Implementadas

1. **Separación de Responsabilidades**: Cada clase tiene un propósito específico
2. **Reutilización de Código**: Funcionalidades comunes en clases base
3. **Manejo de Errores**: Try-catch apropiados y logs informativos
4. **Threading Safety**: Soporte para ejecución paralela
5. **Configuración Centralizada**: Un solo punto de configuración
6. **Evidencias Automáticas**: Capturas y logs sin intervención manual

## 🤝 Contribución al Portafolio Profesional

Este proyecto demuestra competencias clave en:

- ✅ **Automatización de Pruebas**: Selenium WebDriver con Java
- ✅ **Frameworks de Testing**: TestNG y organización de suites
- ✅ **Gestión de Dependencias**: Maven y configuración de proyectos
- ✅ **Patrones de Diseño**: POM, Factory, Singleton, Builder
- ✅ **Data Driven Testing**: CSV y proveedores de datos
- ✅ **Cross-Browser Testing**: Compatibilidad entre navegadores
- ✅ **Reportes Avanzados**: ExtentReports con evidencias
- ✅ **CI/CD Ready**: Configuración para integración continua
- ✅ **Documentación Técnica**: README completo y comentarios

### Valor para Empleadores

1. **Conocimientos Técnicos Sólidos**: Java 17, Selenium 4, TestNG
2. **Mejores Prácticas**: Principios SOLID, patrones de diseño
3. **Automatización Completa**: Desde datos hasta reportes
4. **Mantenibilidad**: Código limpio y bien estructurado
5. **Escalabilidad**: Fácil extensión para nuevas funcionalidades

## 📞 Contacto y Soporte

**Desarrollador:** Roberto Rivas Lopez
**Curso:** Automatización de Pruebas
**Email:** roberto.rivas.l@mail.pucv.cl
**LinkedIn:** [www.linkedin.com/in/rrivasl](www.linkedin.com/in/rrivasl)
**GitHub:** [https://github.com/RobertoRivasL](https://github.com/RobertoRivasL)

---

## 📄 Licencia

Este proyecto está desarrollado con fines educativos como parte del curso de Automatización de Pruebas.

---

**Nota**: Este proyecto representa una implementación completa de suite de automatización funcional, siguiendo las mejores prácticas de la industria y demostrando competencias avanzadas en testing automatizado.
