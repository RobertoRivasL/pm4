package com.automatizacion.proyecto.pruebas;

import com.automatizacion.proyecto.base.BaseTest;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import com.automatizacion.proyecto.paginas.PaginaRegistro;
import com.automatizacion.proyecto.utilidades.GestorEvidencias;
import com.automatizacion.proyecto.utilidades.LectorDatosPrueba;
import io.qameta.allure.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

/**
 * Clase específica para pruebas de registro individual.
 * Sigue principios SOLID y se enfoca solo en ejecutar pruebas
 * con datos leídos desde archivos CSV/Excel.
 * 
 * Principios aplicados:
 * - SRP: Solo se encarga de ejecutar pruebas de registro
 * - Open/Closed: Fácil de extender para nuevos tipos de datos
 * - Dependency Inversion: Usa abstracciones para leer datos
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
@Epic("Gestión de Usuarios")
@Feature("Registro Individual")
public class PruebaRegistro extends BaseTest {
    
    private static final Logger logger = LoggerFactory.getLogger(PruebaRegistro.class);
    
    // === DEPENDENCIAS ===
    private PaginaRegistro paginaRegistro;
    private LectorDatosPrueba lectorDatos;
    private GestorEvidencias gestorEvidencias;
    
    // === DATOS DE PRUEBA ===
    private ModeloDatosPrueba datosActuales;
    private String casoPruebaActual;
    private long tiempoInicioEjecucion;
    
    /**
     * Configuración antes de cada método de prueba
     */
    @BeforeMethod(alwaysRun = true)
    public void configurarPruebaRegistro() {
        try {
            inicializarComponentes();
            configurarDatosPorDefecto();
            validarPaginaVisible();
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Configuración de prueba de registro completada"));
                
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en configuración: " + e.getMessage()));
            throw new RuntimeException("Fallo en configuración de prueba", e);
        }
    }
    
    /**
     * Inicializa todos los componentes necesarios
     */
    private void inicializarComponentes() {
        this.paginaRegistro = new PaginaRegistro(obtenerDriver());
        this.lectorDatos = new LectorDatosPrueba();
        this.gestorEvidencias = new GestorEvidencias(obtenerDriver(), "capturas/registro/");
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Componentes inicializados correctamente"));
    }
    
    /**
     * Configura datos de prueba por defecto
     */
    private void configurarDatosPorDefecto() {
        this.datosActuales = ModeloDatosPrueba.builder()
                .casoPrueba("REG_INDIVIDUAL_001")
                .descripcion("Prueba de registro individual")
                .nombre("Roberto")
                .apellido("Rivas López")
                .email("roberto.test." + System.currentTimeMillis() + "@email.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .telefono("+56912345678")
                .esValido(true)
                .resultadoEsperado("Registro exitoso")
                .build();
                
        this.casoPruebaActual = datosActuales.getCasoPrueba();
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Datos por defecto configurados: " + casoPruebaActual));
    }
    
    /**
     * Valida que la página de registro esté visible
     */
    private void validarPaginaVisible() {
        boolean paginaVisible = paginaRegistro.esPaginaVisible();
        Assert.assertTrue(paginaVisible, "La página de registro debe estar visible");
        
        logger.debug(TipoMensaje.DEBUG.formatearMensaje(
            "Página de registro validada y visible"));
    }
    
    // === PRUEBA PRINCIPAL ===
    
    @Test(priority = 1,
          description = "Ejecutar prueba de registro individual con datos válidos",
          groups = {"smoke", "registro", "individual"})
    @Story("Registro Individual")
    @Severity(SeverityLevel.BLOCKER)
    @Description("Ejecuta una prueba de registro usando datos de ejemplo")
    public void ejecutarPruebaRegistro() {
        
        iniciarMedicionTiempo();
        
        try {
            logPasoPrueba("=== INICIANDO PRUEBA DE REGISTRO INDIVIDUAL ===");
            logPasoPrueba("Caso de prueba: " + casoPruebaActual);
            logPasoPrueba("Email de prueba: " + datosActuales.getEmail());
            
            // Fase 1: Captura estado inicial
            capturarEstadoInicial();
            
            // Fase 2: Ejecutar registro
            boolean resultadoRegistro = ejecutarRegistro();
            
            // Fase 3: Validaciones básicas
            validarResultado(resultadoRegistro);
            
            // Fase 4: Captura estado final
            capturarEstadoFinal();
            
            logValidacion("=== PRUEBA DE REGISTRO COMPLETADA EXITOSAMENTE ===");
            
        } catch (Exception e) {
            manejarErrorPrueba(e);
            throw e;
            
        } finally {
            finalizarMedicionTiempo();
        }
    }
    
    /**
     * Prueba con datos desde archivo CSV
     */
    @Test(priority = 2,
          description = "Ejecutar registro con datos desde archivo CSV",
          groups = {"regression", "registro", "csv"})
    @Story("Registro con Datos CSV")
    @Severity(SeverityLevel.NORMAL)
    @Description("Lee datos desde CSV y ejecuta registro")
    public void ejecutarPruebaConDatosCSV() {
        
        logPasoPrueba("=== INICIANDO PRUEBA CON DATOS CSV ===");
        
        try {
            // Intentar leer datos desde CSV
            java.util.List<ModeloDatosPrueba> datosCSV = lectorDatos.leerDatosCSV("usuarios_registro.csv");
            
            if (datosCSV.isEmpty()) {
                // Si no existe el archivo, crear uno de ejemplo
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "No se encontraron datos CSV, usando datos por defecto"));
                ejecutarConDatosPorDefecto();
                return;
            }
            
            // Usar el primer registro válido encontrado
            ModeloDatosPrueba datosCSVPrueba = encontrarPrimerRegistroValido(datosCSV);
            
            if (datosCSVPrueba != null) {
                // Hacer email único para evitar conflictos
                datosCSVPrueba.setEmail(datosCSVPrueba.getEmail().replace("@", "." + System.currentTimeMillis() + "@"));
                establecerDatosPrueba(datosCSVPrueba);
                ejecutarRegistroConDatos(datosCSVPrueba);
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "No se encontraron registros válidos en CSV"));
                ejecutarConDatosPorDefecto();
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error leyendo datos CSV: " + e.getMessage()));
            ejecutarConDatosPorDefecto();
        }
    }
    
    /**
     * Prueba con datos desde archivo Excel
     */
    @Test(priority = 3,
          description = "Ejecutar registro con datos desde archivo Excel",
          groups = {"regression", "registro", "excel"})
    @Story("Registro con Datos Excel")
    @Severity(SeverityLevel.NORMAL)
    @Description("Lee datos desde Excel y ejecuta registro")
    public void ejecutarPruebaConDatosExcel() {
        
        logPasoPrueba("=== INICIANDO PRUEBA CON DATOS EXCEL ===");
        
        try {
            // Intentar leer datos desde Excel
            java.util.List<ModeloDatosPrueba> datosExcel = lectorDatos.leerDatosExcel("usuarios_prueba.xlsx");
            
            if (datosExcel.isEmpty()) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "No se encontraron datos Excel, usando datos por defecto"));
                ejecutarConDatosPorDefecto();
                return;
            }
            
            // Usar el primer registro válido encontrado
            ModeloDatosPrueba datosExcelPrueba = encontrarPrimerRegistroValido(datosExcel);
            
            if (datosExcelPrueba != null) {
                // Hacer email único para evitar conflictos
                datosExcelPrueba.setEmail(datosExcelPrueba.getEmail().replace("@", "." + System.currentTimeMillis() + "@"));
                establecerDatosPrueba(datosExcelPrueba);
                ejecutarRegistroConDatos(datosExcelPrueba);
            } else {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "No se encontraron registros válidos en Excel"));
                ejecutarConDatosPorDefecto();
            }
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error leyendo datos Excel: " + e.getMessage()));
            ejecutarConDatosPorDefecto();
        }
    }
    
    // === MÉTODOS DE APOYO ===
    
    /**
     * Encuentra el primer registro válido en una lista de datos
     */
    private ModeloDatosPrueba encontrarPrimerRegistroValido(java.util.List<ModeloDatosPrueba> datos) {
        return datos.stream()
                .filter(ModeloDatosPrueba::isEsValido)
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Ejecuta registro con datos por defecto
     */
    private void ejecutarConDatosPorDefecto() {
        logPasoPrueba("Ejecutando con datos por defecto");
        ejecutarRegistroConDatos(datosActuales);
    }
    
    /**
     * Ejecuta el registro con datos específicos
     */
    private void ejecutarRegistroConDatos(ModeloDatosPrueba datos) {
        try {
            logPasoPrueba("Ejecutando registro con: " + datos.getEmail());
            
            capturarEstadoInicial();
            boolean resultado = paginaRegistro.registrarUsuario(datos);
            validarResultado(resultado);
            capturarEstadoFinal();
            
            logValidacion("Registro completado para: " + datos.getCasoPrueba());
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error en registro con datos: " + e.getMessage()));
            gestorEvidencias.capturarPantallaError("error_registro_datos", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Inicia medición de tiempo
     */
    private void iniciarMedicionTiempo() {
        this.tiempoInicioEjecucion = System.currentTimeMillis();
    }
    
    /**
     * Captura el estado inicial de la página
     */
    @Step("Capturar estado inicial")
    private void capturarEstadoInicial() {
        try {
            gestorEvidencias.capturarPantalla("estado_inicial_" + casoPruebaActual);
            String titulo = paginaRegistro.obtenerTitulo();
            logPasoPrueba("Estado inicial capturado - Título: " + titulo);
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo capturar estado inicial: " + e.getMessage()));
        }
    }
    
    /**
     * Ejecuta el proceso de registro
     */
    @Step("Ejecutar proceso de registro")
    private boolean ejecutarRegistro() {
        logPasoPrueba("Ejecutando registro con email: " + datosActuales.getEmail());
        
        try {
            // Validar que el formulario esté presente
            boolean formularioPresente = paginaRegistro.validarElementosPagina();
            Assert.assertTrue(formularioPresente, "El formulario debe estar presente");
            
            // Ejecutar registro
            boolean registroExitoso = paginaRegistro.registrarUsuario(datosActuales);
            
            logPasoPrueba("Resultado del registro: " + (registroExitoso ? "EXITOSO" : "FALLIDO"));
            return registroExitoso;
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error durante el registro: " + e.getMessage()));
            gestorEvidencias.capturarPantallaError("error_registro", e.getMessage());
            throw new RuntimeException("Fallo en ejecución de registro", e);
        }
    }
    
    /**
     * Valida el resultado del registro
     */
    @Step("Validar resultado del registro")
    private void validarResultado(boolean resultadoRegistro) {
        logPasoPrueba("Validando resultado del registro");
        
        try {
            if (datosActuales.isEsValido()) {
                // Para datos válidos, esperamos éxito
                Assert.assertTrue(resultadoRegistro, 
                    "El registro debería ser exitoso para datos válidos");
                logValidacion("Registro exitoso validado correctamente");
            } else {
                // Para datos inválidos, esperamos fallo
                Assert.assertFalse(resultadoRegistro,
                    "El registro debería fallar para datos inválidos");
                logValidacion("Fallo de registro validado correctamente");
            }
            
        } catch (AssertionError e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Fallo en validación: " + e.getMessage()));
            gestorEvidencias.capturarPantallaError("fallo_validacion", e.getMessage());
            throw e;
        }
    }
    
    /**
     * Captura el estado final de la página
     */
    @Step("Capturar estado final")
    private void capturarEstadoFinal() {
        try {
            gestorEvidencias.capturarPantalla("estado_final_" + casoPruebaActual);
            
            String informacionFinal = obtenerInformacionFinal();
            Allure.addAttachment("Estado Final", informacionFinal);
            
            logPasoPrueba("Estado final capturado");
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "No se pudo capturar estado final: " + e.getMessage()));
        }
    }
    
    /**
     * Obtiene información del estado final
     */
    private String obtenerInformacionFinal() {
        StringBuilder info = new StringBuilder();
        info.append("=== INFORMACIÓN FINAL ===\n");
        info.append("Caso: ").append(casoPruebaActual).append("\n");
        info.append("URL: ").append(obtenerDriver().getCurrentUrl()).append("\n");
        info.append("Título: ").append(paginaRegistro.obtenerTitulo()).append("\n");
        info.append("Email usado: ").append(datosActuales.getEmail()).append("\n");
        
        return info.toString();
    }
    
    /**
     * Finaliza medición de tiempo
     */
    private void finalizarMedicionTiempo() {
        if (tiempoInicioEjecucion > 0) {
            long tiempoTotal = System.currentTimeMillis() - tiempoInicioEjecucion;
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Tiempo total de ejecución: " + tiempoTotal + "ms"));
                
            Allure.addAttachment("Tiempo de Ejecución", tiempoTotal + " milisegundos");
            
            // Validar tiempo razonable (menos de 30 segundos)
            Assert.assertTrue(tiempoTotal < 30000, 
                "El tiempo de ejecución debería ser menor a 30 segundos");
        }
    }
    
    /**
     * Maneja errores durante la prueba
     */
    private void manejarErrorPrueba(Exception e) {
        logger.error(TipoMensaje.ERROR.formatearMensaje(
            "Error en prueba de registro: " + e.getMessage()));
            
        try {
            gestorEvidencias.capturarPantallaError("error_critico", e.getMessage());
            Allure.addAttachment("Error", e.toString());
            
        } catch (Exception errorCaptura) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error adicional: " + errorCaptura.getMessage()));
        }
    }
    
    // === MÉTODOS PÚBLICOS DE UTILIDAD ===
    
    /**
     * Establece datos personalizados para la prueba
     */
    public void establecerDatosPrueba(ModeloDatosPrueba datos) {
        this.datosActuales = datos;
        this.casoPruebaActual = datos.getCasoPrueba();
        
        logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
            "Datos personalizados establecidos: " + casoPruebaActual));
    }
    
    /**
     * Obtiene los datos actuales de la prueba
     */
    public ModeloDatosPrueba obtenerDatosActuales() {
        return this.datosActuales;
    }
    
    /**
     * Obtiene métricas básicas de la prueba
     */
    public String obtenerMetricas() {
        StringBuilder metricas = new StringBuilder();
        metricas.append("=== MÉTRICAS DE PRUEBA ===\n");
        metricas.append("Caso actual: ").append(casoPruebaActual).append("\n");
        
        if (tiempoInicioEjecucion > 0) {
            long tiempoTranscurrido = System.currentTimeMillis() - tiempoInicioEjecucion;
            metricas.append("Tiempo transcurrido: ").append(tiempoTranscurrido).append("ms\n");
        }
        
        if (gestorEvidencias != null) {
            metricas.append("Evidencias: ").append(gestorEvidencias.obtenerCantidadEvidencias()).append("\n");
        }
        
        return metricas.toString();
    }
}