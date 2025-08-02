package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.configuracion.ConfiguracionGlobal;
import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase responsable de leer datos de prueba desde diferentes fuentes externas.
 * Soporta archivos CSV, Excel y otros formatos de datos.
 * 
 * Implementa el principio de Responsabilidad Única y proporciona
 * abstracción para diferentes fuentes de datos.
 * 
 * @author Roberto Rivas Lopez
 * @version 1.0
 */
public class LectorDatosPrueba {
    
    private static final Logger logger = LoggerFactory.getLogger(LectorDatosPrueba.class);
    private static final String SEPARADOR_CSV = ",";
    
    private final ConfiguracionGlobal configuracion;
    private final String directorioBase;
    
    /**
     * Constructor que inicializa el lector con la configuración global
     */
    public LectorDatosPrueba() {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
        this.directorioBase = configuracion.obtenerDirectorioDatos();
        verificarDirectorioBase();
    }
    
    /**
     * Constructor que permite especificar un directorio personalizado
     * @param directorioPersonalizado directorio donde buscar los archivos de datos
     */
    public LectorDatosPrueba(String directorioPersonalizado) {
        this.configuracion = ConfiguracionGlobal.obtenerInstancia();
        this.directorioBase = directorioPersonalizado;
        verificarDirectorioBase();
    }
    
