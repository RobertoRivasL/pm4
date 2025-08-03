package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaRegistro;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de registro implementando el patrón Page Object Model.
 * Específicamente diseñada para https://practice.expandtesting.com/register
 * 
 * Implementa la interfaz IPaginaRegistro siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaRegistro extends PaginaBase implements IPaginaRegistro {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);
    
    // === ELEMENTOS DE LA PÁGINA DE REGISTRO ===
    
    // Campos del formulario
    @FindBy(id = "username")
    private WebElement campoUsername;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(id = "confirmPassword")
    private WebElement campoConfirmPassword;
    
    // Botón de registro
    @FindBy(xpath = "//button[@data-testid='register-submit']")
    private WebElement botonRegistro;
    
    // Mensajes de estado
    @FindBy(xpath = "//div[contains(@class, 'alert-success')]")
    private WebElement mensajeExito;
    
    @FindBy(xpath = "//div[contains(@class, 'alert-danger')]")
    private WebElement mensajeError;
    
    // Enlaces de navegación
    @FindBy(xpath = "//a[contains(@href, '/login')]")
    private WebElement enlaceLogin;
    
    // Título de la página
    @FindBy(xpath = "//h1[contains(text(), 'Test Register page')]")
    private WebElement tituloPagina;
    
    // Selectores adicionales para validaciones
    @FindBy(xpath = "//input[@id='username']//following-sibling::div[contains(@class, 'invalid-feedback')]")
    private WebElement errorUsername;
    
    @FindBy(xpath = "//input[@id='password']//following-sibling::div[contains(@class, 'invalid-feedback')]")
    private WebElement errorPassword;
    
    @FindBy(xpath = "//input[@id='confirmPassword']//following-sibling::div[contains(@class, 'invalid-feedback')]")
    private WebElement errorConfirmPassword;
    
    /**
     * Constructor que inicializa la página
     * @param driver instancia de WebDriver
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.info(TipoMensaje.CONFIGURACION.formatearMensaje("PaginaRegistro inicializada"));
    }
    
    // === IMPLEMENTACIÓN DE LA INTERFAZ IPaginaRegistro ===
    
    @Override
    public boolean esPaginaVisible() {
        try {
            boolean tituloVisible = esElementoVisible(tituloPagina);
            boolean formularioVisible = esElementoVisible(campoUsername) && 
                                      esElementoVisible(campoPassword) && 
                                      esElementoVisible(campoConfirmPassword);
            
            boolean paginaVisible = tituloVisible && formularioVisible;
            
            if (paginaVisible) {
                logger.info(TipoMensaje.VALIDACION.formatearMensaje("Página de registro visible"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Página de registro no completamente visible"));
            }
            
            return paginaVisible;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando visibilidad de página: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean validarElementosPagina() {
        try {
            logger.info(TipoMensaje.VALIDACION.formatearMensaje("Validando elementos de la página de registro"));
            
            // Verificar elementos críticos
            boolean elementosPresentes = 
                espera.esperarElementoVisible(campoUsername) != null &&
                espera.esperarElementoVisible(campoPassword) != null &&
                espera.esperarElementoVisible(campoConfirmPassword) != null &&
                espera.esperarElementoVisible(botonRegistro) != null;
            
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
    
    @Override
    public boolean registrarUsuario(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                "Iniciando registro de usuario: " + datos.obtenerNombreCompleto()));
            
            // Limpiar formulario primero
            limpiarFormulario();
            
            // Completar campos según Practice Expand Testing
            if (datos.getEmail() != null) {
                ingresarUsername(datos.getEmail()); // En este sitio, username es el email
            }
            
            if (datos.getPassword() != null) {
                ingresarPassword(datos.getPassword());
            }
            
            if (datos.getConfirmacionPassword() != null) {
                ingresarConfirmPassword(datos.getConfirmacionPassword());
            }
            
            // Hacer clic en registrar
            clickBotonRegistro();
            
            // Esperar resultado
            Thread.sleep(2000); // Pequeña pausa para que se procese
            
            // Verificar resultado
            boolean registroExitoso = verificarRegistroExitoso();
            
            if (registroExitoso) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro completado exitosamente"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("El registro no fue exitoso"));
            }
            
            return registroExitoso;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en proceso de registro: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean completarCamposObligatorios(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Completando campos obligatorios"));
            
            // Solo campos obligatorios según la página
            if (datos.getEmail() != null) {
                ingresarUsername(datos.getEmail());
            }
            
            if (datos.getPassword() != null) {
                ingresarPassword(datos.getPassword());
            }
            
            if (datos.getConfirmacionPassword() != null) {
                ingresarConfirmPassword(datos.getConfirmacionPassword());
            }
            
            return true;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error completando campos obligatorios: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean completarTodosLosCampos(ModeloDatosPrueba datos) {
        // En Practice Expand Testing, solo hay campos obligatorios
        return completarCamposObligatorios(datos);
    }
    
    @Override
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
    
    @Override
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
    
    @Override
    public void ingresarConfirmPassword(String confirmPassword) {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ingresando confirmación de contraseña"));
            
            WebElement campo = espera.esperarElementoVisible(campoConfirmPassword);
            campo.clear();
            campo.sendKeys(confirmPassword);
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error ingresando confirmación: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar confirmación de contraseña", e);
        }
    }
    
    @Override
    public void clickBotonRegistro() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Haciendo clic en botón de registro"));
            
            WebElement boton = espera.esperarElementoClickeable(botonRegistro);
            boton.click();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error haciendo clic en botón registro: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer clic en botón de registro", e);
        }
    }
    
    @Override
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
            
            if (esElementoVisible(errorConfirmPassword)) {
                errores.append("Confirm Password: ").append(errorConfirmPassword.getText()).append(" ");
            }
            
            return errores.toString().trim();
            
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se encontraron mensajes de error"));
            return "";
        }
    }
    
    @Override
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
    
    @Override
    public void limpiarFormulario() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Limpiando formulario de registro"));
            
            if (esElementoVisible(campoUsername)) {
                campoUsername.clear();
            }
            
            if (esElementoVisible(campoPassword)) {
                campoPassword.clear();
            }
            
            if (esElementoVisible(campoConfirmPassword)) {
                campoConfirmPassword.clear();
            }
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error limpiando formulario: " + e.getMessage()));
        }
    }
    
    @Override
    public void irALogin() {
        try {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Navegando a página de login"));
            
            WebElement enlace = espera.esperarElementoClickeable(enlaceLogin);
            enlace.click();
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error navegando a login: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a login", e);
        }
    }
    
    @Override
    public boolean esFormularioHabilitado() {
        try {
            return campoUsername.isEnabled() && 
                   campoPassword.isEnabled() && 
                   campoConfirmPassword.isEnabled();
                   
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando estado del formulario"));
            return false;
        }
    }
    
    @Override
    public boolean esBotonRegistroHabilitado() {
        try {
            return botonRegistro.isEnabled();
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando estado del botón"));
            return false;
        }
    }
    
    // === MÉTODOS PRIVADOS DE APOYO ===
    
    /**
     * Verifica si el registro fue exitoso
     * @return true si hay evidencia de registro exitoso
     */
    private boolean verificarRegistroExitoso() {
        try {
            // Verificar mensaje de éxito
            String mensajeExitoso = obtenerMensajeExito();
            if (!mensajeExitoso.isEmpty()) {
                return true;
            }
            
            // Verificar si hay redirección o cambio de URL
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("login") || urlActual.contains("success")) {
                return true;
            }
            
            // Si no hay errores, asumir éxito
            String mensajeErrorActual = obtenerMensajeError();
            return mensajeErrorActual.isEmpty();
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error verificando éxito del registro"));
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

@Override
public boolean hayMensajesError() {
    try {
        List<WebElement> mensajesError = driver.findElements(
            By.xpath("//div[contains(@class, 'error')] | //div[contains(@class, 'alert-danger')] | //span[contains(@class, 'error-message')]")
        );
        return !mensajesError.isEmpty() && mensajesError.stream().anyMatch(WebElement::isDisplayed);
    } catch (Exception e) {
        return false;
    }
}

@Override
public String obtenerMensajeError() {
    try {
        WebElement mensajeError = driver.findElement(
            By.xpath("//div[contains(@class, 'error')] | //div[contains(@class, 'alert-danger')] | //span[contains(@class, 'error-message')]")
        );
        return mensajeError.getText();
    } catch (Exception e) {
        return "";
    }
}

@Override
public void limpiarFormulario() {
    try {
        if (campoNombre.isDisplayed()) campoNombre.clear();
        if (campoApellido.isDisplayed()) campoApellido.clear();
        if (campoEmail.isDisplayed()) campoEmail.clear();
        if (campoPassword.isDisplayed()) campoPassword.clear();
        if (campoConfirmarPassword.isDisplayed()) campoConfirmarPassword.clear();
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario de registro limpiado"));
    } catch (Exception e) {
        logger.error(TipoMensaje.ERROR.formatearMensajeError(
            "Error al limpiar formulario de registro", e));
    }
}

@Override
public void irALogin() {
    try {
        // Buscar enlace a login
        WebElement enlaceLogin = driver.findElement(
            By.xpath("//a[contains(text(), 'Login')] | //a[contains(text(), 'Iniciar')] | //a[contains(@href, 'login')]")
        );
        enlaceLogin.click();
        
        logger.info(TipoMensaje.NAVEGACION.formatearMensaje("Navegando a página de login"));
    } catch (Exception e) {
        logger.error(TipoMensaje.ERROR.formatearMensajeError(
            "Error al navegar a login", e));
    }
}

// CORREGIR visibilidad del método esElementoVisible
@Override
protected boolean esElementoVisible(WebElement elemento) {
    return super.esElementoVisible(elemento);
}

}