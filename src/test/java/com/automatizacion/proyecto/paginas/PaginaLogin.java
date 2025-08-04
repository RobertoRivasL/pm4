package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaLogin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginaLogin extends PaginaBase implements IPaginaLogin {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaLogin.class);
    
    @FindBy(id = "username")
    private WebElement campoEmail;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(xpath = "//button[text()='Login']")
    private WebElement botonLogin;
    
    @FindBy(className = "success-message")
    private WebElement mensajeExito;
    
    @FindBy(className = "error-message")
    private WebElement mensajeError;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'email')]")
    private WebElement errorEmail;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'password')]")
    private WebElement errorPassword;
    
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaLogin inicializada"));
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            return esperarElementoVisible(campoEmail, 10) && 
                   esperarElementoVisible(campoPassword, 5) &&
                   esperarElementoVisible(botonLogin, 5);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando visibilidad página login: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public void validarElementosPagina() {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje("Validando elementos de página login"));
        
        if (!esElementoVisible(campoEmail)) {
            throw new RuntimeException("Campo email no está visible");
        }
        if (!esElementoVisible(campoPassword)) {
            throw new RuntimeException("Campo password no está visible");
        }
        if (!esElementoVisible(botonLogin)) {
            throw new RuntimeException("Botón login no está visible");
        }
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Elementos de página login validados correctamente"));
    }
    
    @Override
    public void ingresarCredenciales(String email, String password) {
        ingresarEmail(email);
        ingresarPassword(password);
    }
    
    @Override
    public void ingresarEmail(String email) {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando email: " + email));
        ingresarTextoSeguro(campoEmail, email, true);
    }
    
    @Override
    public void ingresarPassword(String password) {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando password"));
        ingresarTextoSeguro(campoPassword, password, true);
    }
    
    @Override
    public void clickBotonLogin() {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Haciendo click en botón login"));
        clickSeguro(botonLogin);
    }
    
    @Override
    public boolean iniciarSesion(ModeloDatosPrueba datos) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Iniciando proceso de login"));
        
        try {
            ingresarCredenciales(datos.getEmail(), datos.getPassword());
            clickBotonLogin();
            Thread.sleep(2000); // Espera para procesos
            
            if (datos.isEsValido()) {
                boolean exitoso = verificarLoginExitoso();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login completado - Exitoso: " + exitoso));
                return exitoso;
            } else {
                boolean fallido = verificarLoginFallido();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login completado - Falló como esperado: " + fallido));
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
            if (esElementoVisible(mensajeExito)) {
                String mensaje = obtenerTextoSeguro(mensajeExito);
                logger.info(TipoMensaje.EXITO.formatearMensaje("Login exitoso detectado: " + mensaje));
                return true;
            }
            
            String urlActual = driver.getCurrentUrl();
            if (urlActual.contains("secure") || urlActual.contains("welcome") || urlActual.contains("dashboard")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Redirección exitosa detectada: " + urlActual));
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando login exitoso: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean verificarLoginFallido() {
        try {
            boolean enPaginaLogin = esPaginaVisible();
            boolean hayErrores = hayErroresValidacion();
            boolean hayMensajeError = esElementoVisible(mensajeError);
            
            boolean loginFallo = enPaginaLogin && (hayErrores || hayMensajeError);
            
            if (loginFallo) {
                String mensajeError = obtenerMensajeError();
                logger.info(TipoMensaje.VALIDACION.formatearMensaje("Login falló como esperado: " + mensajeError));
            }
            
            return loginFallo;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando login fallido: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public String obtenerMensajeError() {
        try {
            if (esElementoVisible(mensajeError)) {
                String mensaje = obtenerTextoSeguro(mensajeError);
                if (!mensaje.isEmpty()) {
                    return mensaje;
                }
            }
            
            StringBuilder erroresCompletos = new StringBuilder();
            
            if (esElementoVisible(errorEmail)) {
                erroresCompletos.append(obtenerTextoSeguro(errorEmail)).append("; ");
            }
            if (esElementoVisible(errorPassword)) {
                erroresCompletos.append(obtenerTextoSeguro(errorPassword)).append("; ");
            }
            
            String resultado = erroresCompletos.toString();
            return resultado.endsWith("; ") ? resultado.substring(0, resultado.length() - 2) : resultado;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Error obteniendo mensaje de error: " + e.getMessage()));
            return "";
        }
    }
}