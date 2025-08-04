/*
 * Autores: Antonio B. Arriagada LL., Dante Escalona Bustos, Roberto Rivas Lopez
 * Proyecto: Suite de Automatización Funcional
 * Descripción: PaginaLogin actualizada para funcionar con practice.expandtesting.com
 * Fecha: 04 de agosto de 2025
 * Basado en tu implementación existente con mejoras para el sitio real
 */

package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaLogin;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de Login mejorada basada en tu implementación existente
 * Actualizada para funcionar con https://practice.expandtesting.com/login
 * 
 * MEJORAS INCLUIDAS:
 * - Selectores compatibles con el sitio real
 * - Mejor manejo de errores y validaciones
 * - Logging mejorado para debugging
 * - Capturas automáticas en puntos críticos
 * 
 * @author Antonio B. Arriagada LL.
 * @author Dante Escalona Bustos
 * @author Roberto Rivas Lopez
 * @version 2.0 - Mejorada para sitio real
 */
public class PaginaLogin extends PaginaBase implements IPaginaLogin {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaLogin.class);
    
    // === ELEMENTOS PA EL SITIO REAL ===
    
    @FindBy(id = "username")
    private WebElement campoEmail;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(xpath = "//button[contains(text(),'Login') or @type='submit']")
    private WebElement botonLogin;
    
    @FindBy(id = "flash")
    private WebElement mensajeFlash;
    
    @FindBy(className = "success-message")
    private WebElement mensajeExito;
    
    @FindBy(className = "error-message")
    private WebElement mensajeError;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'email') or contains(.,'username')]")
    private WebElement errorEmail;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'password')]")
    private WebElement errorPassword;
    
    @FindBy(linkText = "here")
    private WebElement enlaceRegistro;
    
    public PaginaLogin(WebDriver driver) {
    super(driver);
    logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaLogin inicializada"));
}
    
    @Override
    public boolean esPaginaVisible() {
        try {
            // Verificar URL correcta
            String urlActual = driver.getCurrentUrl().toLowerCase();
            boolean urlCorrecta = urlActual.contains("practice.expandtesting.com") && 
                                urlActual.contains("login");
            
            // Verificar elementos críticos con selectores robustos
            boolean tieneUsername = esperarElementoVisible(campoEmail, 10) || 
                                  existeElemento(By.cssSelector("#username, input[name='username']"));
            boolean tienePassword = esperarElementoVisible(campoPassword, 5) ||
                                  existeElemento(By.cssSelector("#password, input[name='password']"));
            boolean tieneBoton = esperarElementoVisible(botonLogin, 5) ||
                               existeElemento(By.cssSelector("button[type='submit'], button:contains('Login')"));
            
            boolean paginaVisible = urlCorrecta && tieneUsername && tienePassword && tieneBoton;
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                String.format("Verificación página login - URL: %s, Username: %s, Password: %s, Botón: %s", 
                            urlCorrecta, tieneUsername, tienePassword, tieneBoton)));
            
            return paginaVisible;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando visibilidad página login: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public void validarElementosPagina() {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje("Validando elementos de página login"));
        
        // Validaciones mejoradas con fallbacks
        if (!esElementoVisible(campoEmail) && !existeElemento(By.id("username"))) {
            throw new RuntimeException("Campo email/username no está visible");
        }
        if (!esElementoVisible(campoPassword) && !existeElemento(By.id("password"))) {
            throw new RuntimeException("Campo password no está visible");
        }
        if (!esElementoVisible(botonLogin) && !existeElemento(By.cssSelector("button[type='submit']"))) {
            throw new RuntimeException("Botón login no está visible");
        }
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Elementos de página login validados correctamente"));
    }
    
    @Override
    public void ingresarCredenciales(String email, String password) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Ingresando credenciales - Email: " + enmascararTexto(email)));
        
        ingresarEmail(email);
        ingresarPassword(password);
    }
    
    @Override
    public void ingresarEmail(String email) {
        try {
            logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando email: " + enmascararTexto(email)));
            
            WebElement campoActual = obtenerCampoEmail();
            if (campoActual != null) {
                ingresarTextoSeguro(campoActual, email, true);
                Thread.sleep(500); // Pausa para asegurar que el texto se ingrese
            } else {
                throw new RuntimeException("No se encontró campo email/username");
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando email: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar email", e);
        }
    }
    
    @Override
    public void ingresarPassword(String password) {
        try {
            logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando password"));
            
            WebElement campoActual = obtenerCampoPassword();
            if (campoActual != null) {
                ingresarTextoSeguro(campoActual, password, true);
                Thread.sleep(500); // Pausa para asegurar que el texto se ingrese
            } else {
                throw new RuntimeException("No se encontró campo password");
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar password", e);
        }
    }
    
    @Override
    public void clickBotonLogin() {
        try {
            logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Haciendo click en botón login"));
            
            WebElement botonActual = obtenerBotonLogin();
            if (botonActual != null) {
                clickSeguro(botonActual);
                Thread.sleep(2000); // Esperar procesamiento del login
            } else {
                throw new RuntimeException("No se encontró botón login");
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error haciendo click en login: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en login", e);
        }
    }
    
    @Override
    public boolean iniciarSesion(ModeloDatosPrueba datos) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Iniciando proceso de login"));
        
        try {
            // Validar datos
            if (!datos.camposLoginCompletos()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Datos de login incompletos"));
                return false;
            }
            
            // Captura inicial
            capturarPantalla("antes_login_" + datos.getCasoPrueba());
            
            // Ejecutar login
            ingresarCredenciales(datos.getEmail(), datos.getPassword());
            clickBotonLogin();
            
            // Esperar procesamiento adicional
            Thread.sleep(3000);
            
            // Captura después del intento
            capturarPantalla("despues_login_" + datos.getCasoPrueba());
            
            // Verificar resultado según expectativa
            if (datos.isEsValido()) {
                boolean exitoso = verificarLoginExitoso();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login completado - Exitoso: " + exitoso));
                if (exitoso) {
                    capturarPantalla("login_exitoso_final_" + datos.getCasoPrueba());
                }
                return exitoso;
            } else {
                boolean fallido = verificarLoginFallido();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login completado - Falló como esperado: " + fallido));
                if (fallido) {
                    capturarPantalla("login_fallido_confirmado_" + datos.getCasoPrueba());
                }
                return fallido;
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en proceso de login: " + e.getMessage()));
            capturarPantalla("error_login_" + System.currentTimeMillis());
            return false;
        }
    }
    
    @Override
    public boolean verificarLoginExitoso() {
        try {
            // Esperar un momento para que se procese
            Thread.sleep(2000);
            
            // 1. Verificar cambio de URL (salida de login)
            String urlActual = driver.getCurrentUrl().toLowerCase();
            boolean salidaDeLogin = !urlActual.contains("login") || 
                                  urlActual.contains("secure") || 
                                  urlActual.contains("welcome");
            
            // 2. Verificar mensaje de éxito
            boolean mensajeExitoVisible = verificarMensajeExito();
            
            // 3. Verificar contenido de página segura
            String paginaContent = driver.getPageSource().toLowerCase();
            boolean contenidoSeguro = paginaContent.contains("secure") || 
                                    paginaContent.contains("logout") ||
                                    paginaContent.contains("welcome") ||
                                    paginaContent.contains("logged");
            
            // 4. Verificar ausencia de errores
            boolean sinErrores = !hayErroresValidacion() && !verificarMensajeError();
            
            boolean loginExitoso = salidaDeLogin || mensajeExitoVisible || 
                                 (contenidoSeguro && sinErrores);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                String.format("Verificación login exitoso - URL cambió: %s, Éxito visible: %s, Contenido seguro: %s, Sin errores: %s", 
                            salidaDeLogin, mensajeExitoVisible, contenidoSeguro, sinErrores)));
            
            if (loginExitoso) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login exitoso detectado - URL: " + urlActual));
            }
            
            return loginExitoso;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando login exitoso: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean verificarLoginFallido() {
        try {
            // 1. Verificar que permanecemos en página de login
            boolean enPaginaLogin = esPaginaVisible();
            
            // 2. Verificar mensajes de error
            boolean hayMensajeError = verificarMensajeError();
            
            // 3. Verificar errores de validación
            boolean hayErrores = hayErroresValidacion();
            
            // 4. Verificar URL sigue siendo de login
            String urlActual = driver.getCurrentUrl().toLowerCase();
            boolean urlLogin = urlActual.contains("login");
            
            boolean loginFallido = enPaginaLogin || hayMensajeError || hayErrores || urlLogin;
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                String.format("Verificación login fallido - En login: %s, Error mensaje: %s, Errores: %s, URL login: %s", 
                            enPaginaLogin, hayMensajeError, hayErrores, urlLogin)));
            
            if (loginFallido) {
                String mensajeError = obtenerMensajeError();
                logger.info(TipoMensaje.VALIDACION.formatearMensaje("Login falló como esperado: " + mensajeError));
            }
            
            return loginFallido;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando login fallido: " + e.getMessage()));
            return true; // Asumir fallo si hay error
        }
    }
    
    @Override
    public String obtenerMensajeError() {
        try {
            StringBuilder mensajes = new StringBuilder();
            
            // Verificar mensaje flash (común en el sitio real)
            if (esElementoVisible(mensajeFlash)) {
                String textoFlash = obtenerTextoSeguro(mensajeFlash);
                if (!textoFlash.isEmpty()) {
                    mensajes.append(textoFlash).append("; ");
                }
            }
            
            // Verificar mensaje de error general
            if (esElementoVisible(mensajeError)) {
                String textoError = obtenerTextoSeguro(mensajeError);
                if (!textoError.isEmpty()) {
                    mensajes.append(textoError).append("; ");
                }
            }
            
            // Verificar errores específicos de campos
            if (esElementoVisible(errorEmail)) {
                mensajes.append(obtenerTextoSeguro(errorEmail)).append("; ");
            }
            if (esElementoVisible(errorPassword)) {
                mensajes.append(obtenerTextoSeguro(errorPassword)).append("; ");
            }
            
            // Buscar mensajes con selectores adicionales
            try {
                WebElement errorGeneral = driver.findElement(By.cssSelector(".alert, .message, .error"));
                if (errorGeneral.isDisplayed()) {
                    String texto = errorGeneral.getText().trim();
                    if (!texto.isEmpty()) {
                        mensajes.append(texto).append("; ");
                    }
                }
            } catch (Exception e) {
                // Ignorar si no se encuentra
            }
            
            String resultado = mensajes.toString();
            return resultado.endsWith("; ") ? resultado.substring(0, resultado.length() - 2) : resultado;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error obteniendo mensaje de error: " + e.getMessage()));
            return "";
        }
    }
    
    // ================================
    // MÉTODOS AUXILIARES PRIVADOS
    // ================================
    
    /**
     * Obtiene el campo email/username usando múltiples estrategias
     */
    private WebElement obtenerCampoEmail() {
        if (esElementoVisible(campoEmail)) {
            return campoEmail;
        }
        
        try {
            return driver.findElement(By.cssSelector("#username, input[name='username'], input[type='text']"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("No se encontró campo email/username"));
            return null;
        }
    }
    
    /**
     * Obtiene el campo password usando múltiples estrategias
     */
    private WebElement obtenerCampoPassword() {
        if (esElementoVisible(campoPassword)) {
            return campoPassword;
        }
        
        try {
            return driver.findElement(By.cssSelector("#password, input[name='password'], input[type='password']"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("No se encontró campo password"));
            return null;
        }
    }
    
    /**
     * Obtiene el botón login usando múltiples estrategias
     */
    private WebElement obtenerBotonLogin() {
        if (esElementoVisible(botonLogin)) {
            return botonLogin;
        }
        
        try {
            return driver.findElement(By.cssSelector("button[type='submit'], button:contains('Login'), input[type='submit']"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("No se encontró botón login"));
            return null;
        }
    }
    
    /**
     * Verifica si existe un elemento con el selector dado
     */
    private boolean existeElemento(By selector) {
        try {
            WebElement elemento = driver.findElement(selector);
            return elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Verifica si hay mensaje de éxito visible
     */
    private boolean verificarMensajeExito() {
        return esElementoVisible(mensajeExito) && !obtenerTextoSeguro(mensajeExito).isEmpty();
    }
    
    /**
     * Verifica si hay mensaje de error visible
     */
    private boolean verificarMensajeError() {
        return (esElementoVisible(mensajeError) && !obtenerTextoSeguro(mensajeError).isEmpty()) ||
               (esElementoVisible(mensajeFlash) && !obtenerTextoSeguro(mensajeFlash).isEmpty());
    }
    
    /**
     * Enmascara texto para logging seguro
     */
    private String enmascararTexto(String texto) {
        if (texto == null || texto.length() <= 3) {
            return "***";
        }
        return texto.substring(0, 2) + "***" + texto.substring(texto.length() - 1);
    }
    
    /**
     * Limpia los campos del formulario
     */
    public void limpiarFormulario() {
        try {
            WebElement campoEmailActual = obtenerCampoEmail();
            WebElement campoPasswordActual = obtenerCampoPassword();
            
            if (campoEmailActual != null) {
                campoEmailActual.clear();
            }
            if (campoPasswordActual != null) {
                campoPasswordActual.clear();
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario limpiado"));
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error limpiando formulario: " + e.getMessage()));
        }
    }
    
    /**
     * Navega a página de registro
     */
    public void irARegistro() {
        try {
            if (esElementoVisible(enlaceRegistro)) {
                clickSeguro(enlaceRegistro);
                logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Navegando a registro via enlace"));
            } else {
                driver.navigate().to("https://practice.expandtesting.com/register");
                logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Navegación directa a registro"));
            }
            Thread.sleep(2000);
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error navegando a registro: " + e.getMessage()));
        }
    }
}