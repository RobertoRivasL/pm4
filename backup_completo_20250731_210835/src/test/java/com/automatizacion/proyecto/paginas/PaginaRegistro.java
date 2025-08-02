package com.automatizacion.proyecto.paginas;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.IPaginaRegistro;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Página de registro implementando el patrón Page Object Model.
 * Encapsula todos los elementos y acciones relacionadas con el registro de
 * usuarios.
 * 
 * Implementa la interfaz IPaginaRegistro siguiendo el principio de
 * Inversión de Dependencias del SOLID.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class PaginaRegistro extends PaginaBase implements IPaginaRegistro {

    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);

    private PaginaLogin paginaLogin;
    private PaginaRegistro paginaRegistro;

    // === ELEMENTOS DEL FORMULARIO PRINCIPAL ===

    @FindBy(id = "firstName")
    private WebElement campoNombre;

    @FindBy(id = "lastName")
    private WebElement campoApellido;

    @FindBy(id = "email")
    private WebElement campoEmail;

    @FindBy(id = "password")
    private WebElement campoPassword;

    @FindBy(id = "confirmPassword")
    private WebElement campoConfirmarPassword;

    @FindBy(id = "phone")
    private WebElement campoTelefono;

    // === ELEMENTOS DE SELECCIÓN ===

    @FindBy(id = "gender")
    private WebElement selectGenero;

    @FindBy(id = "country")
    private WebElement selectPais;

    @FindBy(id = "city")
    private WebElement selectCiudad;

    @FindBy(id = "birthDate")
    private WebElement campoFechaNacimiento;

    // === BOTONES Y CONTROLES ===

    @FindBy(id = "registerButton")
    private WebElement botonRegistrar;

    @FindBy(id = "clearFormButton")
    private WebElement botonLimpiarFormulario;

    @FindBy(id = "termsCheckbox")
    private WebElement checkboxTerminos;

    @FindBy(id = "newsletterCheckbox")
    private WebElement checkboxNewsletter;

    // === ELEMENTOS DE VALIDACIÓN Y MENSAJES ===

    @FindBy(className = "register-form")
    private WebElement formularioRegistro;

    @FindBy(className = "success-message")
    private WebElement mensajeExito;

    @FindBy(className = "error-message")
    private WebElement mensajeError;

    @FindBy(className = "loading-spinner")
    private WebElement spinnerCarga;

    // === MENSAJES DE ERROR ESPECÍFICOS POR CAMPO ===

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'nombre') or contains(.,'firstName')]")
    private WebElement errorNombre;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'apellido') or contains(.,'lastName')]")
    private WebElement errorApellido;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'email')]")
    private WebElement errorEmail;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'password') or contains(.,'contraseña')]")
    private WebElement errorPassword;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'confirm') or contains(.,'confirmar')]")
    private WebElement errorConfirmarPassword;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'teléfono') or contains(.,'phone')]")
    private WebElement errorTelefono;

    @FindBy(xpath = "//div[@class='field-error'][contains(.,'términos') or contains(.,'terms')]")
    private WebElement errorTerminos;

    // === ELEMENTOS ADICIONALES ===

    @FindBy(id = "loginLink")
    private WebElement enlaceLogin;

    @FindBy(id = "registerTitle")
    private WebElement tituloRegistro;

    @FindBy(className = "form-section")
    private WebElement seccionFormulario;

    /**
     * Constructor que inicializa la página con el driver
     * 
     * @param driver instancia de WebDriver
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        logger.debug(TipoMensaje.DEBUG.formatearMensaje("PaginaRegistro inicializada"));
    }

    @Override
    public void actualizarPagina() {
        // Implementa la lógica necesaria aquí
    }

    // === IMPLEMENTACIÓN DE INTERFAZ IPaginaRegistro ===

    @Override
    public boolean esPaginaVisible() {
        try {
            return esperarElementoVisible(formularioRegistro, 10) != null &&
                    esperarElementoVisible(campoNombre, 5) != null &&
                    esperarElementoVisible(campoEmail, 5) != null &&
                    esperarElementoVisible(botonRegistrar, 5) != null;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando visibilidad de página registro: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean esperarCargaPagina(int segundos) {
        try {
            return esperarElementoVisible(formularioRegistro, segundos) != null;
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error esperando carga de la página de registro: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public void llenarFormularioCompleto(ModeloDatosPrueba datos) {
        try {
            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Llenando formulario completo con: " + datos.generarResumen()));

            // Campos obligatorios
            if (datos.getNombre() != null) {
                ingresarNombre(datos.getNombre());
            }

            if (datos.getApellido() != null) {
                ingresarApellido(datos.getApellido());
            }

            if (datos.getEmail() != null) {
                ingresarEmail(datos.getEmail());
            }

            if (datos.getPassword() != null) {
                ingresarPassword(datos.getPassword());
            }

            if (datos.getConfirmacionPassword() != null) {
                ingresarConfirmarPassword(datos.getConfirmacionPassword());
            }

            // Campos opcionales
            if (datos.getTelefono() != null) {
                ingresarTelefono(datos.getTelefono());
            }

            if (datos.getGenero() != null) {
                seleccionarGenero(datos.getGenero());
            }

            if (datos.getPais() != null) {
                seleccionarPais(datos.getPais());
            }

            if (datos.getCiudad() != null) {
                seleccionarCiudad(datos.getCiudad());
            }

            if (datos.getFechaNacimiento() != null) {
                ingresarFechaNacimiento(datos.getFechaNacimiento());
            }

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error llenando formulario completo: " + e.getMessage()));
            throw new RuntimeException("No se pudo llenar el formulario completo", e);
        }
    }

    @Override
    public void ingresarNombre(String nombre) {
        try {
            esperarElementoClicable(campoNombre, 10);
            limpiarYEscribir(campoNombre, nombre);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Nombre ingresado: " + nombre));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando nombre: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el nombre", e);
        }
    }

    @Override
    public void ingresarApellido(String apellido) {
        try {
            esperarElementoClicable(campoApellido, 10);
            limpiarYEscribir(campoApellido, apellido);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Apellido ingresado: " + apellido));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando apellido: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el apellido", e);
        }
    }

    @Override
    public void ingresarEmail(String email) {
        try {
            esperarElementoClicable(campoEmail, 10);
            limpiarYEscribir(campoEmail, email);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Email ingresado: " + enmascararEmail(email)));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando email: " + e.getMessage()));
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
                    "Error ingresando password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar el password", e);
        }
    }

    @Override
    public void ingresarConfirmarPassword(String confirmarPassword) {
        try {
            esperarElementoClicable(campoConfirmarPassword, 10);
            limpiarYEscribir(campoConfirmarPassword, confirmarPassword);

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Confirmación de password ingresada"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando confirmación password: " + e.getMessage()));
            throw new RuntimeException("No se pudo ingresar la confirmación de password", e);
        }
    }

    @Override
    public void aceptarTerminos() {
        try {
            if (esElementoVisible(checkboxTerminos) && !checkboxTerminos.isSelected()) {
                hacerClick(checkboxTerminos);
                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Términos y condiciones aceptados"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error aceptando términos: " + e.getMessage()));
            throw new RuntimeException("No se pudieron aceptar los términos", e);
        }
    }

    @Override
    public void clickBotonRegistrar() {
        try {
            esperarElementoClicable(botonRegistrar, 10);
            hacerClick(botonRegistrar);

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Click en botón Registrar ejecutado"));

            // Esperar que desaparezca el spinner si está presente
            if (esElementoVisible(spinnerCarga)) {
                esperarElementoInvisible(spinnerCarga, 20);
            }

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error haciendo click en botón registrar: " + e.getMessage()));
            throw new RuntimeException("No se pudo hacer click en el botón registrar", e);
        }
    }

    @Override
    public boolean registrarUsuario(ModeloDatosPrueba datos) {
        try {
            if (!datos.camposObligatoriosCompletos()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Datos de registro incompletos"));
                return false;
            }

            logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje(
                    "Registrando usuario con: " + datos.generarResumen()));

            llenarFormularioCompleto(datos);
            aceptarTerminos();
            clickBotonRegistrar();

            // Verificar resultado
            if (datos.isEsValido()) {
                return verificarRegistroExitoso();
            } else {
                return verificarRegistroFallido();
            }

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error en proceso de registro: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean verificarRegistroExitoso() {
        try {
            // Ajusta el selector según tu página real
            return esElementoVisible(mensajeExito) || fueRedirigidoALogin();
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando registro exitoso: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public boolean verificarRegistroFallido() {
        try {
            return esElementoVisible(mensajeError) || hayErroresValidacion();

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error verificando registro fallido: " + e.getMessage()));
            return false;
        }
    }

    @Override
    public String obtenerMensajeError() {
        try {
            if (esElementoVisible(mensajeError)) {
                return obtenerTexto(mensajeError);
            }

            // Buscar errores específicos de campos
            StringBuilder errores = new StringBuilder();

            if (esElementoVisible(errorNombre)) {
                errores.append(obtenerTexto(errorNombre)).append("; ");
            }
            if (esElementoVisible(errorApellido)) {
                errores.append(obtenerTexto(errorApellido)).append("; ");
            }
            if (esElementoVisible(errorEmail)) {
                errores.append(obtenerTexto(errorEmail)).append("; ");
            }
            if (esElementoVisible(errorPassword)) {
                errores.append(obtenerTexto(errorPassword)).append("; ");
            }
            if (esElementoVisible(errorConfirmarPassword)) {
                errores.append(obtenerTexto(errorConfirmarPassword)).append("; ");
            }

            return errores.toString();

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error obteniendo mensaje de error: " + e.getMessage()));
            return "";
        }
    }

    // === MÉTODOS ADICIONALES ESPECÍFICOS ===

    /**
     * Ingresa el número de teléfono
     * 
     * @param telefono número de teléfono
     */
    public void ingresarTelefono(String telefono) {
        try {
            if (esElementoVisible(campoTelefono)) {
                esperarElementoClicable(campoTelefono, 10);
                limpiarYEscribir(campoTelefono, telefono);

                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Teléfono ingresado"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando teléfono: " + e.getMessage()));
        }
    }

    /**
     * Selecciona el género del usuario
     * 
     * @param genero género a seleccionar
     */
    public void seleccionarGenero(String genero) {
        try {
            if (esElementoVisible(selectGenero)) {
                Select select = new Select(selectGenero);
                select.selectByVisibleText(genero);

                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Género seleccionado: " + genero));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error seleccionando género: " + e.getMessage()));
        }
    }

    /**
     * Selecciona el país del usuario
     * 
     * @param pais país a seleccionar
     */
    public void seleccionarPais(String pais) {
        try {
            if (esElementoVisible(selectPais)) {
                Select select = new Select(selectPais);
                select.selectByVisibleText(pais);

                logger.debug(TipoMensaje.DEBUG.formatearMensaje("País seleccionado: " + pais));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error seleccionando país: " + e.getMessage()));
        }
    }

    /**
     * Selecciona la ciudad del usuario
     * 
     * @param ciudad ciudad a seleccionar
     */
    public void seleccionarCiudad(String ciudad) {
        try {
            if (esElementoVisible(selectCiudad)) {
                Select select = new Select(selectCiudad);
                select.selectByVisibleText(ciudad);

                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Ciudad seleccionada: " + ciudad));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error seleccionando ciudad: " + e.getMessage()));
        }
    }

    /**
     * Ingresa la fecha de nacimiento
     * 
     * @param fechaNacimiento fecha en formato dd/mm/yyyy
     */
    public void ingresarFechaNacimiento(String fechaNacimiento) {
        try {
            if (esElementoVisible(campoFechaNacimiento)) {
                esperarElementoClicable(campoFechaNacimiento, 10);
                limpiarYEscribir(campoFechaNacimiento, fechaNacimiento);

                logger.debug(TipoMensaje.DEBUG.formatearMensaje("Fecha de nacimiento ingresada"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error ingresando fecha de nacimiento: " + e.getMessage()));
        }
    }

    /**
     * Activa o desactiva la suscripción al newsletter
     * 
     * @param suscribir true para suscribirse
     */
    public void configurarNewsletter(boolean suscribir) {
        try {
            if (esElementoVisible(checkboxNewsletter)) {
                boolean estaSeleccionado = checkboxNewsletter.isSelected();

                if (suscribir && !estaSeleccionado) {
                    hacerClick(checkboxNewsletter);
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("Newsletter activado"));
                } else if (!suscribir && estaSeleccionado) {
                    hacerClick(checkboxNewsletter);
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje("Newsletter desactivado"));
                }
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error configurando newsletter: " + e.getMessage()));
        }
    }

    /**
     * Limpia todo el formulario
     */
    public void limpiarFormulario() {
        try {
            if (esElementoVisible(botonLimpiarFormulario)) {
                hacerClick(botonLimpiarFormulario);
            } else {
                // Limpiar campos manualmente
                limpiarCamposManualmente();
            }

            logger.debug(TipoMensaje.DEBUG.formatearMensaje("Formulario de registro limpiado"));

        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error limpiando formulario: " + e.getMessage()));
        }
    }

    /**
     * Navega a la página de login
     */
    public void irALogin() {
        try {
            if (esElementoVisible(enlaceLogin)) {
                esperarElementoClicable(enlaceLogin, 10);
                hacerClick(enlaceLogin);

                logger.info(TipoMensaje.PASO_PRUEBA.formatearMensaje("Navegando a página de login"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error navegando a login: " + e.getMessage()));
        }
    }

    /**
     * Verifica si hay errores de validación en los campos
     * 
     * @return true si hay errores visibles
     */
    public boolean hayErroresValidacion() {
        return esElementoVisible(errorNombre) ||
                esElementoVisible(errorApellido) ||
                esElementoVisible(errorEmail) ||
                esElementoVisible(errorPassword) ||
                esElementoVisible(errorConfirmarPassword) ||
                esElementoVisible(errorTelefono) ||
                esElementoVisible(errorTerminos) ||
                esElementoVisible(mensajeError);
    }

    /**
     * Verifica si todos los elementos principales están visibles
     * 
     * @return true si todos los elementos están presentes
     */
    @Override
    public void navegarAtras() {
        driver.navigate().back();
    }

    /**
     * Verifica si todos los elementos principales están visibles
     * Ahora retorna void, como lo requiere la interfaz/base.
     */
    @Override
    public void validarElementosPagina() {
        try {
            boolean todosPresentes = esElementoVisible(campoNombre) &&
                    esElementoVisible(campoApellido) &&
                    esElementoVisible(campoEmail) &&
                    esElementoVisible(campoPassword) &&
                    esElementoVisible(campoConfirmarPassword) &&
                    esElementoVisible(botonRegistrar) &&
                    esElementoVisible(checkboxTerminos);

            if (todosPresentes) {
                logger.info(TipoMensaje.VALIDACION.formatearMensaje(
                        "Todos los elementos de la página registro están presentes"));
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                        "Faltan elementos en la página registro"));
            }
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error validando elementos de página: " + e.getMessage()));
        }
    }

    /**
     * Obtiene el título de la página
     * 
     * @return texto del título
     */
    public String obtenerTitulo() {
        try {
            if (esElementoVisible(tituloRegistro)) {
                return obtenerTexto(tituloRegistro);
            }
            return driver.getTitle();
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                    "Error obteniendo título: " + e.getMessage()));
            return "";
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
     * Limpia los campos del formulario manualmente
     */
    private void limpiarCamposManualmente() {
        try {
            if (esElementoVisible(campoNombre))
                campoNombre.clear();
            if (esElementoVisible(campoApellido))
                campoApellido.clear();
            if (esElementoVisible(campoEmail))
                campoEmail.clear();
            if (esElementoVisible(campoPassword))
                campoPassword.clear();
            if (esElementoVisible(campoConfirmarPassword))
                campoConfirmarPassword.clear();
            if (esElementoVisible(campoTelefono))
                campoTelefono.clear();
            if (esElementoVisible(campoFechaNacimiento))
                campoFechaNacimiento.clear();
        } catch (Exception e) {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Error en limpieza manual: " + e.getMessage()));
        }
    }

    /**
     * Enmascara email para logs seguros
     * 
     * @param email email a enmascarar
     * @return email enmascarado
     */
    private String enmascararEmail(String email) {
        if (email == null || email.length() < 3) {
            return "***";
        }

        int posArroba = email.indexOf('@');
        if (posArroba <= 1) {
            return "***@" + email.substring(posArroba + 1);
        }

        return email.substring(0, 2) + "***@" + email.substring(posArroba + 1);
    }

    public boolean fueRedirigidoALogin() {
        // Ajusta el selector según la URL o algún elemento único de la página de login
        return driver.getCurrentUrl().contains("/login");
    }
