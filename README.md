# 🚀 Suite de Automatización Funcional

**Proyecto de Automatización de Pruebas - Curso de Testing**
**Autor:** Roberto Rivas Lopez
**Versión:** 1.0.0
**Java:** 21 | **Maven:** 3.9.10 | **Selenium:** 4.15.0

---

## 📋 Descripción del Proyecto

Este proyecto implementa una suite completa de automatización de pruebas funcionales para validar los flujos críticos de **registro de usuarios** e **inicio de sesión** en aplicaciones web. La suite está diseñada siguiendo las mejores prácticas de automatización y los principios SOLID.

### 🎯 Objetivos

- ✅ Validar formularios de registro (campos obligatorios, reglas de negocio, mensajes de error)
- ✅ Validar inicio de sesión (credenciales válidas/inválidas, bloqueos)
- ✅ Generar evidencias automáticas (capturas, logs, reportes)
- ✅ Ejecutar pruebas en múltiples navegadores (Cross-browser testing)
- ✅ Proporcionar base extensible para futuras funcionalidades

---

## 🏗️ Arquitectura del Proyecto

El proyecto está estructurado siguiendo los principios de **Modularidad**, **Abstracción**, **Encapsulación** y **Separación de Intereses**:

```
suite-automatizacion/
├── src/
│   ├── main/java/com/automatizacion/proyecto/
│   │   ├── configuracion/          # Configuración global y navegadores
│   │   ├── utilidades/             # Utilidades reutilizables
│   │   └── enums/                  # Enumeraciones del proyecto
│   └── test/java/com/automatizacion/proyecto/
│       ├── base/                   # Clase base para todas las pruebas
│       ├── paginas/                # Page Object Model (POM)
│       │   └── interfaces/         # Interfaces para abstracción
│       ├── pruebas/                # Clases de pruebas
│       ├── utilidades/             # Utilidades específicas de test
│       └── datos/                  # Modelos y proveedores de datos
├── src/test/resources/
│   ├── config.properties           # Configuración global
│   ├── datos/                      # Archivos de datos de prueba
│   ├── testng.xml                  # Configuración de TestNG
│   └── allure.properties           # Configuración de Allure
├── reportes/                       # Reportes generados
├── capturas/                       # Capturas de pantalla
└── pom.xml                         # Configuración de Maven
```

### 🧩 Componentes Principales

#### 📁 Configuración

- **`ConfiguracionGlobal.java`**: Singleton para manejo de propiedades
- **`ConfiguracionNavegador.java`**: Factory para creación de WebDrivers

#### 🔧 Utilidades

- **`GestorCapturaPantalla.java`**: Manejo de screenshots
- **`ManejadorScrollPagina.java`**: Operaciones de scroll
- **`EsperaExplicita.java`**: Esperas inteligentes
- **`GeneradorDatosPrueba.java`**: Generación de datos aleatorios
- **`LectorDatosPrueba.java`**: Lectura de CSV/Excel

#### 📄 Page Object Model

- **`PaginaBase.java`**: Clase base con funcionalidades comunes
- **`PaginaLogin.java`**: Elementos y acciones de login
- **`PaginaRegistro.java`**: Elementos y acciones de registro
- **Interfaces**: Contratos para cada página

#### 🧪 Pruebas

- **`BaseTest.java`**: Configuración común para todas las pruebas
- **`PruebasLogin.java`**: Casos de prueba para inicio de sesión
- **`PruebasRegistro.java`**: Casos de prueba para registro de usuarios

---

## 🛠️ Tecnologías y Dependencias

### Tecnologías Core

- **Java 21**: Lenguaje de programación principal
- **Maven 3.9.10**: Gestión de dependencias y build
- **Selenium 4.15.0**: Automatización de navegadores web
- **TestNG 7.8.0**: Framework de testing

### Dependencias Principales

- **WebDriverManager 5.6.2**: Gestión automática de drivers
- **Allure 2.24.0**: Generación de reportes avanzados
- **Apache POI 5.2.4**: Lectura de archivos Excel/CSV
- **Logback 1.4.11**: Sistema de logging
- **Apache Commons**: Utilidades adicionales

