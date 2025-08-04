package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.base.PaginaBase;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

/**
 * Página de Login usando el patrón Page Object Model.
 * 
 * SOLUCIONES IMPLEMENTADAS:
 * - Capturas correctas: formulario lleno ANTES de submit y resultado DESPUÉS
 * - Manejo de ventanas que no se cierran
 * - Esperas específicas para elementos de login
 * - Validaciones robustas de estado
 * 
 * Principios aplicados:
 * - Page Object Model: Encapsula elementos y acciones de la página
 * - Single Responsibility: Solo maneja operaciones de login
 * - Encapsulation: Elementos privados con métodos públicos específicos
 * 
 * @author Antonio B. Arriagada LL. (anarriag@gmail.com)
 * @author Dante Escalona Bustos (Jacobo.bustos.22@gmail.com)  
 * @author Roberto Rivas Lopez (umancl@gmail.com)
 * @version 1.0
 */
public class PaginaLogin extends PaginaBase {
    
    // === LOCALIZADORES DE ELEMENTOS ===
    
    @FindBy(id = "username")
    private WebElement campoUsuario;
    
    @FindBy(id = "password") 
    private WebElement campoPassword;
    
    @FindBy(id = "login-button")
    private WebElement botonLogin;
    
    @FindBy(css = ".login-form, form")
    private WebElement formularioLogin;
    
    @FindBy(css = ".alert-danger, .error-message, .text-danger")
    private WebElement mensajeError;
    
    @FindBy(css = ".alert-success, .success-message, .text-success")
    private WebElement mensajeExito;
    
    @FindBy(css = ".loading, .spinner")
    private WebElement indicadorCarga;
    
    // === SELECTORES COMO CONSTANTES ===
    private static final String SELECTOR_FORMULARIO = "form, .login-form";
    private static final String SELECTOR_MENSAJE_ERROR = ".alert-danger, .error-message, .text-danger";
    private static final String SELECTOR_MENSAJE_EXITO = ".alert-success, .success-message, .text-success";
    private static final String SELECTOR_DASHBOARD = "#secure, .secure-area, .main-content";
    
    // === LOCALIZADORES BY ===
    private static final By BY_CAMPO_USUARIO = By.id("username");
    private static final By BY_CAMPO_PASSWORD = By.id("password");
    private static final By BY_BOTON_LOGIN = By.id("login-button");
    private static final By BY_MENSAJE_ERROR = By.cssSelector(SELECTOR_MENSAJE_ERROR);
    private static final By BY_MENSAJE_EXITO = By.cssSelector(SELECTOR_MENSAJE_EXITO);
    private static final By BY_DASHBOARD = By.cssSelector(SELECTOR_DASHBOARD);
    
