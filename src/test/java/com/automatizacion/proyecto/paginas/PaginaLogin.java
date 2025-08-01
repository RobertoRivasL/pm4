package test.java.com.automatizacion.proyecto.paginas;

import test.java.com.automatizacion.proyecto.datos.datos.ModeloDatosPrueba;
import main.java.com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaLogin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de inicio de sesión implementando el patrón Page Object Model.
 * Encapsula todos los elementos y acciones relacionadas con el login.
 * 
 * Implementa la interfaz IPaginaLogin siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaLogin extends PaginaBase implements IPaginaLogin {

    private static final Logger logger = LoggerFactory.getLogger(PaginaLogin.class);

    private PaginaLogin paginaLogin;
    private PaginaRegistro paginaRegistro;

    // === ELEMENTOS DE LA PÁGINA ===

    @FindBy(id = "email")
    private WebElement campoEmail;

    @FindBy(id = "password")
    private WebElement campoPassword;

    @FindBy(id = "loginButton")
    private WebElement botonLogin;

    @FindBy(id = "forgotPasswordLink")
    private WebElement enlaceOlvidoPassword;

    @FindBy(id = "registerLink")
    private WebElement enlaceRegistro;

    @FindBy(className = "login-form")
    private WebElement formularioLogin;

    @FindBy(className = "error-message")
    private WebElement mensajeError;

    @FindBy(className = "success-message")
    private WebElement mensajeExito;

    @FindBy(id = "rememberMe")
    private WebElement checkboxRecordarme;

    @FindBy(className = "loading-spinner")
    private WebElement spinnerCarga;

    // Elementos adicionales para validaciones
    @FindBy(xpath = "//div[@class='field-error'][contains(.,'email')]")
    private WebElement errorEmail;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'password')]")
    private WebElement errorPassword;

    @FindBy(id = "loginTitle")
    private WebElement tituloLogin;

    @FindBy(className = "success-message")
    private WebElement mensajeBienvenida;

    /**
     * Constructor que inicializa la página con el driver
     * 
     * @param driver instancia de WebDriver
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaLogin inicializada"));
    }

    // === IMPLEMENTACIÓN DE INTERFAZ IPaginaLogin ===

    @Override
    public boolean esPaginaVisible() {
        try {
            return esperarElementoVisible(formularioLogin, 10) != null &&
                    esperarElementoVisible(campoEmail, 5) != null &&
                    esperarElementoVisible(campoPassword, 5) != null &&
                    esperarElementoVisible(botonLogin, 5) != null;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando visibilidad de página login: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public void navegarAtras() {
        driver.navigate().back();
    }

    @Override
    public boolean esperarCargaPagina(int segundos) {
        try {
            return esperarElementoVisible(formularioLogin, segundos) != null;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error esperando carga de la página de login: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public void ingresarCredenciales(String email, String password) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Ingresando credenciales - Email: " + enmascararTexto(email)));

            ingresarEmail(email);
            ingresarPassword(password);

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error al ingresar credenciales: " + e.getMessage()));
            throw new RuntimeException("No se pudieron ingresar las credenciales", e);
        }
    }

    @Override
    public void ingresarEmail(String email) {
        try {
            esperarElementoClicable(campoEmail, 10);
            limpiarYEscribir(campoEmail, email);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Email ingresado: " + enmascararTexto(email)));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error al ingresar email: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el email", e);
        }
    }

    @Override
    public void ingresarPassword(String password) {
        try {
            esperarElementoClicable(campoPassword, 10);
            limpiarYEscribir(campoPassword, password);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Password ingresado"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error al ingresar password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el password", e);
        }
    }

    @Override
    public void clickBotonLogin() {
        try {
            esperarElementoClicable(botonLogin, 10);
            hacerClick(botonLogin);

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Click en botón Login ejecutado"));

            // Esperar que desaparezca el spinner si está presente
            if (esElementoVisible(spinnerCarga)) {
                esperarElementoInvisible(spinnerCarga, 15);
            }

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error al hacer click en botón login: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en el botón login", e);
        }
    }

    @Override
    public boolean iniciarSesion(ModeloDatosPrueba datos) {
        try {
            if (!datos.camposLoginCompletos()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Datos de login incompletos"));
                return false;
            }

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Iniciando sesión con: " + datos.generarResumen()));

            ingresarCredenciales(datos.getEmail(), datos.getPassword());
            clickBotonLogin();

            // Verificar resultado
            if (datos.isEsValido()) {
                return verificarLoginExitoso();
            } else {
                return verificarLoginFallido();
            }

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error en proceso de inicio de sesión: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean verificarLoginExitoso() {
        try {
            // Ajusta el selector según tu página real
            return esElementoVisible(mensajeBienvenida) || fueRedirigidoADashboard();
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando login exitoso: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean verificarLoginFallido() {
        try {
            return esElementoVisible(mensajeError) ||
                    esElementoVisible(errorEmail) ||
                    esElementoVisible(errorPassword);

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando login fallido: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public String obtenerMensajeError() {
        try {
            if (esElementoVisible(mensajeError)) {
                return obtenerTexto(mensajeError);
            }

            if (esElementoVisible(errorEmail)) {
                return obtenerTexto(errorEmail);
            }

            if (esElementoVisible(errorPassword)) {
                return obtenerTexto(errorPassword);
            }

            return "";

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error obteniendo mensaje de error: " + e.getMessage()));
            return "";
        }
    }

    // === MÉTODOS ADICIONALES ESPECÍFICOS DE ESTA PÁGINA ===

    /**
     * Activa o desactiva el checkbox "Recordarme"
     * 
     * @param activar true para activar, false para desactivar
     */
    public void establecerRecordarme(boolean activar) {
        try {
            if (esElementoVisible(checkboxRecordarme)) {
                boolean estaSeleccionado = checkboxRecordarme.isSelected();

                if (activar && !estaSeleccionado) {
                    hacerClick(checkboxRecordarme);
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("Checkbox 'Recordarme' activado"));
                } else if (!activar && estaSeleccionado) {
                    hacerClick(checkboxRecordarme);
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("Checkbox 'Recordarme' desactivado"));
                }
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error estableciendo checkbox recordarme: " + e.getMessage()));
        }
    }

    /**
     * Hace click en el enlace "Olvidé mi contraseña"
     */
    public void clickOlvidoPassword() {
        try {
            esperarElementoClicable(enlaceOlvidoPassword, 10);
            hacerClick(enlaceOlvidoPassword);

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Click en enlace 'Olvidé mi contraseña'"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error al hacer click en olvido password: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en olvido password", e);
        }
    }

    /**
     * Navega a la página de registro
     */
    public void irARegistro() {
        try {
            esperarElementoClicable(enlaceRegistro, 10);
            hacerClick(enlaceRegistro);

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Navegando a página de registro"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error navegando a registro: " + e.getMessage()));
            throw new RuntimeException("No se pudo navegar a registro", e);
        }
    }

    /**
     * Verifica si el formulario está en estado de carga
     * 
     * @return true si está cargando
     */
    public boolean estaEnCarga() {
        return esElementoVisible(spinnerCarga);
    }

    /**
     * Obtiene el título de la página de login
     * 
     * @return texto del título
     */
    public String obtenerTitulo() {
        try {
            if (esElementoVisible(tituloLogin)) {
                return obtenerTexto(tituloLogin);
            }
            return driver.getTitle();
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error obteniendo título: " + e.getMessage()));
            return "";
        }
    }

    /**
     * Verifica si todos los elementos principales están visibles
     * 
     * @return true si todos los elementos están presentes
     */
    @Override
    public void validarElementosPagina() {
        try {
            boolean todosPresentes = esElementoVisible(campoEmail) &&
                    esElementoVisible(campoPassword) &&
                    esElementoVisible(botonLogin) &&
                    esElementoVisible(formularioLogin);

            if (todosPresentes) {
                logger.info(TipoMensaje.VALIDACION.formatearMensaje(
                        "Todos los elementos de la página login están presentes"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Faltan elementos en la página login"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error validando elementos de página: " + e.getMessage()));
        }
    }

    /**
     * Limpia todos los campos del formulario
     */
    public void limpiarFormulario() {
        try {
            if (esElementoVisible(campoEmail)) {
                campoEmail.clear();
            }
            if (esElementoVisible(campoPassword)) {
                campoPassword.clear();
            }

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario de login limpiado"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error limpiando formulario: " + e.getMessage()));
        }
    }

    /**
     * Verifica si hay errores de validación en los campos
     * 
     * @return true si hay errores visibles
     */
    public boolean hayErroresValidacion() {
        return esElementoVisible(errorEmail) ||
                esElementoVisible(errorPassword) ||
                esElementoVisible(mensajeError);
    }

    @Override
    public void actualizarPagina() {
        // Implementa la lógica necesaria aquí
    }

    /**
     * Enmascara texto sensible para logs
     * 
     * @param texto texto a enmascarar
     * @return texto enmascarado
     */
    private String enmascararTexto(String texto) {
        if (texto == null || texto.length() < 3) {
            return "***";
        }

        if (texto.contains("@")) {
            // Es un email
            int posArroba = texto.indexOf('@');
            return texto.substring(0, 2) + "***@" + texto.substring(posArroba + 1);
        }

        // Texto genérico
        return texto.substring(0, 2) + "***";
    }

    public boolean fueRedirigidoADashboard() {
        // Ajusta el selector según la URL o algún elemento único del dashboard/área
        // segura
        return driver.getCurrentUrl().contains("/secure");
    }
}