package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaRegistro;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PaginaRegistro extends PaginaBase implements IPaginaRegistro {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);
    
    @FindBy(id = "username")
    private WebElement campoNombre;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(id = "confirm-password")
    private WebElement campoConfirmarPassword;
    
    @FindBy(id = "email")
    private WebElement campoEmail;
    
    @FindBy(xpath = "//button[text()='Register']")
    private WebElement botonRegistrar;
    
    @FindBy(id = "terms")
    private WebElement checkboxTerminos;
    
    @FindBy(className = "success-message")
    private WebElement mensajeExito;
    
    @FindBy(className = "error-message")
    private WebElement mensajeError;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'username')]")
    private WebElement errorNombre;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'email')]")
    private WebElement errorEmail;
    
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'password')]")
    private WebElement errorPassword;
    
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaRegistro inicializada"));
    }
    
    @Override
    public boolean esPaginaVisible() {
        try {
            return esperarElementoVisible(campoNombre, 10) && 
                   esperarElementoVisible(campoPassword, 5) &&
                   esperarElementoVisible(botonRegistrar, 5);
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando visibilidad página registro: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public void validarElementosPagina() {
        logger.info(TipoMensaje.VALIDACION.formatearMensaje("Validando elementos de página registro"));
        
        if (!esElementoVisible(campoNombre)) {
            throw new RuntimeException("Campo nombre no está visible");
        }
        if (!esElementoVisible(campoPassword)) {
            throw new RuntimeException("Campo password no está visible");
        }
        if (!esElementoVisible(botonRegistrar)) {
            throw new RuntimeException("Botón registrar no está visible");
        }
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Elementos de página registro validados correctamente"));
    }
    
    @Override
    public void llenarFormularioCompleto(ModeloDatosPrueba datos) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Llenando formulario completo de registro"));
        
        if (datos.getNombre() != null && !datos.getNombre().isEmpty()) {
            ingresarNombre(datos.getNombre());
        }
        if (datos.getEmail() != null && !datos.getEmail().isEmpty()) {
            ingresarEmail(datos.getEmail());
        }
        if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
            ingresarPassword(datos.getPassword());
        }
        if (datos.getConfirmacionPassword() != null && !datos.getConfirmacionPassword().isEmpty()) {
            ingresarConfirmarPassword(datos.getConfirmacionPassword());
        }
        if (datos.isAceptarTerminos()) {
            aceptarTerminos();
        }
        
        logger.info(TipoMensaje.EXITO.formatearMensaje("Formulario de registro completado"));
    }
    
    @Override
    public void ingresarNombre(String nombre) {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando nombre: " + nombre));
        ingresarTextoSeguro(campoNombre, nombre, true);
    }
    
    @Override
    public void ingresarApellido(String apellido) {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Apellido manejado como nombre completo"));
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
    public void ingresarConfirmarPassword(String confirmarPassword) {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando confirmación de password"));
        ingresarTextoSeguro(campoConfirmarPassword, confirmarPassword, true);
    }
    
    @Override
    public void aceptarTerminos() {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Aceptando términos y condiciones"));
        try {
            if (esperarElementoClickeable(checkboxTerminos, TIMEOUT_DEFECTO) && !checkboxTerminos.isSelected()) {
                clickSeguro(checkboxTerminos);
                logger.debug(TipoMensaje.EXITO.formatearMensaje("Términos aceptados"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error aceptando términos: " + e.getMessage()));
        }
    }
    
    @Override
    public void clickBotonRegistrar() {
        logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Haciendo click en botón registrar"));
        clickSeguro(botonRegistrar);
    }
    
    @Override
    public boolean registrarUsuario(ModeloDatosPrueba datos) {
        logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Iniciando proceso de registro de usuario"));
        
        try {
            llenarFormularioCompleto(datos);
            clickBotonRegistrar();
            Thread.sleep(2000); // Espera para procesos
            
            if (datos.isEsValido()) {
                boolean exitoso = verificarRegistroExitoso();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro completado - Exitoso: " + exitoso));
                return exitoso;
            } else {
                boolean fallido = verificarRegistroFallido();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro completado - Falló como esperado: " + fallido));
                return fallido;
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error en proceso de registro: " + e.getMessage()));
            capturarPantalla("error_registro_" + System.currentTimeMillis());
            return false;
        }
    }
    
    @Override
    public boolean verificarRegistroExitoso() {
        try {
            if (esElementoVisible(mensajeExito)) {
                String mensaje = obtenerTextoSeguro(mensajeExito);
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro exitoso detectado: " + mensaje));
                return true;
            }
            
            String urlActual = driver.getCurrentUrl();
            if (urlActual.contains("success") || urlActual.contains("welcome") || urlActual.contains("dashboard")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Redirección exitosa detectada: " + urlActual));
                return true;
            }
            
            return false;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando registro exitoso: " + e.getMessage()));
            return false;
        }
    }
    
    @Override
    public boolean verificarRegistroFallido() {
        try {
            boolean enPaginaRegistro = esPaginaVisible();
            boolean hayErrores = hayErroresValidacion(); // Ahora usa el método público
            boolean hayMensajeError = esElementoVisible(mensajeError);
            
            boolean registroFallo = enPaginaRegistro && (hayErrores || hayMensajeError);
            
            if (registroFallo) {
                String mensajeError = obtenerMensajeError();
                logger.info(TipoMensaje.VALIDACION.formatearMensaje("Registro falló como esperado: " + mensajeError));
            }
            
            return registroFallo;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error verificando registro fallido: " + e.getMessage()));
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
            
            if (esElementoVisible(errorNombre)) {
                erroresCompletos.append(obtenerTextoSeguro(errorNombre)).append("; ");
            }
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
    
    // ===============================================
    // MÉTODO PÚBLICO AGREGADO PARA RESOLVER EL ERROR
    // ===============================================
    
    /**
     * Verifica si hay errores de validación en la página (método público)
     * @return true si hay errores visibles
     */
    public boolean hayErroresValidacion() {
        try {
            return driver.findElements(org.openqa.selenium.By.cssSelector(".error-message, .field-error, .alert-danger"))
                        .stream().anyMatch(org.openqa.selenium.WebElement::isDisplayed);
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Error verificando errores de validación: " + e.getMessage()));
            return false;
        }
    }
}