    /**
     * Constructor que inicializa la página de login
     * 
     * @param driver WebDriver activo
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje(
            "Inicializando página de login"));
    }
    
    // === IMPLEMENTACIÓN DE MÉTODOS ABSTRACTOS ===
    
    @Override
    public boolean esPaginaVisible() {
        try {
            // Verificar múltiples indicadores de que estamos en la página de login
            boolean formularioPresente = estaPresente(By.cssSelector(SELECTOR_FORMULARIO));
            boolean camposPresentes = estaPresente(BY_CAMPO_USUARIO) && estaPresente(BY_CAMPO_PASSWORD);
            boolean botonPresente = estaPresente(BY_BOTON_LOGIN);
            
            boolean paginaVisible = formularioPresente && camposPresentes && botonPresente;
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Verificación de página login - Formulario: " + formularioPresente + 
                ", Campos: " + camposPresentes + ", Botón: " + botonPresente));
            
            return paginaVisible;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando página de login: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public String obtenerUrlEsperada() {
        return config.obtenerUrlLogin();
    }
    
    // === MÉTODOS PRINCIPALES DE LOGIN ===
    
    /**
     * Realiza el proceso completo de login con capturas correctas
     * SOLUCIÓN PRINCIPAL: Capturas en momentos correctos
     * 
     * @param usuario nombre de usuario
     * @param password contraseña
     * @param nombreCaso nombre del caso de prueba para capturas
     * @return true si el login fue exitoso
     */
    public boolean realizarLoginCompleto(String usuario, String password, String nombreCaso) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
            "Iniciando login completo para usuario: " + usuario));
        
        try {
            // 1. Verificar que estamos en la página correcta
            if (!esPaginaVisible()) {
                throw new RuntimeException("No estamos en la página de login");
            }
            
            // 2. Limpiar campos primero
            limpiarCamposLogin();
            
            // 3. Llenar formulario
            ingresarCredenciales(usuario, password);
            
            // 4. CAPTURA CRÍTICA: Formulario lleno ANTES de enviar
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Capturando formulario lleno antes de enviar"));
            GestorCapturaPantalla.capturarFormularioLogin(driver, nombreCaso, SELECTOR_FORMULARIO);
            
            // 5. Enviar formulario
            boolean loginExitoso = enviarFormularioLogin();
            
            // 6. Esperar resultado y obtener estado
            String estadoLogin = esperarYObtenerResultadoLogin();
            
            // 7. CAPTURA CRÍTICA: Resultado DESPUÉS de enviar
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Capturando resultado de login: " + (loginExitoso ? "exitoso" : "fallido")));
            
            String selectorResultado = loginExitoso ? SELECTOR_DASHBOARD : SELECTOR_MENSAJE_ERROR;
            GestorCapturaPantalla.capturarResultadoLogin(driver, nombreCaso, loginExitoso, selectorResultado);
            
            // 8. Log del resultado
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Login completado - Usuario: " + usuario + ", Resultado: " + estadoLogin));
            
            return loginExitoso;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error durante login: " + e.getMessage()));
            GestorCapturaPantalla.capturarPantallaError(driver, nombreCaso + "_login_error", e);
            return false;
        }
    }
    
    /**
     * Ingresa credenciales en los campos de login
     * 
     * @param usuario nombre de usuario
     * @param password contraseña
     */
    public void ingresarCredenciales(String usuario, String password) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando credenciales"));
        
        try {
            // Ingresar usuario
            WebElement campoUser = esperarElementoClickeable(BY_CAMPO_USUARIO, TIMEOUT_ELEMENTO_LARGO);
            ingresarTextoSeguro(campoUser, usuario);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Usuario ingresado"));
            
            // Pequeña pausa entre campos
            Thread.sleep(500);
            
            // Ingresar password
            WebElement campoPass = esperarElementoClickeable(BY_CAMPO_PASSWORD, TIMEOUT_ELEMENTO_LARGO);
            ingresarTextoSeguro(campoPass, password);
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Password ingresado"));
            
            // Verificar que los campos no estén vacíos
            verificarCamposLlenos();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error ingresando credenciales: " + e.getMessage()));
            throw new RuntimeException("No se pudieron ingresar las credenciales", e);
        }
    }
    
    /**
     * Envía el formulario de login y espera respuesta
     * MEJORADO: Mejor manejo de estados de carga
     * 
     * @return true si no hay errores inmediatos
     */
    public boolean enviarFormularioLogin() {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Enviando formulario de login"));
        
        try {
            // Buscar y hacer click en el botón de login
            WebElement boton = esperarElementoClickeable(BY_BOTON_LOGIN, TIMEOUT_ELEMENTO_LARGO);
            
            // Hacer click de forma segura
            clickSeguro(boton);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario enviado"));
            
            // Esperar un momento para que inicie el procesamiento
            Thread.sleep(1000);
            
            return true;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error enviando formulario: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Espera y obtiene el resultado del login
     * CRÍTICO: Determina si login fue exitoso o falló
     * 
     * @return descripción del resultado
     */
    private String esperarYObtenerResultadoLogin() {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Esperando resultado de login"));
        
        try {
            // Esperar a que desaparezca cualquier indicador de carga
            esperarDesaparicionIndicadorCarga();
            
            // Verificar si hay mensaje de error (login fallido)
            if (hayMensajeError()) {
                String mensajeError = obtenerMensajeError();
                logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Login fallido: " + mensajeError));
                return "FALLIDO: " + mensajeError;
            }
            
            // Verificar si hay redirección o dashboard (login exitoso)
            if (esLoginExitoso()) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login exitoso - Dashboard cargado"));
                return "EXITOSO: Acceso concedido";
            }
            
            // Verificar cambio de URL
            String urlActual = obtenerUrlActual();
            if (!urlActual.contains("login")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login exitoso - URL cambió a: " + urlActual));
                return "EXITOSO: Redirección a " + urlActual;
            }
            
            // Si llegamos aquí, el resultado es incierto
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Resultado de login incierto"));
            return "INCIERTO: No se pudo determinar el resultado";
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error obteniendo resultado: " + e.getMessage()));
            return "ERROR: " + e.getMessage();
        }
    }
    
    // === MÉTODOS DE VALIDACIÓN ===
    
    /**
     * Verifica si el login fue exitoso
     * @return true si el login fue exitoso
     */
    public boolean esLoginExitoso() {
        try {
            // Método 1: Buscar elementos del dashboard
            if (estaVisible(BY_DASHBOARD)) {
                return true;
            }
            
            // Método 2: Verificar URL (no debe contener 'login')
            String urlActual = obtenerUrlActual();
            if (!urlActual.toLowerCase().contains("login")) {
                return true;
            }
            
            // Método 3: Buscar mensaje de bienvenida
            if (estaVisible(BY_MENSAJE_EXITO)) {
                return true;
            }
            
            // Método 4: Verificar que no estamos en página de login
            return !esPaginaVisible();
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Error verificando login exitoso: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Verifica si hay mensaje de error visible
     * @return true si hay error
     */
    public boolean hayMensajeError() {
        return estaVisible(BY_MENSAJE_ERROR);
    }
    
    /**
     * Obtiene el texto del mensaje de error
     * @return texto del error o cadena vacía
     */
    public String obtenerMensajeError() {
        return obtenerTextoSeguro(BY_MENSAJE_ERROR);
    }
    
    /**
     * Verifica que los campos estén llenos antes de enviar
     */
    private void verificarCamposLlenos() {
        try {
            String valorUsuario = campoUsuario.getAttribute("value");
            String valorPassword = campoPassword.getAttribute("value");
            
            if (valorUsuario == null || valorUsuario.trim().isEmpty()) {
                throw new RuntimeException("Campo usuario está vacío");
            }
            
            if (valorPassword == null || valorPassword.trim().isEmpty()) {
                throw new RuntimeException("Campo password está vacío");
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Campos verificados - llenos correctamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error verificando campos: " + e.getMessage()));
            throw new RuntimeException("Los campos no están llenos correctamente", e);
        }
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Limpia los campos de login
     */
    public void limpiarCamposLogin() {
        try {
            if (estaPresente(BY_CAMPO_USUARIO)) {
                WebElement campoUser = buscarElemento(BY_CAMPO_USUARIO);
                campoUser.clear();
            }
            
            if (estaPresente(BY_CAMPO_PASSWORD)) {
                WebElement campoPass = buscarElemento(BY_CAMPO_PASSWORD);
                campoPass.clear();
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Campos de login limpiados"));
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error limpiando campos: " + e.getMessage()));
        }
    }
    
    /**
     * Espera a que desaparezca el indicador de carga
     */
    private void esperarDesaparicionIndicadorCarga() {
        try {
            // Esperar hasta 10 segundos a que desaparezca el indicador de carga
            espera.until(ExpectedConditions.invisibilityOfElementLocated(
                By.cssSelector(".loading, .spinner")));
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Indicador de carga desapareció"));
            
        } catch (Exception e) {
            // Si no hay indicador de carga o no desaparece, continuar
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "No hay indicador de carga o no desapareció"));
        }
    }
    
    /**
     * Obtiene información del estado actual de la página
     * @return información de estado para debugging
     */
    public String obtenerEstadoPagina() {
        StringBuilder estado = new StringBuilder();
        estado.append("=== ESTADO PÁGINA LOGIN ===\n");
        estado.append("URL actual: ").append(obtenerUrlActual()).append("\n");
        estado.append("Título: ").append(obtenerTituloPagina()).append("\n");
        estado.append("Formulario visible: ").append(esPaginaVisible()).append("\n");
        estado.append("Hay error: ").append(hayMensajeError()).append("\n");
        estado.append("Login exitoso: ").append(esLoginExitoso()).append("\n");
        
        if (hayMensajeError()) {
            estado.append("Mensaje error: ").append(obtenerMensajeError()).append("\n");
        }
        
        estado.append("===============================");
        
        return estado.toString();
    }
    
    /**
     * Realiza logout si estamos logueados
     * UTILIDAD: Para limpiar estado entre pruebas
     */
    public void realizarLogout() {
        try {
            // Buscar botón de logout común
            By[] selectoresLogout = {
                By.id("logout"),
                By.cssSelector(".logout"),
                By.cssSelector("[data-action='logout']"),
                By.linkText("Logout"),
                By.linkText("Cerrar Sesión")
            };
            
            for (By selector : selectoresLogout) {
                if (estaPresente(selector)) {
                    WebElement botonLogout = buscarElemento(selector);
                    clickSeguro(botonLogout);
                    
                    logger.info(TipoMensaje.INFORMATIVO.formatearMensaje("Logout realizado"));
                    
                    // Esperar a volver a página de login
                    Thread.sleep(2000);
                    return;
                }
            }
            
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se encontró botón de logout"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error realizando logout: " + e.getMessage()));
        }
    }
}