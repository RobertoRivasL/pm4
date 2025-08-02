package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.interfaces.IPaginaLogin;
import com.automatizacion.proyecto.utilidades.EsperaExplicita;
import com.automatizacion.proyecto.utilidades.GestorCapturaPantalla;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.By;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de login implementando el patrón Page Object Model.
 * Encapsula todos los elementos y acciones relacionadas con el inicio de
 * sesión.
 * 
 * Implementa la interfaz IPaginaLogin siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaLogin extends PaginaBase {
    public PaginaLogin(WebDriver driver, EsperaExplicita espera, GestorCapturaPantalla gestorCaptura) {
        super(driver, espera, gestorCaptura);
    }

    private static final Logger logger = LoggerFactory.getLogger(PaginaLogin.class);

    // ===== LOCALIZADORES DE ELEMENTOS =====

    @FindBy(id = "email")
    private WebElement campoEmail;

    @FindBy(id = "password")
    private WebElement campoPassword;

    @FindBy(id = "btnLogin")
    private WebElement botonLogin;

    @FindBy(id = "recordarme")
    private WebElement checkboxRecordarme;

    @FindBy(linkText = "¿Olvidaste tu contraseña?")
    private WebElement enlaceOlvidePassword;

    @FindBy(linkText = "Crear cuenta nueva")
    private WebElement enlaceCrearCuenta;

    @FindBy(xpath = "//div[@class='mensaje-error']")
    private WebElement mensajeError;

    @FindBy(xpath = "//div[@class='mensaje-exito']")
    private WebElement mensajeExito;

    @FindBy(xpath = "//div[@class='alerta-bloqueo']")
    private WebElement alertaBloqueo;

    @FindBy(id = "captcha")
    private WebElement campoCaptcha;

    @FindBy(className = "usuario-logueado")
    private WebElement indicadorUsuarioLogueado;

    // Localizadores como constantes
    private static final By FORMULARIO_LOGIN = By.id("formularioLogin");
    private static final By SPINNER_CARGA = By.className("spinner-loading");
    private static final By MODAL_BLOQUEO = By.id("modalBloqueo");
    private static final By BOTON_CERRAR_MODAL = By.className("btn-cerrar-modal");

    /**
     * Constructor que inicializa la página de login.
     * 
     * @param driver WebDriver para interactuar con la página
     */
    public PaginaLogin(WebDriver driver) {
        super(driver);
        logger.info("Inicializando página de login");
    }

    /**
     * Implementación del método de la interfaz para verificar si la página está
     * visible.
     */
    @Override
    public boolean esPaginaVisible() {
        try {
            return esElementoVisible(FORMULARIO_LOGIN) &&
                    campoEmail.isDisplayed() &&
                    campoPassword.isDisplayed() &&
                    botonLogin.isDisplayed();
        } catch (Exception e) {
            logger.warn("Error al verificar visibilidad de página de login: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Ingresa el email en el campo correspondiente.
     */
    @Override
    public void ingresarEmail(String email) {
        try {
            espera.esperarElementoVisible(By.id("email"));
            limpiarEIngresarTexto(campoEmail, email);
            logger.debug("Email ingresado: {}", email);
        } catch (Exception e) {
            logger.error("Error al ingresar email: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el email", e);
        }
    }

    /**
     * Ingresa la contraseña en el campo correspondiente.
     */
    @Override
    public void ingresarPassword(String password) {
        try {
            espera.esperarElementoVisible(By.id("password"));
            limpiarEIngresarTexto(campoPassword, password);
            logger.debug("Password ingresado");
        } catch (Exception e) {
            logger.error("Error al ingresar password: {}", e.getMessage());
            throw new RuntimeException("No se pudo ingresar el password", e);
        }
    }

    /**
     * Marca o desmarca el checkbox de "Recordarme".
     */
    @Override
    public void marcarRecordarme(boolean recordar) {
        try {
            espera.esperarElementoClickeable(By.id("recordarme"));

            boolean estaSeleccionado = checkboxRecordarme.isSelected();

            if (recordar && !estaSeleccionado) {
                hacerClicRobusto(checkboxRecordarme);
                logger.debug("Checkbox 'Recordarme' marcado");
            } else if (!recordar && estaSeleccionado) {
                hacerClicRobusto(checkboxRecordarme);
                logger.debug("Checkbox 'Recordarme' desmarcado");
            }
        } catch (Exception e) {
            logger.error("Error al manejar checkbox recordarme: {}", e.getMessage());
            throw new RuntimeException("No se pudo manejar el checkbox recordarme", e);
        }
    }

    /**
     * Hace clic en el botón de login.
     */
    @Override
    public void clickBotonLogin() {
        try {
            espera.esperarElementoClickeable(By.id("btnLogin"));
            hacerClicRobusto(botonLogin);

            // Esperar a que la página procese el login
            espera.esperarInvisibilidadDelElemento(SPINNER_CARGA);
            logger.info("Click en botón login ejecutado");

        } catch (Exception e) {
            logger.error("Error al hacer click en login: {}", e.getMessage());
            throw new RuntimeException("No se pudo hacer click en login", e);
        }
    }

    /**
     * Hace clic en el enlace "¿Olvidaste tu contraseña?".
     */
    @Override
    public void clickOlvidePassword() {
        try {
            espera.esperarElementoClickeable(By.linkText("¿Olvidaste tu contraseña?"));
            hacerClicRobusto(enlaceOlvidePassword);
            logger.debug("Click en enlace 'Olvidé mi contraseña'");
        } catch (Exception e) {
            logger.error("Error al hacer click en olvidé password: {}", e.getMessage());
            throw new RuntimeException("No se pudo hacer click en olvidé password", e);
        }
    }

    /**
     * Hace clic en el enlace "Crear cuenta nueva".
     */
    @Override
    public void clickCrearCuenta() {
        try {
            espera.esperarElementoClickeable(By.linkText("Crear cuenta nueva"));
            hacerClicRobusto(enlaceCrearCuenta);
            logger.debug("Click en enlace 'Crear cuenta nueva'");
        } catch (Exception e) {
            logger.error("Error al hacer click en crear cuenta: {}", e.getMessage());
            throw new RuntimeException("No se pudo hacer click en crear cuenta", e);
        }
    }

    /**
     * Realiza el login completo con los datos proporcionados.
     */
    @Override
    public boolean iniciarSesion(ModeloDatosPrueba datos) {
        try {
            logger.info("Iniciando sesión para usuario: {}", datos.getEmail());

            // Verificar que la página esté visible
            if (!esPaginaVisible()) {
                throw new RuntimeException("La página de login no está visible");
            }

            // Llenar campos de login
            ingresarEmail(datos.getEmail());
            ingresarPassword(datos.getPassword());

            // Marcar recordarme si está especificado
            if (datos.isRecordarme()) {
                marcarRecordarme(true);
            }

            // Capturar pantalla antes del login (USO CORRECTO)
            gestorCaptura.capturarPantalla(driver, "antes_login_" + datos.getCasoPrueba());

            // Hacer click en login
            clickBotonLogin();

            // Verificar resultado
            boolean loginExitoso = verificarLoginExitoso();

            // Capturar pantalla después del resultado (USO CORRECTO)
            gestorCaptura.capturarPantalla(driver,
                    "despues_login_" + datos.getCasoPrueba() +
                            (loginExitoso ? "_exitoso" : "_fallido"));

            logger.info("Login completado. Exitoso: {}", loginExitoso);
            return loginExitoso;

        } catch (Exception e) {
            logger.error("Error durante el login: {}", e.getMessage());
            gestorCaptura.capturarPantalla(driver, "error_login_" + datos.getCasoPrueba());
            return false;
        }
    }

    /**
     * Obtiene el mensaje de error si existe.
     */
    @Override
    public String obtenerMensajeError() {
        try {
            if (esElementoVisible(By.xpath("//div[@class='mensaje-error']"))) {
                return mensajeError.getText().trim();
            }
            if (esElementoVisible(By.xpath("//div[@class='alerta-bloqueo']"))) {
                return alertaBloqueo.getText().trim();
            }
            return "";
        } catch (Exception e) {
            logger.warn("No se pudo obtener mensaje de error: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Obtiene el mensaje de éxito si existe.
     */
    @Override
    public String obtenerMensajeExito() {
        try {
            if (esElementoVisible(By.xpath("//div[@class='mensaje-exito']"))) {
                return mensajeExito.getText().trim();
            }
            return "";
        } catch (Exception e) {
            logger.warn("No se pudo obtener mensaje de éxito: {}", e.getMessage());
            return "";
        }
    }

    /**
     * Verifica si la cuenta está bloqueada.
     */
    @Override
    public boolean esCuentaBloqueada() {
        try {
            return esElementoVisible(MODAL_BLOQUEO) ||
                    esElementoVisible(By.xpath("//div[@class='alerta-bloqueo']")) ||
                    obtenerMensajeError().toLowerCase().contains("bloqueada") ||
                    obtenerMensajeError().toLowerCase().contains("suspendida");
        } catch (Exception e) {
            logger.warn("Error al verificar cuenta bloqueada: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si hay errores de validación en la página.
     */
    @Override
    public boolean hayErroresValidacion() {
        try {
            return esElementoVisible(By.xpath("//div[@class='mensaje-error']")) ||
                    esElementoVisible(By.xpath("//span[@class='error-campo']")) ||
                    esElementoVisible(By.xpath("//*[contains(@class,'error')]"));
        } catch (Exception e) {
            logger.warn("Error al verificar errores de validación: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Verifica si el usuario está logueado exitosamente.
     */
    @Override
    public boolean esUsuarioLogueado() {
        try {
            // Verificar indicadores de usuario logueado
            if (esElementoVisible(By.className("usuario-logueado"))) {
                return true;
            }

            // Verificar si la URL cambió a dashboard o home
            String urlActual = obtenerUrlActual();
            if (urlActual.contains("dashboard") || urlActual.contains("home") || urlActual.contains("perfil")) {
                return true;
            }

            // Verificar elementos que solo aparecen cuando está logueado
            if (esElementoVisible(By.id("menuUsuario")) ||
                    esElementoVisible(By.linkText("Cerrar Sesión"))) {
                return true;
            }

            return false;

        } catch (Exception e) {
            logger.warn("Error al verificar usuario logueado: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Limpia los campos del formulario de login.
     */
    @Override
    public void limpiarCampos() {
        try {
            campoEmail.clear();
            campoPassword.clear();

            // Desmarcar recordarme si está marcado
            if (checkboxRecordarme.isSelected()) {
                hacerClicRobusto(checkboxRecordarme);
            }

            logger.debug("Campos de login limpiados");
        } catch (Exception e) {
            logger.warn("Error al limpiar campos: {}", e.getMessage());
        }
    }

    /**
     * Cierra el modal de bloqueo si está presente.
     */
    @Override
    public void cerrarModalBloqueo() {
        try {
            if (esElementoVisible(MODAL_BLOQUEO)) {
                WebElement botonCerrar = driver.findElement(BOTON_CERRAR_MODAL);
                hacerClicRobusto(botonCerrar);
                espera.esperarInvisibilidadDelElemento(MODAL_BLOQUEO);
                logger.debug("Modal de bloqueo cerrado");
            }
        } catch (Exception e) {
            logger.warn("Error al cerrar modal de bloqueo: {}", e.getMessage());
        }
    }

    // ===== MÉTODOS PRIVADOS DE APOYO =====

    /**
     * Verifica si el login fue exitoso.
     */
    private boolean verificarLoginExitoso() {
        try {
            // Esperar un momento para que la página procese
            Thread.sleep(2000);

            // Si hay errores, el login falló
            if (hayErroresValidacion() || esCuentaBloqueada()) {
                return false;
            }

            // Verificar si el usuario está logueado
            return esUsuarioLogueado();

        } catch (Exception e) {
            logger.warn("Error al verificar login exitoso: {}", e.getMessage());
            return false;
        }
    }
}