### Navegadores Soportados

- ✅ **Google Chrome** (Predeterminado)
- ✅ **Mozilla Firefox**
- ✅ **Microsoft Edge**
- ✅ **Safari** (Solo macOS)

---

## 🚀 Instalación y Configuración

### Prerrequisitos

```bash
# Verificar versiones
java -version    # Java 21+
mvn -version     # Maven 3.9.10+
```

### Instalación

```bash
# 1. Clonar el repositorio
git clone <url-del-repositorio>
cd suite-automatizacion

# 2. Instalar dependencias
mvn clean install

# 3. Verificar configuración
mvn test-compile
```

### Configuración

Editar `src/test/resources/config.properties`:

```properties
# URL de la aplicación a probar
url.base=https://tu-aplicacion.com

# Navegador preferido
navegador.tipo=CHROME
navegador.headless=false

# Timeouts
timeout.implicito=10
timeout.explicito=15
```

---

## 🧪 Ejecución de Pruebas

### Comandos Básicos

```bash
# Ejecutar todas las pruebas
mvn clean test

# Ejecutar solo pruebas de smoke
git init


# Ejecutar pruebas de regresión
mvn clean test -Pregression

# Ejecutar en Firefox
mvn clean test -Pfirefox

# Ejecutar en modo headless
mvn clean test -Pheadless

# Ejecutar en paralelo
mvn clean test -Pparallel
```

### Ejecución por Grupos

```bash
# Solo pruebas de login
mvn clean test -Dgroups=login

# Solo pruebas positivas
mvn clean test -Dgroups=positivo

# Pruebas de seguridad
mvn clean test -Dgroups=security
```

### Ejecución con Parámetros

```bash
# Especificar navegador y URL
mvn clean test -Dbrowser=firefox -DbaseUrl=https://staging.app.com

# Modo headless con captura de errores
mvn clean test -Dheadless=true -Dcapturas.solo.errores=true
```

---

## 📊 Casos de Prueba Implementados

### 🔐 Pruebas de Login (15 casos)

#### ✅ Casos Positivos

- **LOGIN_001**: Login exitoso con credenciales válidas
- **LOGIN_002**: Login con múltiples usuarios válidos
- **LOGIN_003**: Funcionalidad "Recordarme"

#### ❌ Casos Negativos

- **LOGIN_004**: Credenciales inválidas
- **LOGIN_005**: Email vacío
- **LOGIN_006**: Password vacío
- **LOGIN_007**: Múltiples intentos fallidos
- **LOGIN_008**: Caracteres especiales

#### 🔧 Casos Funcionales

- **LOGIN_009**: Enlaces de navegación
- **LOGIN_010**: Validación de elementos UI
- **LOGIN_011**: Tiempo de respuesta
- **LOGIN_012**: Limpieza de formulario

#### 🛡️ Casos de Seguridad

- **LOGIN_013**: Prevención de inyección
- **LOGIN_014**: Bloqueo por intentos
- **LOGIN_015**: Validación de entrada

### 📝 Pruebas de Registro (18 casos)

#### ✅ Casos Positivos

- **REG_001**: Registro exitoso con datos completos
- **REG_002**: Registro con datos mínimos
- **REG_003**: Campos opcionales
- **REG_004**: Caracteres especiales válidos

#### ❌ Casos Negativos

- **REG_005**: Validación de campos obligatorios
- **REG_006**: Formato de email inválido
- **REG_007**: Contraseñas que no coinciden
- **REG_008**: Contraseña débil
- **REG_009**: Usuario existente

#### 🔧 Casos Funcionales

- **REG_010**: Términos y condiciones
- **REG_011**: Newsletter
- **REG_012**: Limpieza de formulario
- **REG_013**: Navegación entre páginas

#### 🛡️ Casos de Seguridad

- **REG_014**: Prevención XSS
- **REG_015**: Inyección de scripts
- **REG_016**: Longitud máxima de campos
- **REG_017**: Caracteres especiales maliciosos
- **REG_018**: Validación de entrada

