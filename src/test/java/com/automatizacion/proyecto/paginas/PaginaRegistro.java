// ===============================================
// ARCHIVO: src/test/java/com/automatizacion/proyecto/paginas/PaginaRegistro.java
// VERSIÓN CORREGIDA FINAL
// ===============================================
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
        
        try {
            // Scroll para asegurar que los elementos están visibles
            manejadorScroll.scrollAlInicio();
            Thread.sleep(500);
            
            if (datos.getNombre() != null && !datos.getNombre().isEmpty()) {
                logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando nombre: " + datos.getNombre()));
                ingresarNombre(datos.getNombre());
                Thread.sleep(500); // Pausa entre campos
            }
            
            if (datos.getEmail() != null && !datos.getEmail().isEmpty()) {
                logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando email: " + datos.getEmail()));
                ingresarEmail(datos.getEmail());
                Thread.sleep(500);
            }
            
            if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
                logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando password"));
                ingresarPassword(datos.getPassword());
                Thread.sleep(500);
            }
            
            if (datos.getConfirmacionPassword() != null && !datos.getConfirmacionPassword().isEmpty()) {
                logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Ingresando confirmación password"));
                ingresarConfirmarPassword(datos.getConfirmacionPassword());
                Thread.sleep(500);
            }
            
            if (datos.isAceptarTerminos()) {
                logger.debug(TipoMensaje.PASO_PRUEBA.formatearMensaje("Aceptando términos"));
                aceptarTerminos();
                Thread.sleep(500);
            }
            
            // Scroll final para mostrar todo el formulario lleno
            scrollParaCaptura();
            Thread.sleep(1000);
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Formulario de registro completado correctamente"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error llenando formulario: " + e.getMessage()));
            throw new RuntimeException("Error llenando formulario", e);
        }
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
            // 1. Scroll al inicio para ver el formulario
            manejadorScroll.scrollAlInicio();
            Thread.sleep(1000);
            
            // 2. Llenar formulario
            llenarFormularioCompleto(datos);
            
            // 3. Captura ANTES de hacer click (con datos visibles)
            capturarPantalla("formulario_lleno_" + datos.getCasoPrueba());
            
            // 4. Hacer click en registrar
            clickBotonRegistrar();
            
            // 5. Esperar procesamiento
            Thread.sleep(3000); // Más tiempo para procesamiento
            
            // 6. Verificar resultado
            if (datos.isEsValido()) {
                boolean exitoso = verificarRegistroExitoso();
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro completado - Exitoso: " + exitoso));
                
                if (!exitoso) {
                    // Debug: capturar estado actual para análisis
                    capturarPantalla("debug_registro_no_exitoso_" + datos.getCasoPrueba());
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("URL actual: " + driver.getCurrentUrl()));
                    logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje("Título actual: " + driver.getTitle()));
                }
                
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
            // Esperar un poco más para que se procese la respuesta
            Thread.sleep(2000);
            
            // 1. Verificar mensaje de éxito
            if (esElementoVisible(mensajeExito)) {
                String mensaje = obtenerTextoSeguro(mensajeExito);
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro exitoso detectado con mensaje: " + mensaje));
                return true;
            }
            
            // 2. Verificar redirección por URL
            String urlActual = driver.getCurrentUrl();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("URL actual para verificación: " + urlActual));
            
            if (urlActual.contains("success") || 
                urlActual.contains("welcome") || 
                urlActual.contains("dashboard") ||
                urlActual.contains("home") ||
                !urlActual.contains("register")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro exitoso detectado por cambio de URL: " + urlActual));
                return true;
            }
            
            // 3. Verificar cambio en el título
            String titulo = obtenerTituloPagina();
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Título actual: " + titulo));
            
            if (titulo.toLowerCase().contains("welcome") || 
                titulo.toLowerCase().contains("success") ||
                titulo.toLowerCase().contains("home") ||
                !titulo.toLowerCase().contains("register")) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro exitoso detectado por cambio de título: " + titulo));
                return true;
            }
            
            // 4. Verificar ausencia de errores
            boolean hayErrores = hayErroresValidacion();
            if (!hayErrores && !esPaginaVisible()) {
                logger.info(TipoMensaje.EXITO.formatearMensaje("Registro exitoso detectado por ausencia de errores y cambio de página"));
                return true;
            }
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("No se detectó registro exitoso"));
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
            boolean hayErrores = hayErroresValidacion();
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
    
    /**
     * Scroll para mostrar formulario completo en capturas
     */
    private void scrollParaCaptura() {
        try {
            // Buscar el formulario de registro
            org.openqa.selenium.WebElement formulario = driver.findElement(org.openqa.selenium.By.tagName("form"));
            if (formulario != null) {
                manejadorScroll.scrollHastaElemento(formulario);
            } else {
                // Si no encuentra form, scroll al primer input
                org.openqa.selenium.WebElement primerInput = driver.findElement(org.openqa.selenium.By.tagName("input"));
                manejadorScroll.scrollHastaElemento(primerInput);
            }
        } catch (Exception e) {
            manejadorScroll.scrollAlInicio(); // Fallback
            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Scroll para captura - fallback al inicio"));
        }
    }
}