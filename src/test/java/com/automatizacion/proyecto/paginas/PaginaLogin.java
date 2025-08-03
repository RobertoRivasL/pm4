package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaLogin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de login implementando el patrón Page Object Model.
 * Específicamente diseñada para https://practice.expandtesting.com/login
 * 
 * Extiende PaginaBase para heredar funcionalidad común y sigue
 * los principios SOLID de diseño.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaLogin extends PaginaBase implements IPaginaLogin {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaLogin.class);
    
    // === ELEMENTOS DE LA PÁGINA DE LOGIN ===
    
    // Campos del formulario
    @FindBy(id = "username")
    private WebElement campoUsername;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    // Botón de login
    @FindBy(xpath = "//button[@data-testid='login-submit']")
    private WebElement botonLogin;
    
    // Mensajes de estado
    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    private WebElement mensajeExito;
    
    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]")
    private WebElement mensajeError;
    
    // Enlaces de navegación
    @FindBy(xpath = "//a[contains(@href, '/register')]")
    private WebElement enlaceRegistro;
    
    @FindBy(xpath = "//a[contains(text(), 'here')]")
    private WebElement enlaceRegistroAlternativo;
    
    // Título de la página
    @FindBy(xpath = "//h1[contains(text(), 'Test Login page')]")
    private WebElement tituloPagina;
    
    // Información de credenciales válidas (mostrada en la página)
    @FindBy(xpath = "//strong[contains(text(), 'practice')]")
    private WebElement usuarioValido;
    
    @FindBy(xpath = "//strong[contains(text(), 'SuperSecretPassword!')]")
    private WebElement passwordValido;
    
    // Mensajes de validación específicos
    @FindBy(xpath = "//input[@id='username']//following-sibling::div[contains(@class, 'invalid-feedback')]")
    private WebElement errorUsername;
    
    @FindBy(xpath = "//input[@id='password']//following-sibling::div[contains(@class, 'invalid-feedback')]")
    private WebElement errorPassword;
    
    /**
     * Constructor que inicializa la página
     * @param driver instancia de WebDriver
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("PaginaLogin inicializada"));
    }
    
    // === MÉTODOS PÚBLICOS DE LA PÁGINA ===
    
    /**
     * Verifica si la página de login está visible y cargada
     * @return true si la página está visible
     */
    public boolean esPaginaVisible() {
        try {
            boolean tituloVisible = esElementoVisible(tituloPagina);
            boolean formularioVisible = esElementoVisible(campoUsername) && 
                                      esElementoVisible(campoPassword) && 
                                      esElementoVisible(botonLogin);
            
            boolean paginaVisible = tituloVisible && formularioVisible;
            
            if (paginaVisible) {
                logger.info(TipoMensaje.VALIDACION.formatearMensaje("Página de login visible"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Página de login no completamente visible"));
            }
            
            return paginaVisible;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando visibilidad de página: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Valida que todos los elementos esperados estén presentes en la página
     * @return true si todos los elementos están presentes
     */
    public boolean validarElementosPagina() {
        try {
            logger.info(TipoMensaje.VALIDACION.formatearMensaje("Validando elementos de la página de login"));
            
            // Verificar elementos críticos
            boolean elementosPresentes = 
                espera.esperarElementoVisible(campoUsername) != null &&
                espera.esperarElementoVisible(campoPassword) != null &&
                espera.esperarElementoVisible(botonLogin) != null;
            
            if (elementosPresentes) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Todos los elementos están presentes"));
            } else {
                logger.error(TipoMensaje.ERROR.formatearMensaje("Faltan elementos en la página"));
            }
            
            return elementosPresentes;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error validando elementos: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Realiza el proceso completo de inicio de sesión
     * @param datos datos de login (email/username y password)
     * @return true si el login fue exitoso
     */
    public boolean iniciarSesion(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Iniciando sesión para usuario: " + datos.getEmail()));
            
            // Limpiar formulario primero
            limpiarFormulario();
            
            // Ingresar credenciales
            if (datos.getEmail() != null) {
                ingresarUsername(datos.getEmail());
            }
            
            if (datos.getPassword() != null) {
                ingresarPassword(datos.getPassword());
            }
            
            // Hacer clic en login
            clickBotonLogin();
            
            // Esperar resultado
            Thread.sleep(2000); // Pequeña pausa para que se procese
            
            // Verificar resultado
            boolean loginExitoso = verificarLoginExitosoInterno();
            
            if (loginExitoso) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login completado exitosamente"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("El login no fue exitoso"));
            }
            
            return loginExitoso;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en proceso de login: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Ingresa el nombre de usuario
     * @param username nombre de usuario
     */
    public void ingresarUsername(String username) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando username: " + username));
            
            WebElement campo = espera.esperarElementoVisible(campoUsername);
            campo.clear();
            campo.sendKeys(username);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando username: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar username", e);
        }
    }
    
    /**
     * Ingresa la contraseña
     * @param password contraseña
     */
    public void ingresarPassword(String password) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando contraseña"));
            
            WebElement campo = espera.esperarElementoVisible(campoPassword);
            campo.clear();
            campo.sendKeys(password);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando contraseña: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar contraseña", e);
        }
    }
    
    /**
     * Hace clic en el botón de login
     */
    public void clickBotonLogin() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo clic en botón de login"));
            
            WebElement boton = espera.esperarElementoClickeable(botonLogin);
            boton.click();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error haciendo clic en botón login: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer clic en botón de login", e);
        }
    }
    
    /**
     * Obtiene mensaje de error si existe
     * @return mensaje de error o cadena vacía
     */
    public String obtenerMensajeError() {
        try {
            // Intentar obtener mensaje de error general
            if (esElementoVisible(mensajeError)) {
                return mensajeError.getText().trim();
            }
            
            // Verificar errores específicos de campos
            StringBuilder errores = new StringBuilder();
            
            if (esElementoVisible(errorUsername)) {
                errores.append("Username: ").append(errorUsername.getText()).append(" ");
            }
            
            if (esElementoVisible(errorPassword)) {
                errores.append("Password: ").append(errorPassword.getText()).append(" ");
            }
            
            return errores.toString().trim();
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se encontraron mensajes de error"));
            return "";
        }
    }
    
    /**
     * Obtiene mensaje de éxito si existe
     * @return mensaje de éxito o cadena vacía
     */
    public String obtenerMensajeExito() {
        try {
            if (esElementoVisible(mensajeExito)) {
                return mensajeExito.getText().trim();
            }
            return "";
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se encontró mensaje de éxito"));
            return "";
        }
    }
    
    /**
     * Limpia todos los campos del formulario
     */
    public void limpiarFormulario() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Limpiando formulario de login"));
            
            if (esElementoVisible(campoUsername)) {
                campoUsername.clear();
            }
            
            if (esElementoVisible(campoPassword)) {
                campoPassword.clear();
            }
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error limpiando formulario: " + e.getMessage()));
        }
    }
    
    /**
     * Navega a la página de registro
     */
    public void irARegistro() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Navegando a página de registro"));
            
            WebElement enlace = null;
            
            // Intentar con el enlace principal
            if (esElementoVisible(enlaceRegistro)) {
                enlace = enlaceRegistro;
            } else if (esElementoVisible(enlaceRegistroAlternativo)) {
                enlace = enlaceRegistroAlternativo;
            }
            
            if (enlace != null) {
                WebElement enlaceClickeable = espera.esperarElementoClickeable(enlace);
                enlaceClickeable.click();
            } else {
                // Si no hay enlace, navegar directamente
                navegarA("https://practice.expandtesting.com/register");
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error navegando a registro: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a registro", e);
        }
    }
    
    /**
     * Obtiene las credenciales válidas mostradas en la página
     * @return ModeloDatosPrueba con credenciales válidas
     */
    public ModeloDatosPrueba obtenerCredencialesValidas() {
        try {
            // Según la página, las credenciales válidas son:
            // Username: practice
            // Password: SuperSecretPassword!
            
            return ModeloDatosPrueba.builder()
                    .casoPrueba("CREDENCIALES_VALIDAS")
                    .descripcion("Credenciales válidas mostradas en la página")
                    .email("practice")
                    .password("SuperSecretPassword!")
                    .esValido(true)
                    .build();
                    
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error obteniendo credenciales válidas"));
            return ModeloDatosPrueba.crearDatosValidos();
        }
    }
    
    /**
     * Verifica si el formulario está habilitado para entrada
     * @return true si el formulario está habilitado
     */
    public boolean esFormularioHabilitado() {
        try {
            return campoUsername.isEnabled() && 
                   campoPassword.isEnabled() && 
                   botonLogin.isEnabled();
                   
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando estado del formulario"));
            return false;
        }
    }
    
    /**
     * Verifica si el botón de login está habilitado
     * @return true si el botón está habilitado
     */
    public boolean esBotonLoginHabilitado() {
        try {
            return botonLogin.isEnabled();
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando estado del botón"));
            return false;
        }
    }
    
    /**
     * Realiza login con credenciales válidas predeterminadas
     * @return true si el login fue exitoso
     */
    public boolean loginConCredencialesValidas() {
        ModeloDatosPrueba credencialesValidas = obtenerCredencialesValidas();
        return iniciarSesion(credencialesValidas);
    }
    
    /**
     * Verifica si hay opción de "Recordarme" disponible
     * @return true si está disponible
     */
    @Override
    public boolean tieneOpcionRecordarme() {
        // En practice.expandtesting.com no hay opción "Recordarme"
        return false;
    }
    
    /**
     * Marca o desmarca la opción "Recordarme" si está disponible
     * @param marcar true para marcar, false para desmarcar
     */
    @Override
    public void marcarRecordarme(boolean marcar) {
        // No implementado ya que no existe en esta página
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Opción 'Recordarme' no disponible en esta página"));
    }
    
    /**
     * Verifica si hay enlace de "Olvidé mi contraseña"
     * @return true si el enlace está presente
     */
    @Override
    public boolean tieneEnlaceOlvidoPassword() {
        // En practice.expandtesting.com no hay enlace de olvido de contraseña
        return false;
    }
    
    /**
     * Hace clic en el enlace "Olvidé mi contraseña" si está disponible
     */
    @Override
    public void clickOlvidoPassword() {
        // No implementado ya que no existe en esta página
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Enlace 'Olvidé mi contraseña' no disponible en esta página"));
    }
    
    /**
     * Verifica si el login fue exitoso mediante indicadores visuales
     * @return true si hay evidencia de login exitoso
     */
    @Override
    public boolean verificarLoginExitoso() {
        return verificarLoginExitosoInterno();
    }
    
    /**
     * Realiza logout si está logueado
     * @return true si el logout fue exitoso
     */
    @Override
    public boolean realizarLogout() {
        try {
            // En practice.expandtesting.com, verificar si hay botón logout
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("secure")) {
                // Si estamos en página segura, buscar forma de logout
                driver.navigate().to("https://practice.expandtesting.com/login");
                return true;
            }
            return false;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error realizando logout: " + e.getMessage()));
            return false;
        }
    }
    
    /**
     * Verifica si el usuario está actualmente logueado
     * @return true si está logueado
     */
    @Override
    public boolean estaLogueado() {
        try {
            String urlActual = obtenerUrlActual();
            String tituloActual = obtenerTituloPagina();
            
            // Si no estamos en la página de login y no vemos el formulario, probablemente estamos logueados
            return !urlActual.contains("/login") || !esPaginaVisible();
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando estado de login"));
            return false;
        }
    }
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Verifica si el login fue exitoso (método interno)
     * @return true si hay evidencia de login exitoso
     */
    private boolean verificarLoginExitosoInterno() {
        try {
            // Verificar mensaje de éxito
            String mensajeExitoso = obtenerMensajeExito();
            if (!mensajeExitoso.isEmpty()) {
                return true;
            }
            
            // Verificar si hay redirección o cambio de URL
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("secure") || urlActual.contains("dashboard") || urlActual.contains("home")) {
                return true;
            }
            
            // Verificar si el título cambió (indicando navegación exitosa)
            String tituloActual = obtenerTituloPagina();
            if (!tituloActual.contains("Login")) {
                return true;
            }
            
            // Si no hay errores, podría ser éxito
            String mensajeErrorActual = obtenerMensajeError();
            if (mensajeErrorActual.isEmpty()) {
                // Verificar si aún estamos en la página de login
                return !esPaginaVisible();
            }
            
            return false;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando éxito del login"));
            return false;
        }
    }
    
    /**
     * Verifica si un elemento está visible sin lanzar excepción
     * @param elemento elemento a verificar
     * @return true si está visible
     */
    private boolean esElementoVisible(WebElement elemento) {
        try {
            return elemento != null && elemento.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }
}