---

## 📈 Reportes y Evidencias

### 📋 Tipos de Reportes

1. **Allure Reports**: Reportes HTML interactivos con gráficos
2. **TestNG Reports**: Reportes estándar de TestNG
3. **Logs**: Archivos de log detallados
4. **Screenshots**: Capturas automáticas en errores

### 🎯 Generar Reportes

```bash
# Generar reporte Allure
mvn allure:report

# Abrir reporte en navegador
mvn allure:serve

# Limpiar reportes anteriores
mvn allure:clean
```

### 📸 Capturas de Pantalla

- **Automáticas**: En caso de errores
- **Manuales**: En puntos específicos del test
- **Formato**: PNG de alta calidad
- **Ubicación**: `capturas/` con timestamp

---

## 📂 Datos de Prueba

### 📄 Archivos Incluidos

- **`credenciales.csv`**: 15 casos de login
- **`usuarios_prueba.xlsx`**: 30+ casos de registro
- **Generación automática**: Datos aleatorios cuando sea necesario

### 📊 Estructura de Datos CSV

```csv
caso_prueba,descripcion,email,password,es_valido,resultado_esperado,mensaje_error
LOGIN_001,"Login exitoso",usuario@test.com,Password123!,true,"Login exitoso",
LOGIN_002,"Email inválido",email-invalido,Password123!,false,"Error","Email inválido"
```

### 📈 Estructura de Datos Excel

- **Hoja 1**: Datos válidos de registro
- **Hoja 2**: Datos inválidos de registro
- **Hoja 3**: Casos mixtos de login

---

## 🔧 Principios de Diseño Aplicados

### 🏛️ Principios SOLID

#### 1. **Single Responsibility Principle (SRP)**

- Cada clase tiene una responsabilidad específica
- `GestorCapturaPantalla` solo maneja screenshots
- `LectorDatosPrueba` solo lee archivos de datos

#### 2. **Open/Closed Principle (OCP)**

- Extensible sin modificar código existente
- Nuevos navegadores via `TipoNavegador` enum
- Nuevos tipos de mensaje via `TipoMensaje` enum

#### 3. **Liskov Substitution Principle (LSP)**

- Las clases derivadas pueden sustituir a las base
- `PaginaLogin` y `PaginaRegistro` extienden `PaginaBase`

#### 4. **Interface Segregation Principle (ISP)**

- Interfaces específicas y pequeñas
- `IPaginaLogin` e `IPaginaRegistro` separadas

#### 5. **Dependency Inversion Principle (DIP)**

- Dependencias de abstracciones, no implementaciones
- Uso de interfaces para Pages Objects

### 🧩 Otros Principios

#### **Modularidad**

- Componentes independientes y reutilizables
- Separación clara de responsabilidades

#### **Encapsulación**

- Datos y métodos apropiadamente encapsulados
- Acceso controlado via getters/setters

#### **Abstracción**

- Ocultación de complejidad innecesaria
- Interfaces claras y simples

#### **Separación de Intereses**

- Configuración, datos, lógica y pruebas separadas
- Cada capa con responsabilidad específica

---

## 🎯 Características Destacadas

### ⚡ Performance

- Ejecución paralela configurable
- Timeouts optimizados
- Gestión eficiente de memoria

### 🔒 Seguridad

- Pruebas XSS y SQL Injection
- Validación de entrada
- Manejo seguro de credenciales

### 🌐 Cross-Browser

- Soporte múltiples navegadores
- Configuración centralizada
- WebDriverManager automático

### 📱 Responsive

- Pruebas en diferentes resoluciones
- Manejo de viewport dinámico

### 🔄 CI/CD Ready

- Perfiles Maven configurados
- Reportes automáticos
- Integración con Jenkins/GitHub Actions

---

## 📚 Estructura de Comandos Útiles

### 🔍 Desarrollo y Debug

```bash
# Compilar sin ejecutar pruebas
mvn clean compile test-compile

# Validar estructura del proyecto
mvn validate

# Limpiar archivos generados
mvn clean

# Ver dependencias
mvn dependency:tree

# Actualizar dependencias
mvn versions:display-plugin-updates
```

