package com.automatizacion.proyecto.utilidades;

import com.automatizacion.proyecto.datos.ModeloDatosPrueba;
import com.automatizacion.proyecto.enums.TipoMensaje;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class LectorDatosPrueba {
    
    private static final Logger logger = LoggerFactory.getLogger(LectorDatosPrueba.class);
    
    public static List<ModeloDatosPrueba> leerDatosCSV(String rutaArchivo) {
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(
                    LectorDatosPrueba.class.getClassLoader().getResourceAsStream(rutaArchivo), 
                    StandardCharsets.UTF_8))) {
            
            String linea;
            boolean primeraLinea = true;
            
            while ((linea = reader.readLine()) != null) {
                if (primeraLinea) {
                    primeraLinea = false;
                    continue; // Saltar cabeceras
                }
                
                String[] campos = linea.split(",");
                if (campos.length >= 4) {
                    ModeloDatosPrueba modelo = ModeloDatosPrueba.builder()
                            .casoPrueba(campos[0].trim())
                            .nombre(campos[1].trim())
                            .email(campos[2].trim())
                            .password(campos[3].trim())
                            .confirmacionPassword(campos[3].trim())
                            .esValido(Boolean.parseBoolean(campos.length > 4 ? campos[4].trim() : "true"))
                            .build();
                    datos.add(modelo);
                }
            }
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Datos CSV cargados: " + datos.size() + " registros"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error leyendo CSV: " + e.getMessage()));
        }
        
        return datos;
    }
    
    public static List<ModeloDatosPrueba> leerDatosExcel(String rutaArchivo) {
        List<ModeloDatosPrueba> datos = new ArrayList<>();
        
        try (InputStream inputStream = LectorDatosPrueba.class.getClassLoader().getResourceAsStream(rutaArchivo);
             Workbook workbook = new XSSFWorkbook(inputStream)) {
            
            Sheet sheet = workbook.getSheetAt(0);
            
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // Saltar cabeceras
                Row row = sheet.getRow(i);
                if (row != null) {
                    String casoPrueba = getCellValueAsString(row.getCell(0));
                    String nombre = getCellValueAsString(row.getCell(1));
                    String email = getCellValueAsString(row.getCell(2));
                    String password = getCellValueAsString(row.getCell(3));
                    boolean esValido = getCellValueAsBoolean(row.getCell(4));
                    
                    ModeloDatosPrueba modelo = ModeloDatosPrueba.builder()
                            .casoPrueba(casoPrueba)
                            .nombre(nombre)
                            .email(email)
                            .password(password)
                            .confirmacionPassword(password)
                            .esValido(esValido)
                            .build();
                    datos.add(modelo);
                }
            }
            
            logger.info(TipoMensaje.EXITO.formatearMensaje("Datos Excel cargados: " + datos.size() + " registros"));
            
        } catch (Exception e) {
            logger.error(TipoMensaje.ERROR.formatearMensaje("Error leyendo Excel: " + e.getMessage()));
        }
        
        return datos;
    }
    
    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue().trim();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "";
        }
    }
    
    private static boolean getCellValueAsBoolean(Cell cell) {
        if (cell == null) return true;
        
        switch (cell.getCellType()) {
            case BOOLEAN:
                return cell.getBooleanCellValue();
            case STRING:
                return Boolean.parseBoolean(cell.getStringCellValue().trim());
            default:
                return true;
        }
    }
}