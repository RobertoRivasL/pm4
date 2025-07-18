package com.robertorivas.automatizacion.paginas;

import com.robertorivas.automatizacion.modelos.DatosRegistro;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Page Object para la página de registro de Practice Expand Testing.
 * Implementa las interacciones específicas para /register
 * 
 * Basado en los test cases oficiales de Practice Expand Testing:
 * - Test Case 1: Successful Registration (Happy Path)
 * - Test Case 2: Registration with Missing Username
 * - Test Case 3: Registration with Missing Password  
 * - Test Case 4: Registration with Non-matching Passwords
 * 
 * Campos del formulario:
 * - Username field (id="username")
 * - Password field (id="password")
 * - Confirm Password field (id="confirmPassword")
 * - Register button (button[type="submit"])
 * 
 * Mensajes de error esperados:
 * - "All fields are required."
 * - "Passwords do not match."
 * 
 * Mensaje de éxito esperado:
 * - "Successfully registered, you can log in now."
 * 
 * @author Roberto Rivas Lopez
 */
public class PaginaRegistro extends PaginaBase {
    
    private static final Logger logger = LoggerFactory.getLogger(PaginaRegistro.class);
    
    // ===== SELECTORES ESPECÍFICOS PARA PRACTICE EXPAND TESTING =====
    
    // Campos del formulario de registro
    @FindBy(id = "username")
    private WebElement campoUsername;
    
    @FindBy(id = "password")
    private WebElement campoPassword;
    
    @FindBy(id = "confirmPassword")
    private WebElement campoConfirmarPassword;
    
    @FindBy(xpath = "//button[@type='submit' and contains(text(), 'Register')]")
    private WebElement botonRegistrar;
    
    // Mensajes de éxito y error específicos
    @FindBy(xpath = "//div[contains(text(), 'Successfully registered, you can log in now.')]")
    private WebElement mensajeRegistroExitoso;
    
    @FindBy(xpath = "//div[contains(text(), 'All fields are required.')]")
    private WebElement mensajeCamposRequeridos;
    
    @FindBy(xpath = "//div[contains(text(), 'Passwords do not match.')]")
    private WebElement mensajePasswordsNoCoinciden;
    
    // Mensajes generales
    @FindBy(xpath = "//div[contains(@class, 'alert') or contains(@class, 'message')]")
    private WebElement mensajeAlerta;
    
    // Enlaces de navegación
    @FindBy(xpath = "//a[@href='/login' or contains(text(), 'Login')]")
    private WebElement enlaceLogin;
    
    /**
     * Constructor que inicializa la página de registro.
     */
    public PaginaRegistro(WebDriver driver) {
        super(driver);
        PageFactory.initElements(driver, this);
        logger.debug("PaginaRegistro inicializada para Practice Expand Testing");
    }
    
    /**
     * Navega a la página de registro de Practice Expand Testing.
     * URL: https://practice.expandtesting.com/register
     */
    public void navegarAPaginaRegistro() {
        String urlRegistro = configuracion.obtenerUrlRegistro();
        logger.info("Navegando a la página de registro: {}", urlRegistro);
        
        try {
            driver.get(urlRegistro);
            esperarCargaPagina();
            logger.info("Navegación a página de registro completada");
        } catch (Exception e) {
            logger.error("Error navegando a la página de registro: {}", e.getMessage());
            throw new RuntimeException("No se pudo navegar a la página de registro", e);
        }
    }
    
