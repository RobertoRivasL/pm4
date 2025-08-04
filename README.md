# 🚀 Suite de Automatización Funcional

**Proyecto de Automatización de Pruebas - Curso de Testing**

**Autores:** 
- Antonio B. Arriagada LL. (anarriag@gmail.com)
- Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
- Roberto Rivas Lopez (umancl@gmail.com)

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

### 🔧 Soluciones Implementadas

#### Problemas Solucionados:
1. **❌ Capturas mal tomadas** → ✅ Capturas en momentos precisos (formulario lleno + resultado)
2. **❌ Ventanas de login no se cierran** → ✅ Gestión automática de ventanas múltiples
3. **❌ Errores de configuración** → ✅ Configuración robusta con validaciones
4. **❌ Timeouts incorrectos** → ✅ Timeouts específicos para cada tipo de elemento

---

## 🏗️ Arquitectura del Proyecto

El proyecto está estructurado siguiendo los principios de **Modularidad**, **Abstracción**, **Encapsulación** y **Separación de Intereses**:

```
suite-automatizacion/
├── src/
│   ├── main/java/com/automatizacion/proyecto/
│   │   ├── configuracion/          # Configuración global y navegadores
│   │   │   ├── ConfiguracionGlobal.java        # Singleton para configuración
│   │   │   └── ConfiguradorNavegador.java      # Factory para WebDrivers
│   │   ├── utilidades/             # Utilidades reutilizables
│   │   │   └── GestorCapturaPantalla.java      # Manejo de capturas mejorado
│   │   └── enums/                  # Enumeraciones del proyecto
│   │       ├── TipoNavegador.java              # Tipos de navegadores
│   │       └── TipoMensaje.java                # Tipos de mensajes de log
│   └── test/java/com/automatizacion/proyecto/
│       ├── base/                   # Clases base para pruebas y páginas
│       │   ├── PruebaBase.java                 # Clase base para todas las pruebas
│       │   └── PaginaBase.java                 # Clase base para Page Object Model
│       ├── paginas/                # Page Object Model (POM)
│       │   └── PaginaLogin.java                # Página de login corregida
│       └── pruebas/                # Clases de pruebas
│           └── PruebasLogin.java               # Casos de prueba de login
├── src/test/resources/
│   ├── config.properties           # Configuración global
│   ├── testng.xml                  # Configuración de TestNG
│   └── datos/                      # Archivos de datos de prueba
├── reportes/                       # Reportes generados
├── capturas/                       # Capturas de pantalla
└── pom.xml                         # Configuración de Maven
```

### 🧩 Componentes Principales

#### 📁 Configuración
- **`ConfiguracionGlobal.java`**: Singleton para manejo de propiedades
- **`ConfiguradorNavegador.java`**: Factory para creación de WebDrivers

#### 🔧 Utilidades
- **`GestorCapturaPantalla.java`**: Manejo mejorado de capturas
- **`ManejadorScrollPagina.java`**: Operaciones de scroll
- **`EsperaExplicita.java`**: Esperas inteligentes

#### 📄 Page Object Model
- **`PaginaBase.java`**: Clase base con funcionalidades comunes
- **`PaginaLogin.java`**: Elementos y acciones de login corregidas

#### 🧪 Pruebas
- **`PruebaBase.java`**: Configuración común para todas las pruebas
- **`PruebasLogin.java`**: Casos de prueba de login completos

---

## ⚙️ Configuración e Instalación

### 📋 Prerrequisitos

- **Java 21** o superior
- **Maven 3.9.10** o superior
- **Git** para clonar el repositorio
- **Chrome** y/o **Firefox** instalados

### 🔽 Instalación

1. **Clonar el repositorio:**
```bash
git clone <repository-url>
cd suite-automatizacion-funcional
```

2. **Verificar Java y Maven:**
```bash
java --version
mvn --version
```

3. **Instalar dependencias:**
```bash
mvn clean install -DskipTests
```

4. **Configurar archivo de propiedades:**
Editar `src/test/resources/config.properties` con la URL de tu aplicación:
```properties
# Configurar URL base de la aplicación a probar
url.base=https://tu-aplicacion.com
```

---

## 🚀 Ejecución de Pruebas

### 📱 Comandos Básicos

**Ejecutar todas las pruebas:**
```bash
mvn clean test
```

**Ejecutar solo pruebas de smoke:**
```bash
mvn clean test -Dgroups=smoke
```

**Ejecutar en navegador específico:**
```bash
mvn clean test -Dbrowser=CHROME
mvn clean test -Dbrowser=FIREFOX
```

**Ejecutar en modo headless:**
```bash
mvn clean test -Dheadless=true
```

### 🎭 Perfiles de Ejecución

**Cross-browser testing:**
```bash
mvn clean test -Pcrossbrowser
```

**Pruebas de regresión:**
```bash
mvn clean test -Pregression
```

**Modo headless:**
```bash
mvn clean test -Pheadless
```

**Ejecución en paralelo:**
```bash
mvn clean test -Pparallel
```

### 🎯 Grupos de Pruebas

- **smoke**: Pruebas críticas fundamentales
- **regression**: Suite completa de regresión
- **login**: Todas las pruebas de login
- **registro**: Todas las pruebas de registro
- **negativo**: Casos de prueba negativos
- **validacion**: Pruebas de validación
- **security**: Pruebas de seguridad básica