    /**
     * Verifica que el directorio base existe
     */
    private void verificarDirectorioBase() {
        Path directorio = Paths.get(directorioBase);
        if (!Files.exists(directorio)) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Directorio de datos no existe: " + directorioBase));
        } else {
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Directorio de datos configurado: " + directorioBase));
        }
    }
    
    // === MÉTODOS PARA LEER ARCHIVOS CSV ===
    
    /**
     * Lee datos de prueba desde un archivo CSV
     * @param nombreArchivo nombre del archivo CSV (sin ruta)
     * @return lista de ModeloDatosPrueba
     */
    public List<ModeloDatosPrueba> leerDatosCSV(String nombreArchivo) {
        String rutaCompleta = construirRutaArchivo(nombreArchivo);
        return leerDatosCSV(new File(rutaCompleta));
    }
    
    /**
     * Lee datos de prueba desde un archivo CSV
     * @param archivo archivo CSV a leer
     * @return lista de ModeloDatosPrueba
     */
    public List<ModeloDatosPrueba> leerDatosCSV(File archivo) {
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        if (!archivo.exists()) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Archivo CSV no encontrado: " + archivo.getAbsolutePath()));
            return datos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            String[] encabezados = null;
            int numeroLinea = 0;
            
            while ((linea = reader.readLine()) != null) {
                numeroLinea++;
                
                if (linea.trim().isEmpty()) {
                    continue; // Saltar líneas vacías
                }
                
                String[] valores = procesarLineaCSV(linea);
                
                if (numeroLinea == 1) {
                    // Primera línea son los encabezados
                    encabezados = valores;
                    logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                        "Encabezados CSV: " + String.join(", ", encabezados)));
                    continue;
                }
                
                if (encabezados != null && valores.length > 0) {
                    ModeloDatosPrueba modelo = mapearCSVAModelo(encabezados, valores, numeroLinea);
                    if (modelo != null) {
                        datos.add(modelo);
                    }
                }
            }
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Leídos " + datos.size() + " registros desde " + archivo.getName()));
                
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error leyendo archivo CSV: " + e.getMessage()));
        }
        
        return datos;
    }
    
    /**
     * Procesa una línea de CSV considerando comillas y comas
     * @param linea línea a procesar
     * @return array de valores
     */
    private String[] procesarLineaCSV(String linea) {
        List<String> valores = new ArrayList<>();
        StringBuilder valorActual = new StringBuilder();
        boolean dentroComillas = false;
        
        for (int i = 0; i < linea.length(); i++) {
            char caracter = linea.charAt(i);
            
            if (caracter == '"') {
                dentroComillas = !dentroComillas;
            } else if (caracter == ',' && !dentroComillas) {
                valores.add(valorActual.toString().trim());
                valorActual.setLength(0);
            } else {
                valorActual.append(caracter);
            }
        }
        
        // Agregar el último valor
        valores.add(valorActual.toString().trim());
        
        return valores.toArray(new String[0]);
    }
    
    /**
     * Mapea valores CSV a un modelo de datos de prueba
     * @param encabezados array de encabezados
     * @param valores array de valores
     * @param numeroLinea número de línea para logging
     * @return ModeloDatosPrueba mapeado
     */
    private ModeloDatosPrueba mapearCSVAModelo(String[] encabezados, String[] valores, int numeroLinea) {
        try {
            ModeloDatosPrueba.Builder builder = ModeloDatosPrueba.builder();
            
            for (int i = 0; i < Math.min(encabezados.length, valores.length); i++) {
                String encabezado = encabezados[i].toLowerCase().trim();
                String valor = valores[i].trim();
                
                mapearCampoCSV(builder, encabezado, valor);
            }
            
            // Establecer número de línea como parte del caso de prueba si no existe
            ModeloDatosPrueba datos = builder.build();
            if (datos.getCasoPrueba() == null || datos.getCasoPrueba().isEmpty()) {
                datos.setCasoPrueba("CSV_LINEA_" + numeroLinea);
            }
            
            return datos;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error mapeando línea " + numeroLinea + ": " + e.getMessage()));
            return null;
        }
    }
    
    /**
     * Mapea un campo específico del CSV al builder
     * @param builder builder del modelo
     * @param encabezado nombre del encabezado
     * @param valor valor a mapear
     */
    private void mapearCampoCSV(ModeloDatosPrueba.Builder builder, String encabezado, String valor) {
        if (valor.isEmpty()) {
            return; // No mapear valores vacíos
        }
        
        switch (encabezado) {
            case "caso_prueba":
            case "test_case":
                builder.casoPrueba(valor);
                break;
            case "descripcion":
            case "description":
                builder.descripcion(valor);
                break;
            case "nombre":
            case "name":
            case "first_name":
                builder.nombre(valor);
                break;
            case "apellido":
            case "lastname":
            case "last_name":
                builder.apellido(valor);
                break;
            case "email":
            case "correo":
                builder.email(valor);
                break;
            case "password":
            case "contraseña":
                builder.password(valor);
                break;
            case "confirmar_password":
            case "confirm_password":
            case "confirmacion_password":
                builder.confirmacionPassword(valor);
                break;
            case "telefono":
            case "phone":
            case "telephone":
                builder.telefono(valor);
                break;
            case "genero":
            case "gender":
                builder.genero(valor);
                break;
            case "pais":
            case "country":
                builder.pais(valor);
                break;
            case "ciudad":
            case "city":
                builder.ciudad(valor);
                break;
            case "fecha_nacimiento":
            case "birth_date":
                builder.fechaNacimiento(valor);
                break;
            case "navegador":
            case "browser":
                builder.navegador(valor);
                break;
            case "es_valido":
            case "is_valid":
            case "valid":
                builder.esValido(Boolean.parseBoolean(valor));
                break;
            case "resultado_esperado":
            case "expected_result":
                builder.resultadoEsperado(valor);
                break;
            case "mensaje_error":
            case "error_message":
                builder.mensajeError(valor);
                break;
            default:
                logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                    "Campo CSV no reconocido: " + encabezado));
                break;
        }
    }
    
    // === MÉTODOS PARA LEER ARCHIVOS EXCEL ===
    
    /**
     * Lee datos de prueba desde un archivo Excel
     * @param nombreArchivo nombre del archivo Excel
     * @return lista de ModeloDatosPrueba
     */
    public List<ModeloDatosPrueba> leerDatosExcel(String nombreArchivo) {
        String rutaCompleta = construirRutaArchivo(nombreArchivo);
        return leerDatosExcel(new File(rutaCompleta));
    }
    
    /**
     * Lee datos de prueba desde un archivo Excel
     * @param archivo archivo Excel a leer
     * @return lista de ModeloDatosPrueba
     */
    public List<ModeloDatosPrueba> leerDatosExcel(File archivo) {
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        if (!archivo.exists()) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Archivo Excel no encontrado: " + archivo.getAbsolutePath()));
            return datos;
        }
        
        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            // Leer la primera hoja por defecto
            Sheet sheet = workbook.getSheetAt(0);
            
            if (sheet.getPhysicalNumberOfRows() == 0) {
                logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                    "Archivo Excel está vacío: " + archivo.getName()));
                return datos;
            }
            
            // Obtener encabezados de la primera fila
            Row filaEncabezados = sheet.getRow(0);
            String[] encabezados = extraerEncabezadosExcel(filaEncabezados);
            
            logger.debug(TipoMensaje.DEBUG.formatearMensaje(
                "Encabezados Excel: " + String.join(", ", encabezados)));
            
            // Procesar filas de datos
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row fila = sheet.getRow(i);
                if (fila != null && !esFilaVacia(fila)) {
                    String[] valores = extraerValoresExcel(fila, encabezados.length);
                    ModeloDatosPrueba modelo = mapearExcelAModelo(encabezados, valores, i + 1);
                    if (modelo != null) {
                        datos.add(modelo);
                    }
                }
            }
            
            logger.info(TipoMensaje.INFORMATIVO.formatearMensaje(
                "Leídos " + datos.size() + " registros desde " + archivo.getName()));
                
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error leyendo archivo Excel: " + e.getMessage()));
        }
        
        return datos;
    }
    
    /**
     * Extrae encabezados de una fila de Excel
     * @param fila fila de encabezados
     * @return array de encabezados
     */
    private String[] extraerEncabezadosExcel(Row fila) {
        List<String> encabezados = new ArrayList<>();
        
        if (fila != null) {
            for (int i = 0; i < fila.getLastCellNum(); i++) {
                Cell celda = fila.getCell(i);
                encabezados.add(celda != null ? obtenerValorCelda(celda) : "");
            }
        }
        
        return encabezados.toArray(new String[0]);
    }
    
    /**
     * Extrae valores de una fila de Excel
     * @param fila fila de datos
     * @param cantidadColumnas cantidad de columnas esperadas
     * @return array de valores
     */
    private String[] extraerValoresExcel(Row fila, int cantidadColumnas) {
        String[] valores = new String[cantidadColumnas];
        
        for (int i = 0; i < cantidadColumnas; i++) {
            Cell celda = fila.getCell(i);
            valores[i] = celda != null ? obtenerValorCelda(celda) : "";
        }
        
        return valores;
    }
    
    /**
     * Obtiene el valor de una celda de Excel como String
     * @param celda celda de Excel
     * @return valor como String
     */
    private String obtenerValorCelda(Cell celda) {
        if (celda == null) {
            return "";
        }
        
        switch (celda.getCellType()) {
            case STRING:
                return celda.getStringCellValue().trim();
            case NUMERIC:
                if (DateUtil.isCellDateFormatted(celda)) {
                    return celda.getLocalDateTimeCellValue().toLocalDate().toString();
                } else {
                    // Evitar notación científica para números enteros
                    double valor = celda.getNumericCellValue();
                    if (valor == Math.floor(valor)) {
                        return String.valueOf((long) valor);
                    } else {
                        return String.valueOf(valor);
                    }
                }
            case BOOLEAN:
                return String.valueOf(celda.getBooleanCellValue());
            case FORMULA:
                return celda.getCellFormula();
            case BLANK:
            default:
                return "";
        }
    }
    
    /**
     * Verifica si una fila de Excel está vacía
     * @param fila fila a verificar
     * @return true si la fila está vacía
     */
    private boolean esFilaVacia(Row fila) {
        for (Cell celda : fila) {
            if (celda != null && !obtenerValorCelda(celda).isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    /**
     * Mapea valores Excel a un modelo de datos de prueba
     * @param encabezados array de encabezados
     * @param valores array de valores
     * @param numeroFila número de fila para logging
     * @return ModeloDatosPrueba mapeado
     */
    private ModeloDatosPrueba mapearExcelAModelo(String[] encabezados, String[] valores, int numeroFila) {
        try {
            ModeloDatosPrueba.Builder builder = ModeloDatosPrueba.builder();
            
            for (int i = 0; i < Math.min(encabezados.length, valores.length); i++) {
                String encabezado = encabezados[i].toLowerCase().trim();
                String valor = valores[i].trim();
                
                mapearCampoCSV(builder, encabezado, valor); // Reutilizar lógica de mapeo CSV
            }
            
            // Establecer número de fila como parte del caso de prueba si no existe
            ModeloDatosPrueba datos = builder.build();
            if (datos.getCasoPrueba() == null || datos.getCasoPrueba().isEmpty()) {
                datos.setCasoPrueba("EXCEL_FILA_" + numeroFila);
            }
            
            return datos;
            
        } catch (Exception e) {
            logger.warn(TipoMensaje.ADVERTENCIA.formatearMensaje(
                "Error mapeando fila Excel " + numeroFila + ": " + e.getMessage()));
            return null;
        }
    }
    
    // === MÉTODOS DE UTILIDAD ===
    
    /**
     * Construye la ruta completa de un archivo
     * @param nombreArchivo nombre del archivo
     * @return ruta completa
     */
    private String construirRutaArchivo(String nombreArchivo) {
        return directorioBase + File.separator + nombreArchivo;
    }
    
    /**
     * Obtiene la lista de archivos de datos disponibles
     * @return lista de nombres de archivos
     */
    public List<String> obtenerArchivosDisponibles() {
        List<String> archivos = new ArrayList<>();
        Path directorio = Paths.get(directorioBase);
        
        if (!Files.exists(directorio)) {
            return archivos;
        }
        
        try {
            Files.list(directorio)
                    .filter(path -> {
                        String nombre = path.getFileName().toString().toLowerCase();
                        return nombre.endsWith(".csv") || nombre.endsWith(".xlsx") || nombre.endsWith(".xls");
                    })
                    .forEach(path -> archivos.add(path.getFileName().toString()));
                    
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error listando archivos disponibles: " + e.getMessage()));
        }
        
        return archivos;
    }
    
    /**
     * Crea un archivo CSV de ejemplo con la estructura esperada
     * @param rutaArchivo ruta completa donde crear el archivo
     */
    public void crearArchivoEjemploCSV(String rutaArchivo) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(rutaArchivo))) {
            
            // Escribir encabezados
            writer.println("caso_prueba,descripcion,nombre,apellido,email,password,confirmar_password,telefono,es_valido,resultado_esperado,mensaje_error");
            
            // Escribir datos de ejemplo
            writer.println("REG_001,\"Usuario válido\",Roberto,Rivas,roberto.test@email.com,Password123!,Password123!,+56912345678,true,\"Registro exitoso\",");
            writer.println("REG_002,\"Email inválido\",Test,User,email-invalido,Password123!,Password123!,,false,\"Error de validación\",\"Email inválido\"");
            writer.println("REG_003,\"Contraseñas diferentes\",Another,User,another.test@email.com,Password123!,DiferentePass,+56987654321,false,\"Error de validación\",\"Contraseñas no coinciden\"");
            
            logger.info(TipoMensaje.EXITO.formatearMensaje(
                "Archivo CSV de ejemplo creado: " + rutaArchivo));
                
        } catch (IOException e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje(
                "Error creando archivo CSV de ejemplo: " + e.getMessage()));
        }
    }
    
    /**
     * Filtra datos por tipo de validez
     * @param datos lista de datos a filtrar
     * @param esValido true para datos válidos, false para inválidos
     * @return lista filtrada
     */
    public List<ModeloDatosPrueba> filtrarPorValidez(List<ModeloDatosPrueba> datos, boolean esValido) {
        return datos.stream()
                .filter(dato -> dato.isEsValido() == esValido)
                .collect(java.util.stream.Collectors.toList());
    }
    
    /**
     * Busca datos por caso de prueba específico
     * @param datos lista de datos donde buscar
     * @param casoPrueba caso de prueba a buscar
     * @return primer dato que coincida o null si no se encuentra
     */
    public ModeloDatosPrueba buscarPorCasoPrueba(List<ModeloDatosPrueba> datos, String casoPrueba) {
        return datos.stream()
                .filter(dato -> casoPrueba.equals(dato.getCasoPrueba()))
                .findFirst()
                .orElse(null);
    }
    
    /**
     * Obtiene estadísticas de los archivos de datos
     * @return string con estadísticas
     */
    public String obtenerEstadisticasArchivos() {
        List<String> archivos = obtenerArchivosDisponibles();
        StringBuilder stats = new StringBuilder();
        
        stats.append("=== ESTADÍSTICAS DE ARCHIVOS DE DATOS ===\n");
        stats.append("Directorio: ").append(directorioBase).append("\n");
        stats.append("Total de archivos: ").append(archivos.size()).append("\n");
        
        long csvCount = archivos.stream().mapToLong(a -> a.toLowerCase().endsWith(".csv") ? 1 : 0).sum();
        long excelCount = archivos.stream().mapToLong(a -> a.toLowerCase().endsWith(".xlsx") || a.toLowerCase().endsWith(".xls") ? 1 : 0).sum();
        
        stats.append("Archivos CSV: ").append(csvCount).append("\n");
        stats.append("Archivos Excel: ").append(excelCount).append("\n");
        
        if (!archivos.isEmpty()) {
            stats.append("Archivos encontrados:\n");
            archivos.forEach(archivo -> stats.append("  - ").append(archivo).append("\n"));
        }
        
        return stats.toString();
    }
    
    /**
     * Obtiene el directorio base configurado
     * @return directorio base de datos
     */
    public String obtenerDirectorioBase() {
        return directorioBase;
    }
}