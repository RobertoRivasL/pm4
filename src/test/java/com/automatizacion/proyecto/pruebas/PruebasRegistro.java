package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.pruebas.PruebaBase;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.utilidades.LectorDatosPrueba;
import org.testng.annotations.*;
import org.testng.Assert;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.automatizacion.proyecto.paginas.PaginaLogin;


/**
 * Pruebas automatizadas para funcionalidad de registro de usuarios.
 * Implementa casos de prueba válidos e inválidos para validar el formulario de registro.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Epic("Gestión de Usuarios")
@Feature("Registro de Usuarios")
public class PruebasRegistro extends PruebaBase {
    
    private static final Logger logger = LoggerFactory.getLogger(PruebasRegistro.class);
    private PaginaRegistro paginaRegistro;
    
    @BeforeMethod(dependsOnMethods = "configurarPrueba")
    public void configurarPaginaRegistro() {
        try {
            paginaRegistro = new PaginaRegistro(driver);
            navegarARegistro();
            
            // Verificar que la página se cargó correctamente
            Assert.assertTrue(paginaRegistro.esPaginaVisible(), 
                "La página de registro debería estar visible");
                
            logger.info("Página de registro configurada exitosamente");
        } catch (Exception e) {
            logger.error("Error configurando página de registro: {}", e.getMessage(), e);
            capturarPantalla("error_configuracion_registro");
            throw new RuntimeException("Fallo en configuración de página de registro", e);
        }
    }
    
    // ===== PRUEBAS CON DATOS VÁLIDOS =====
    
    @Test(priority = 1, 
          description = "Registro exitoso con datos válidos completos",
          groups = {"smoke", "registro", "positivo"})
    @Story("Registro Exitoso")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que un usuario puede registrarse exitosamente con datos válidos")
    public void testRegistroExitoso() {
        
        iniciarPrueba("Registro Exitoso", "Usuario puede registrarse con datos válidos");
        
        try {
            // Preparar datos de prueba
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_001")
                    .nombre("Juan")
                    .apellido("Pérez")
                    .email("juan.perez.test." + System.currentTimeMillis() + "@email.com")
                    .password("Password123!")
                    .confirmarPassword("Password123!")
                    .telefono("+56912345678")
                    .genero("Masculino")
                    .aceptarTerminos(true)
                    .descripcion("Registro válido completo")
                    .build();
            
            logPasoPrueba("Ejecutando registro con datos: " + datos.getEmail());
            
            // Ejecutar registro
            boolean registroExitoso = paginaRegistro.registrarUsuario(datos);
            
            // Validaciones
            Assert.assertTrue(registroExitoso, 
                "El registro debería ser exitoso con datos válidos");
            
            logValidacion("Registro completado exitosamente");
            finalizarPrueba("Registro Exitoso", true);
            
        } catch (Exception e) {
            logger.error("Error en test de registro exitoso: {}", e.getMessage(), e);
            capturarPantalla("error_registro_exitoso");
            finalizarPrueba("Registro Exitoso", false);
            throw e;
        }
    }
    
    @Test(priority = 2,
          description = "Registro exitoso sin campos opcionales",
          groups = {"regression", "registro", "positivo"})
    @Story("Registro Básico")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica registro exitoso solo con campos obligatorios")
    public void testRegistroBasico() {
        
        iniciarPrueba("Registro Básico", "Registro solo con campos obligatorios");
        
        try {
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_002")
                    .nombre("María")
                    .apellido("González")
                    .email("maria.gonzalez.test." + System.currentTimeMillis() + "@email.com")
                    .password("SecurePass456!")
                    .confirmarPassword("SecurePass456!")
                    .aceptarTerminos(true)
                    .descripcion("Registro básico sin opcionales")
                    .build();
            
            logPasoPrueba("Ejecutando registro básico con: " + datos.getEmail());
            
            boolean registroExitoso = paginaRegistro.registrarUsuario(datos);
            
            Assert.assertTrue(registroExitoso, 
                "El registro básico debería ser exitoso");
            
            logValidacion("Registro básico completado exitosamente");
            finalizarPrueba("Registro Básico", true);
            
        } catch (Exception e) {
            logger.error("Error en test de registro básico: {}", e.getMessage(), e);
            capturarPantalla("error_registro_basico");
            finalizarPrueba("Registro Básico", false);
            throw e;
        }
    }
    
    // ===== PRUEBAS CON DATOS INVÁLIDOS =====
    
    @Test(priority = 3,
          description = "Validación de campos obligatorios vacíos",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Campos")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que se validen los campos obligatorios")
    public void testValidacionCamposObligatorios() {
        
        iniciarPrueba("Validación Campos Obligatorios", "Verificar validación de campos requeridos");
        
        try {
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_003")
                    .nombre("")  // Nombre vacío
                    .apellido("López")
                    .email("test.validacion@email.com")
                    .password("Password123!")
                    .confirmarPassword("Password123!")
                    .aceptarTerminos(true)
                    .esValido(false)
                    .mensajeError("El nombre es obligatorio")
                    .descripcion("Validación nombre obligatorio")
                    .build();
            
            logPasoPrueba("Probando validación con nombre vacío");
            
            boolean registroFallido = paginaRegistro.registrarUsuario(datos);
            
            // El registro debería fallar
            Assert.assertFalse(registroFallido, 
                "El registro debería fallar con nombre vacío");
            
            // Verificar que hay errores de validación
            Assert.assertTrue(paginaRegistro.hayErroresValidacion(), 
                "Deberían mostrarse errores de validación");
            
            logValidacion("Validación de campos obligatorios funcionando correctamente");
            finalizarPrueba("Validación Campos Obligatorios", true);
            
        } catch (Exception e) {
            logger.error("Error en test de validación: {}", e.getMessage(), e);
            capturarPantalla("error_validacion_campos");
            finalizarPrueba("Validación Campos Obligatorios", false);
            throw e;
        }
    }
    
    @Test(priority = 4,
          description = "Validación de formato de email",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Email")
    @Severity(SeverityLevel.NORMAL)
    @Description("Verifica validación de formato de email")
    public void testValidacionFormatoEmail() {
        
        iniciarPrueba("Validación Email", "Verificar validación de formato de email");
        
        try {
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_004")
                    .nombre("Carlos")
                    .apellido("Rodríguez")
                    .email("email.invalido")  // Email sin formato válido
                    .password("Password123!")
                    .confirmarPassword("Password123!")
                    .aceptarTerminos(true)
                    .esValido(false)
                    .mensajeError("Ingrese un email válido")
                    .descripcion("Validación formato email")
                    .build();
            
            logPasoPrueba("Probando validación con email inválido: " + datos.getEmail());
            
            boolean registroFallido = paginaRegistro.registrarUsuario(datos);
            
            Assert.assertFalse(registroFallido, 
                "El registro debería fallar con email inválido");
            
            Assert.assertTrue(paginaRegistro.hayErroresValidacion(), 
                "Deberían mostrarse errores de validación de email");
            
            logValidacion("Validación de formato de email funcionando correctamente");
            finalizarPrueba("Validación Email", true);
            
        } catch (Exception e) {
            logger.error("Error en test de validación email: {}", e.getMessage(), e);
            capturarPantalla("error_validacion_email");
            finalizarPrueba("Validación Email", false);
            throw e;
        }
    }
    
    @Test(priority = 5,
          description = "Validación de contraseñas que no coinciden",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Validación de Contraseña")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que las contraseñas deben coincidir")
    public void testValidacionPasswordsNoCoinciden() {
        
        iniciarPrueba("Validación Passwords", "Verificar que las contraseñas deben coincidir");
        
        try {
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_005")
                    .nombre("Ana")
                    .apellido("Silva")
                    .email("ana.silva.test." + System.currentTimeMillis() + "@email.com")
                    .password("Password123!")
                    .confirmarPassword("Password456!")  // Contraseña diferente
                    .aceptarTerminos(true)
                    .esValido(false)
                    .mensajeError("Las contraseñas no coinciden")
                    .descripcion("Validación contraseñas no coinciden")
                    .build();
            
            logPasoPrueba("Probando validación con contraseñas diferentes");
            
            boolean registroFallido = paginaRegistro.registrarUsuario(datos);
            
            Assert.assertFalse(registroFallido, 
                "El registro debería fallar cuando las contraseñas no coinciden");
            
            Assert.assertTrue(paginaRegistro.hayErroresValidacion(), 
                "Deberían mostrarse errores de validación de contraseña");
            
            logValidacion("Validación de contraseñas no coincidentes funcionando correctamente");
            finalizarPrueba("Validación Passwords", true);
            
        } catch (Exception e) {
            logger.error("Error en test de validación passwords: {}", e.getMessage(), e);
            capturarPantalla("error_validacion_passwords");
            finalizarPrueba("Validación Passwords", false);
            throw e;
        }
    }
    
    @Test(priority = 6,
          description = "Validación de términos y condiciones no aceptados",
          groups = {"regression", "registro", "negativo", "validacion"})
    @Story("Términos y Condiciones")
    @Severity(SeverityLevel.CRITICAL)
    @Description("Verifica que no se puede registrar sin aceptar términos")
    public void testValidacionTerminosNoAceptados() {
        
        iniciarPrueba("Validación Términos", "Verificar validación de términos y condiciones");
        
        try {
            ModeloDatosPrueba datos = ModeloDatosPrueba.builder()
                    .casoPrueba("REG_006")
                    .nombre("Pedro")
                    .apellido("Morales")
                    .email("pedro.morales.test." + System.currentTimeMillis() + "@email.com")
                    .password("Password123!")
                    .confirmarPassword("Password123!")
                    .aceptarTerminos(false)  // No acepta términos
                    .esValido(false)
                    .mensajeError("Debe aceptar los términos y condiciones")
                    .descripcion("Validación términos no aceptados")
                    .build();
            
            logPasoPrueba("Probando validación sin aceptar términos y condiciones");
            
            boolean registroFallido = paginaRegistro.registrarUsuario(datos);
            
            Assert.assertFalse(registroFallido, 
                "El registro debería fallar sin aceptar términos y condiciones");
            
            Assert.assertTrue(paginaRegistro.hayErroresValidacion(), 
                "Deberían mostrarse errores de validación de términos");
            
            logValidacion("Validación de términos y condiciones funcionando correctamente");
            finalizarPrueba("Validación Términos", true);
            
        } catch (Exception e) {
            logger.error("Error en test de validación términos: {}", e.getMessage(), e);
            capturarPantalla("error_validacion_terminos");
            finalizarPrueba("Validación Términos", false);
            throw e;
        }
    }
    
    // ===== PRUEBAS CON DATAPROVIDER (OPCIONAL) =====
    
    @DataProvider(name = "datosRegistroValido")
    public Object[][] proveerDatosRegistroValido() {
        try {
            return LectorDatosPrueba.obtenerDatosRegistroValido();
        } catch (Exception e) {
            logger.warn("Error leyendo datos CSV, usando datos de ejemplo: {}", e.getMessage());
            
            // Datos de fallback
            return new Object[][] {
                {ModeloDatosPrueba.registroValido("REG_DP_001", "Usuario1", "Apellido1", "user1.dp@test.com", "Pass123!")},
                {ModeloDatosPrueba.registroValido("REG_DP_002", "Usuario2", "Apellido2", "user2.dp@test.com", "Pass456!")}
            };
        }
    }
    
    @Test(dataProvider = "datosRegistroValido",
          priority = 10,
          description = "Registro con múltiples conjuntos de datos válidos",
          groups = {"dataprovider", "regression", "registro"})
    @Story("Registro con DataProvider")
    @Severity(SeverityLevel.NORMAL)
    public void testRegistroConDatosCSV(ModeloDatosPrueba datos) {
        
        iniciarPrueba("Registro DataProvider", "Registro con datos desde CSV: " + datos.getCasoPrueba());
        
        try {
            // Hacer email único
            String emailUnico = datos.getEmail().replace("@", "." + System.currentTimeMillis() + "@");
            ModeloDatosPrueba datosUnicos = ModeloDatosPrueba.builder()
                    .casoPrueba(datos.getCasoPrueba())
                    .nombre(datos.getNombre())
                    .apellido(datos.getApellido())
                    .email(emailUnico)
                    .password(datos.getPassword())
                    .confirmarPassword(datos.getConfirmarPassword())
                    .telefono(datos.getTelefono())
                    .genero(datos.getGenero())
                    .aceptarTerminos(datos.isAceptarTerminos())
                    .descripcion(datos.getDescripcion())
                    .build();
            
            logPasoPrueba("Ejecutando registro con datos CSV: " + datosUnicos.getCasoPrueba());
            
            boolean registroExitoso = paginaRegistro.registrarUsuario(datosUnicos);
            
            if (datosUnicos.isEsValido()) {
                Assert.assertTrue(registroExitoso, 
                    "El registro debería ser exitoso para caso: " + datosUnicos.getCasoPrueba());
            } else {
                Assert.assertFalse(registroExitoso, 
                    "El registro debería fallar para caso: " + datosUnicos.getCasoPrueba());
            }
            
            logValidacion("Caso DataProvider ejecutado correctamente: " + datosUnicos.getCasoPrueba());
            finalizarPrueba("Registro DataProvider " + datosUnicos.getCasoPrueba(), true);
            
        } catch (Exception e) {
            logger.error("Error en test DataProvider para caso {}: {}", datos.getCasoPrueba(), e.getMessage(), e);
            capturarPantalla("error_dataprovider_" + datos.getCasoPrueba());
            finalizarPrueba("Registro DataProvider " + datos.getCasoPrueba(), false);
            throw e;
        }
    }
}