---

## 📊 Reportes y Evidencias

### 📈 Allure Reports

**Generar reporte:**
```bash
mvn allure:report
```

**Abrir reporte:**
```bash
mvn allure:serve
```

### 📸 Capturas de Pantalla

Las capturas se guardan automáticamente en:
- **Ubicación**: `capturas/`
- **Momentos**: Formulario lleno, resultado de login, errores
- **Formato**: PNG con timestamp único

### 📝 Logs

Los logs se guardan en:
- **Archivo**: `logs/automation.log`
- **Niveles**: DEBUG, INFO, WARN, ERROR, CRITICAL
- **Formato**: Timestamp + Tipo + Mensaje

---

## 🔍 Casos de Prueba Implementados

### 🔐 Pruebas de Login

| ID | Descripción | Grupo | Prioridad |
|----|-------------|-------|-----------|
| LOGIN_001 | Login exitoso con credenciales válidas | smoke | BLOCKER |
| LOGIN_002 | Acceso al dashboard después de login | smoke | CRITICAL |
| LOGIN_003 | Rechazo de credenciales inválidas | regression | CRITICAL |
| LOGIN_004 | Password incorrecto para usuario válido | regression | NORMAL |
| LOGIN_005 | Usuario inexistente | regression | NORMAL |
| LOGIN_006 | Validación de campos vacíos | validacion | NORMAL |
| LOGIN_007 | Solo usuario sin password | validacion | MINOR |
| LOGIN_008 | Solo password sin usuario | validacion | MINOR |
| LOGIN_009 | Múltiples intentos fallidos | stress | MINOR |
| LOGIN_010 | Password enmascarado | security | NORMAL |
| LOGIN_011 | Elementos de interfaz presentes | ui | MINOR |
| LOGIN_012 | Flujo completo login-logout | integration | CRITICAL |
| LOGIN_013 | Tiempo de respuesta | performance | MINOR |

### 📝 Pruebas de Registro

| ID | Descripción | Grupo | Prioridad |
|----|-------------|-------|-----------|
| REG_001 | Registro exitoso con datos válidos | smoke | BLOCKER |
| REG_002 | Email con formato inválido | validacion | NORMAL |
| REG_003 | Contraseñas que no coinciden | validacion | CRITICAL |
| REG_004 | Campos obligatorios vacíos | validacion | NORMAL |
| REG_005 | Contraseña débil | security | NORMAL |
| REG_006 | Usuario ya existente | negativo | NORMAL |
| REG_007 | Términos no aceptados | validacion | CRITICAL |
| REG_008 | Campos opcionales | funcionalidad | MINOR |

---

## 🛠️ Soluciones Técnicas Implementadas

### 🔧 Problema: Capturas mal tomadas
**Solución:**
- Capturas específicas en momentos precisos:
  1. **Formulario lleno** (antes de submit)
  2. **Resultado visible** (después de submit)
  3. **Estado final** (confirmación)

### 🪟 Problema: Ventanas que no se cierran
**Solución:**
- Gestión automática de ventanas múltiples
- Cierre forzado de ventanas extra
- Verificación de handles de ventana

### ⏱️ Problema: Timeouts incorrectos
**Solución:**
- Timeouts específicos por tipo de elemento
- Esperas inteligentes con condiciones específicas
- Configuración granular de tiempos

### 🔐 Problema: Elementos de login no encontrados
**Solución:**
- Selectores múltiples y fallbacks
- Validación robusta de presencia de elementos
- Reintentos automáticos con JavaScript

---

## 🎨 Principios de Diseño Aplicados

### 🏗️ Principios SOLID

1. **Single Responsibility Principle (SRP)**
   - Cada clase tiene una única responsabilidad
   - `GestorCapturaPantalla` solo maneja capturas
   - `ConfiguracionGlobal` solo maneja configuración

2. **Open/Closed Principle (OCP)**
   - Extensible para nuevos navegadores sin modificar código existente
   - Nuevos tipos de mensaje mediante enums

3. **Liskov Substitution Principle (LSP)**
   - Todas las páginas heredan de `PaginaBase`
   - Todas las pruebas heredan de `PruebaBase`

4. **Interface Segregation Principle (ISP)**
   - Interfaces específicas para cada funcionalidad
   - No dependencias innecesarias

5. **Dependency Inversion Principle (DIP)**
   - Dependencias de abstracciones, no implementaciones
   - Inyección de dependencias mediante constructores

### 🧩 Patrones de Diseño

- **Singleton**: `ConfiguracionGlobal`
- **Factory**: `ConfiguradorNavegador`
- **Page Object Model**: Todas las páginas
- **Template Method**: `PruebaBase`, `PaginaBase`

---

## 🔧 Troubleshooting

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

**Autores:**
- **Antonio B. Arriagada LL.** - anarriag@gmail.com
- **Dante Escalona Bustos** - Jacobo.bustos.22@gmail.com  
- **Roberto Rivas Lopez** - umancl@gmail.com

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
- ✅ **Solución de problemas específicos** de login y capturas

El proyecto está listo para ser extendido con nuevas funcionalidades y sirve como referencia para futuras implementaciones de automatización de pruebas.

---

**¡Gracias por usar la Suite de Automatización Funcional!** 🎉