### 🧪 Testing Avanzado

```bash
# Ejecutar prueba específica
mvn test -Dtest=PruebasLogin#testLoginExitoso

# Ejecutar clase completa
mvn test -Dtest=PruebasLogin

# Ejecutar con perfil específico
mvn clean test -Psmoke,headless,chrome

# Debug mode
mvn test -Dmaven.surefire.debug

# Verbose output
mvn test -X
```

### 📊 Reportes y Análisis

```bash
# Generar site con reportes
mvn site

# Análisis de código estático
mvn spotbugs:check

# Cobertura de código
mvn jacoco:report

# Dependencias vulnerables
mvn dependency:check
```

---

## 🤝 Contribución

### 📋 Estándares de Código

- **Idioma**: Código y comentarios en español
- **Naming**: CamelCase para métodos, PascalCase para clases
- **Logging**: Uso de SLF4J con niveles apropiados
- **Testing**: Cobertura mínima del 80%

### 🔄 Flujo de Trabajo

1. Fork del repositorio
2. Crear branch descriptiva
3. Implementar cambios con tests
4. Ejecutar suite completa
5. Commit con mensaje descriptivo
6. Pull Request con descripción detallada

### 📝 Documentación

- Javadoc para todos los métodos públicos
- README actualizado para nuevas features
- Ejemplos de uso incluidos

---

## 🐛 Troubleshooting

### ❓ Problemas Comunes

#### 🌐 Driver Issues

```bash
# Error: ChromeDriver not found
# Solución: WebDriverManager se encarga automáticamente
# Verificar: navegador.tipo=CHROME en config.properties
```

#### 🕐 Timeouts

```bash
# Error: Element not found
# Solución: Aumentar timeouts en config.properties
timeout.explicito=20
timeout.implicito=15
```

#### 📁 Archivos de Datos

```bash
# Error: CSV/Excel not found
# Solución: Verificar ubicación en src/test/resources/datos/
# Crear archivos de ejemplo: usar GeneradorDatosPrueba
```

#### 🖥️ Headless Issues

```bash
# Error en modo headless
# Solución: navegador.headless=false para debug
# Verificar: dimensiones de ventana configuradas
```

### 🔧 Logs de Debug

```bash
# Habilitar logs detallados
log.nivel=DEBUG

# Ver logs en tiempo real
tail -f logs/automation.log

# Filtrar errores
grep ERROR logs/automation.log
```

---

## 📞 Contacto y Soporte

**Autor:** Roberto Rivas Lopez
**Email:** roberto.rivas@estudiante.com
**Proyecto:** Curso de Automatización de Pruebas
**Universidad/Institución:** [Nombre de la Institución]

### 📖 Recursos Adicionales

- [Documentación de Selenium](https://selenium-python.readthedocs.io/)
- [TestNG Documentation](https://testng.org/doc/)
- [Allure Framework](https://docs.qameta.io/allure/)
- [Maven Guide](https://maven.apache.org/guides/)

---

## 📄 Licencia

Este proyecto es desarrollado con fines educativos para el curso de Automatización de Pruebas.

---

## 🏆 Conclusión

Esta suite de automatización representa una base sólida y escalable para pruebas funcionales, implementando las mejores prácticas de la industria y principios de desarrollo de software. El proyecto demuestra conocimientos avanzados en:

- ✅ **Arquitectura de pruebas** bien estructurada
- ✅ **Patrones de diseño** (Page Object Model, Factory, Singleton)
- ✅ **Principios SOLID** aplicados correctamente
- ✅ **Manejo de datos** flexible y robusto
- ✅ **Reportes** profesionales y detallados
- ✅ **Cross-browser testing** automatizado
- ✅ **CI/CD integration** preparado

El proyecto está listo para ser extendido con nuevas funcionalidades y sirve como referencia para futuras implementaciones de automatización de pruebas.

---

**¡Gracias por usar la Suite de Automatización Funcional!** 🎉
