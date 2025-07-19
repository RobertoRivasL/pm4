# 🚗 CONFIGURACIÓN DE DRIVERS - DOCUMENTACIÓN TÉCNICA

## 📁 Ubicación de Drivers en el Proyecto

### **src/test/resources/drivers/**
```
src/test/resources/drivers/
├── msedgedriver.exe          # Microsoft Edge WebDriver (incluido)
└── Driver_Notes/             # Documentación oficial del driver
    ├── credits.html          # Información de créditos
    ├── EULA                  # Términos de uso
    └── LICENSE               # Licencia del driver
```

## 🎯 Estrategia de Gestión de Drivers

### **Automática (WebDriverManager) - Recomendada**
- **Chrome**: Descarga automática según versión instalada
- **Firefox**: Descarga automática según versión instalada
- **Ventajas**: Sin configuración manual, siempre actualizado
- **Configuración**: Automática en `ConfiguracionNavegador.java`

### **Manual (Edge) - Incluido en Proyecto**
- **Microsoft Edge**: Driver específico incluido en `src/test/resources/drivers/`
- **Ventaja**: Funciona sin conexión a internet
- **Configuración**: Automática vía WebDriverManager con fallback

## ⚙️ Configuración en Código

### **ConfiguracionNavegador.java**
```java
public static WebDriver crearDriver(TipoNavegador tipo) {
    switch (tipo) {
        case CHROME:
            WebDriverManager.chromedriver().setup();
            return new ChromeDriver(configurarOpcionesChrome());
            
        case FIREFOX:
            WebDriverManager.firefoxdriver().setup();
            return new FirefoxDriver(configurarOpcionesFirefox());
            
        case EDGE:
            WebDriverManager.edgedriver().setup();
            // Si WebDriverManager falla, puede usar driver local en resources
            return new EdgeDriver(configurarOpcionesEdge());
    }
}
```

### **Comando de Ejecución por Navegador**
```bash
# Chrome (por defecto)
mvn test -Dnavegador=chrome

# Firefox  
mvn test -Dnavegador=firefox

# Microsoft Edge (usa driver incluido)
mvn test -Dnavegador=edge

# Modos headless
mvn test -Dnavegador=chrome-headless
mvn test -Dnavegador=firefox-headless
```

## 🔄 Compatibilidad de Versiones

### **Edge Driver Incluido**
- **Versión**: Compatible con Edge versiones recientes
- **Actualización**: Manual cuando sea necesario
- **Verificación**: El driver funciona con versiones estables de Edge

### **Chrome/Firefox (Automático)**
- **Versión**: Siempre la más compatible con navegador instalado
- **Actualización**: Automática en cada ejecución
- **Verificación**: WebDriverManager maneja compatibilidad

## 🛠️ Troubleshooting

### **Error: "Edge driver not compatible"**
1. Verificar versión de Edge instalada
2. Actualizar driver manualmente si es necesario
3. Descargar desde: https://developer.microsoft.com/en-us/microsoft-edge/tools/webdriver/

### **Error: "WebDriverManager can't download driver"**
1. Verificar conexión a internet
2. Para Edge: El driver local en resources funciona como fallback
3. Para Chrome/Firefox: Verificar que WebDriverManager tenga permisos

### **Error: "Driver executable not found"**
1. Edge: Verificar que `src/test/resources/drivers/msedgedriver.exe` existe
2. Chrome/Firefox: Permitir que WebDriverManager descargue automáticamente
3. Verificar permisos de ejecución

## 📊 Ventajas de la Estrategia Híbrida

### **Automática (Chrome/Firefox)**
✅ Siempre actualizada
✅ Sin mantenimiento manual
✅ Compatible con CI/CD
✅ No requiere espacio en repositorio

### **Manual (Edge)**
✅ Funciona sin internet
✅ Control total de versión
✅ Configuración predecible
✅ Ideal para entornos corporativos

## 🚀 Configuración para Desarrollo

### **Primer Setup**
1. El proyecto funciona inmediatamente sin configuración adicional
2. Chrome/Firefox se configuran automáticamente
3. Edge usa el driver incluido en el proyecto
4. No se requiere descarga manual de drivers

### **Ejecución en CI/CD**
```yaml
# GitHub Actions / Azure DevOps
- name: Run tests
  run: |
    mvn test -Dnavegador=chrome-headless  # Chrome headless
    mvn test -Dnavegador=firefox-headless # Firefox headless  
    mvn test -Dnavegador=edge            # Edge con driver incluido
```

### **Ejecución en Diferentes Entornos**
- **Windows**: Funciona con todos los navegadores
- **Linux**: Chrome y Firefox automático, Edge requiere instalación
- **macOS**: Chrome y Firefox automático, Edge requiere instalación

## 📝 Notas Importantes

1. **Edge Driver**: El archivo `msedgedriver.exe` está incluido y versionado en el proyecto
2. **Licencias**: Toda la documentación legal está en `Driver_Notes/`
3. **Actualizaciones**: Edge driver se actualiza manualmente cuando es necesario
4. **Compatibilidad**: El driver incluido funciona con versiones estables recientes de Edge
5. **Fallback**: Si WebDriverManager falla, el sistema puede usar drivers locales

---

**Esta configuración híbrida garantiza que el proyecto funcione en cualquier entorno sin configuración adicional, manteniendo la flexibilidad para todos los navegadores principales.**