    /**
     * Verifica si la página de registro está cargada correctamente.
     * Verifica la presencia de todos los elementos esenciales del formulario.
     */
    @Override
    public boolean estaPaginaCargada() {
        try {
            // Verificar elementos esenciales según test cases de Practice Expand Testing
            boolean campoUsernamePresente = estaElementoVisible(By.id("username"));
            boolean campoPasswordPresente = estaElementoVisible(By.id("password"));
            boolean campoConfirmarPasswordPresente = estaElementoVisible(By.id("confirmPassword"));
            boolean botonRegistrarPresente = estaElementoVisible(By.xpath("//button[@type='submit' and contains(text(), 'Register')]"));
            
            boolean paginaCargada = campoUsernamePresente && campoPasswordPresente && 
                                  campoConfirmarPasswordPresente && botonRegistrarPresente;
            
            if (paginaCargada) {
                logger.debug("Página de registro cargada correctamente - todos los elementos presentes");
            } else {
                logger.warn("La página de registro no está completamente cargada. Username: {}, Password: {}, ConfirmPassword: {}, Button: {}", 
                           campoUsernamePresente, campoPasswordPresente, campoConfirmarPasswordPresente, botonRegistrarPresente);
            }
            
            return paginaCargada;
        } catch (Exception e) {
            logger.debug("Error verificando si la página de registro está cargada: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Espera a que la página de registro se cargue completamente.
     * Espera todos los elementos esenciales del formulario.
     */
    public void esperarCargaPagina() {
        logger.debug("Esperando carga completa de página de registro");
        
        try {
            // Esperar elementos principales según estructura de Practice Expand Testing
            esperarElementoVisible(By.id("username"), tiempoEsperaExplicito);
            esperarElementoVisible(By.id("password"), tiempoEsperaExplicito);
            esperarElementoVisible(By.id("confirmPassword"), tiempoEsperaExplicito);
            esperarElementoVisible(By.xpath("//button[@type='submit' and contains(text(), 'Register')]"), tiempoEsperaExplicito);
            
            logger.debug("Página de registro cargada exitosamente");
        } catch (Exception e) {
            logger.error("Error esperando carga de página de registro: {}", e.getMessage());
            throw new RuntimeException("La página de registro no se cargó en el tiempo esperado", e);
        }
    }
    
    // ===== MÉTODOS DE INTERACCIÓN CON FORMULARIO =====
    
    /**
     * Llena el formulario de registro con los datos proporcionados.
     * Utiliza el modelo DatosRegistro adaptándolo a los campos de Practice Expand Testing.
     */
    public void llenarFormularioRegistro(DatosRegistro datos) {
        logger.info("Llenando formulario de registro con datos completos");
        
        try {
            // Username (usando email del modelo como username)
            if (datos.getEmail() != null && !datos.getEmail().isEmpty()) {
                limpiarYEscribir(campoUsername, datos.getEmail());
                logger.debug("Username ingresado: {}", datos.getEmail());
            }
            
            // Password
            if (datos.getPassword() != null && !datos.getPassword().isEmpty()) {
                limpiarYEscribir(campoPassword, datos.getPassword());
                logger.debug("Password ingresado (longitud: {})", datos.getPassword().length());
            }
            
            // Confirm Password
            if (datos.getConfirmarPassword() != null && !datos.getConfirmarPassword().isEmpty()) {
                limpiarYEscribir(campoConfirmarPassword, datos.getConfirmarPassword());
                logger.debug("Confirm Password ingresado (longitud: {})", datos.getConfirmarPassword().length());
            }
            
            logger.info("Formulario de registro completado");
        } catch (Exception e) {
            logger.error("Error llenando formulario de registro: {}", e.getMessage());
            throw new RuntimeException("No se pudo completar el formulario de registro", e);
        }
    }
    
    /**
     * Llena campos específicos para test cases individuales.
     * Permite llenar campos de forma selectiva para pruebas específicas.
     */
    public void llenarCampo(String campo, String valor) {
        logger.debug("Llenando campo específico: {} = {}", campo, valor != null ? valor : "NULL");
        
        try {
            switch (campo.toLowerCase()) {
                case "username":
                    if (valor != null && !valor.isEmpty()) {
                        limpiarYEscribir(campoUsername, valor);
                        logger.debug("Campo username completado");
                    } else {
                        logger.debug("Campo username dejado vacío intencionalmente");
                    }
                    break;
                case "password":
                    if (valor != null && !valor.isEmpty()) {
                        limpiarYEscribir(campoPassword, valor);
                        logger.debug("Campo password completado");
                    } else {
                        logger.debug("Campo password dejado vacío intencionalmente");
                    }
                    break;
                case "confirmpassword":
                case "confirm_password":
                    if (valor != null && !valor.isEmpty()) {
                        limpiarYEscribir(campoConfirmarPassword, valor);
                        logger.debug("Campo confirmPassword completado");
                    } else {
                        logger.debug("Campo confirmPassword dejado vacío intencionalmente");
                    }
                    break;
                default:
                    logger.warn("Campo no reconocido: {}", campo);
            }
        } catch (Exception e) {
            logger.error("Error llenando campo {}: {}", campo, e.getMessage());
            throw new RuntimeException("No se pudo llenar el campo: " + campo, e);
        }
    }
    
    /**
     * Envía el formulario de registro haciendo clic en el botón Register.
     * Implementa esperas y manejo de errores robusto.
     */
    public void enviarFormulario() {
        logger.info("Enviando formulario de registro");
        
        try {
            // Asegurar que el botón esté visible y clickeable
            scrollearHaciaElemento(botonRegistrar);
            esperarElementoClickeable(botonRegistrar, tiempoEsperaExplicito);
            
            // Hacer clic en el botón
            hacerClicSeguro(botonRegistrar);
            
            // Esperar a que se procese la respuesta del servidor
            Thread.sleep(3000);
            
            logger.info("Formulario de registro enviado exitosamente");
        } catch (Exception e) {
            logger.error("Error enviando formulario de registro: {}", e.getMessage());
            throw new RuntimeException("No se pudo enviar el formulario de registro", e);
        }
    }
    
    // ===== MÉTODOS DE VALIDACIÓN =====
    
    /**
     * Verifica si el registro fue exitoso.
     * Según test case 1: redirección a /login con mensaje "Successfully registered, you can log in now."
     */
    public boolean registroExitoso() {
        try {
            // Verificar si estamos en la página de login (redirección exitosa)
            boolean enPaginaLogin = driver.getCurrentUrl().contains("/login");
            
            // Verificar mensaje de éxito específico
            boolean mensajeExitoVisible = estaElementoVisible(By.xpath("//div[contains(text(), 'Successfully registered')]"));
            
            boolean exitoso = enPaginaLogin || mensajeExitoVisible;
            
            if (exitoso) {
                logger.info("✓ Registro exitoso detectado - URL: {}", driver.getCurrentUrl());
            } else {
                logger.debug("✗ Registro no exitoso - URL actual: {}", driver.getCurrentUrl());
            }
            
            return exitoso;
        } catch (Exception e) {
            logger.debug("Error verificando éxito del registro: {}", e.getMessage());
            return false;
        }
    }
    
    /**
     * Obtiene el mensaje de éxito después del registro.
     * Mensaje esperado: "Successfully registered, you can log in now."
     */
    public String obtenerMensajeExito() {
        try {
            if (estaElementoVisible(By.xpath("//div[contains(text(), 'Successfully registered')]"))) {
                String mensaje = obtenerTexto(mensajeRegistroExitoso);
                logger.debug("Mensaje de éxito obtenido: {}", mensaje);
                return mensaje;
            }
        } catch (Exception e) {
            logger.debug("Error obteniendo mensaje de éxito: {}", e.getMessage());
        }
        return null;
    }
    
    /**
     * Obtiene todos los errores de validación mostrados.
     * Mensajes esperados según test cases:
     * - "All fields are required."
     * - "Passwords do not match."
     */
    public List<String> obtenerErroresValidacion() {
        List<String> errores = new ArrayList<>();
        
        try {
            // Buscar mensajes de error específicos de Practice Expand Testing
            List<String> selectoresError = List.of(
                "//div[contains(text(), 'All fields are required')]",
                "//div[contains(text(), 'Passwords do not match')]",
                "//div[contains(@class, 'alert-danger') or contains(@class, 'error')]",
                "//div[contains(@class, 'alert') and not(contains(@class, 'success'))]"
            );
            
            for (String selector : selectoresError) {
                List<WebElement> elementosError = driver.findElements(By.xpath(selector));
                for (WebElement elemento : elementosError) {
                    if (elemento.isDisplayed()) {
                        String textoError = elemento.getText().trim();
                        if (!textoError.isEmpty() && !errores.contains(textoError)) {
                            errores.add(textoError);
                            logger.debug("Error de validación encontrado: {}", textoError);
                        }
                    }
                }
            }
            
            if (errores.isEmpty()) {
                logger.debug("No se encontraron errores de validación visibles");
            } else {
                logger.info("Errores de validación encontrados: {}", errores);
            }
            
        } catch (Exception e) {
            logger.debug("Error obteniendo errores de validación: {}", e.getMessage());
        }
        
        return errores;
    }
    
    // ===== MÉTODOS DE NAVEGACIÓN =====
    
    /**
     * Navega a la página de login desde la página de registro.
     * Utiliza enlace si está disponible, sino navegación directa.
     */
    public void irAPaginaLogin() {
        logger.info("Navegando a página de login");
        
        try {
            if (estaElementoVisible(By.xpath("//a[@href='/login' or contains(text(), 'Login')]"))) {
                hacerClicSeguro(enlaceLogin);
                logger.info("Navegación a login exitosa usando enlace");
            } else {
                // Navegación directa si no hay enlace
                String urlLogin = configuracion.obtenerUrlLogin();
                driver.get(urlLogin);
                logger.info("Navegación directa a login completada");
            }
        } catch (Exception e) {
            logger.error("Error navegando a página de login: {}", e.getMessage());
            throw new RuntimeException("No se pudo navegar a la página de login", e);
        }
    }
    
    /**
     * Limpia todos los campos del formulario.
     * Útil para pruebas que requieren formulario limpio.
     */
    public void limpiarFormulario() {
        logger.info("Limpiando formulario de registro");
        
        try {
            campoUsername.clear();
            campoPassword.clear();
            campoConfirmarPassword.clear();
            
            logger.info("Formulario de registro limpiado exitosamente");
        } catch (Exception e) {
            logger.error("Error limpiando formulario: {}", e.getMessage());
            throw new RuntimeException("No se pudo limpiar el formulario", e);
        }
    }
    
    // ===== MÉTODOS DE VERIFICACIÓN ESPECÍFICOS PARA TEST CASES =====
    
    /**
     * Test Case 2 & 3: Verifica que se muestre el error "All fields are required."
     * Se utiliza cuando falta username o password.
     */
    public boolean verificarErrorCamposRequeridos() {
        boolean errorVisible = estaElementoVisible(By.xpath("//div[contains(text(), 'All fields are required')]"));
        if (errorVisible) {
            logger.debug("✓ Error 'All fields are required' visible correctamente");
        } else {
            logger.debug("✗ Error 'All fields are required' NO visible");
        }
        return errorVisible;
    }
    
    /**
     * Test Case 4: Verifica que se muestre el error "Passwords do not match."
     * Se utiliza cuando password y confirmPassword son diferentes.
     */
    public boolean verificarErrorPasswordsNoCoinciden() {
        boolean errorVisible = estaElementoVisible(By.xpath("//div[contains(text(), 'Passwords do not match')]"));
        if (errorVisible) {
            logger.debug("✓ Error 'Passwords do not match' visible correctamente");
        } else {
            logger.debug("✗ Error 'Passwords do not match' NO visible");
        }
        return errorVisible;
    }
    
    /**
     * Test Case 1: Verifica que se muestre el mensaje "Successfully registered, you can log in now."
     * Se utiliza para confirmar registro exitoso.
     */
    public boolean verificarMensajeRegistroExitoso() {
        boolean mensajeVisible = estaElementoVisible(By.xpath("//div[contains(text(), 'Successfully registered')]"));
        if (mensajeVisible) {
            logger.debug("✓ Mensaje 'Successfully registered' visible correctamente");
        } else {
            logger.debug("✗ Mensaje 'Successfully registered' NO visible");
        }
        return mensajeVisible;
    }
    
    /**
     * Método de debug para obtener información del estado actual de la página.
     * Útil para troubleshooting cuando los tests fallan.
     */
    public void imprimirEstadoPagina() {
        try {
            logger.info("=== ESTADO ACTUAL DE LA PÁGINA ===");
            logger.info("URL actual: {}", driver.getCurrentUrl());
            logger.info("Título de página: {}", driver.getTitle());
            
            // Verificar presencia de elementos
            logger.info("Campo Username presente: {}", estaElementoVisible(By.id("username")));
            logger.info("Campo Password presente: {}", estaElementoVisible(By.id("password")));
            logger.info("Campo ConfirmPassword presente: {}", estaElementoVisible(By.id("confirmPassword")));
            logger.info("Botón Register presente: {}", estaElementoVisible(By.xpath("//button[@type='submit' and contains(text(), 'Register')]")));
            
            // Verificar mensajes
            logger.info("Mensaje éxito visible: {}", verificarMensajeRegistroExitoso());
            logger.info("Error campos requeridos visible: {}", verificarErrorCamposRequeridos());
            logger.info("Error passwords no coinciden visible: {}", verificarErrorPasswordsNoCoinciden());
            
            logger.info("=== FIN ESTADO PÁGINA ===");
        } catch (Exception e) {
            logger.error("Error obteniendo estado de página: {}", e.getMessage());
        }
    }
    
    // ===== MÉTODOS DE COMPATIBILIDAD PARA PRUEBAS DE INTEGRACIÓN =====
    
    /**
     * Llena campos básicos del formulario (compatibilidad con PruebasIntegracion).
     * @param datos Los datos de registro
     * @return Esta instancia para method chaining
     */
    public PaginaRegistro llenarCamposBasicos(DatosRegistro datos) {
        logger.debug("Llenando campos básicos para integración");
        llenarFormularioRegistro(datos);
        return this;
    }
    
    /**
     * Obtiene los valores actuales del formulario (compatibilidad con PruebasIntegracion).
     * @return Map con los valores de los campos
     */
    public Map<String, String> obtenerValoresFormulario() {
        Map<String, String> valores = new HashMap<>();
        try {
            valores.put("username", campoUsername.getAttribute("value"));
            valores.put("password", campoPassword.getAttribute("value"));
            valores.put("confirmPassword", campoConfirmarPassword.getAttribute("value"));
            logger.debug("Valores del formulario obtenidos: {}", valores);
        } catch (Exception e) {
            logger.error("Error obteniendo valores del formulario: {}", e.getMessage());
        }
        return valores;
    }
    
    /**
     * Verifica si hay errores de validación visibles (compatibilidad con PruebasIntegracion).
     * @return true si hay errores visibles
     */
    public boolean hayErroresValidacion() {
        List<String> errores = obtenerErroresValidacion();
        boolean hayErrores = !errores.isEmpty();
        logger.debug("Hay errores de validación: {}", hayErrores);
        return hayErrores;
    }
}
