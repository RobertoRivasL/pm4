package com.automatizacion.proyecto.datos;

import com.automatizacion.proyecto.utilidades.LectorDatosPrueba;
import org.testng.annotations.DataProvider;

public class ProveedorDatos {
    
    @DataProvider(name = "datosRegistroCSV")
    public static Object[][] proveerDatosRegistroCSV() {
        return LectorDatosPrueba.leerDatosCSV("datos/usuarios_registro.csv")
                .stream()
                .map(dato -> new Object[]{dato})
                .toArray(Object[][]::new);
    }
    
    @DataProvider(name = "datosRegistroExcel")
    public static Object[][] proveerDatosRegistroExcel() {
        return LectorDatosPrueba.leerDatosExcel("datos/usuarios_registro.xlsx")
                .stream()
                .map(dato -> new Object[]{dato})
                .toArray(Object[][]::new);
    }
    
    @DataProvider(name = "datosLoginValidos")
    public static Object[][] proveerDatosLoginValidos() {
        return new Object[][]{
            {ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_001")
                .email("practice")
                .password("SuperSecretPassword!")
                .esValido(true)
                .build()},
            {ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_002")
                .email("usuario.valido@test.com")
                .password("Password123!")
                .esValido(true)
                .build()}
        };
    }
    
    @DataProvider(name = "datosLoginInvalidos")
    public static Object[][] proveerDatosLoginInvalidos() {
        return new Object[][]{
            {ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_FAIL_001")
                .email("usuario.inexistente@test.com")
                .password("PasswordIncorrecto!")
                .esValido(false)
                .mensajeError("Invalid credentials")
                .build()},
            {ModeloDatosPrueba.builder()
                .casoPrueba("LOGIN_FAIL_002")
                .email("email-invalido")
                .password("Password123!")
                .esValido(false)
                .mensajeError("Invalid email format")
                .build()}
        };
    }
    
    @DataProvider(name = "datosRegistroValidos")
    public static Object[][] proveerDatosRegistroValidos() {
        return new Object[][]{
            {ModeloDatosPrueba.builder()
                .casoPrueba("REG_VALID_001")
                .nombre("Roberto")
                .email("roberto.test." + System.currentTimeMillis() + "@test.com")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(true)
                .aceptarTerminos(true)
                .build()},
            {ModeloDatosPrueba.builder()
                .casoPrueba("REG_VALID_002")
                .nombre("Usuario Prueba")
                .email("usuario.prueba." + System.currentTimeMillis() + "@test.com")
                .password("SecurePass456#")
                .confirmacionPassword("SecurePass456#")
                .esValido(true)
                .aceptarTerminos(true)
                .build()}
        };
    }
    
    @DataProvider(name = "datosRegistroInvalidos")
    public static Object[][] proveerDatosRegistroInvalidos() {
        return new Object[][]{
            {ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALID_001")
                .nombre("Usuario")
                .email("email-invalido")
                .password("Password123!")
                .confirmacionPassword("Password123!")
                .esValido(false)
                .mensajeError("Invalid email")
                .build()},
            {ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALID_002")
                .nombre("Usuario")
                .email("usuario@test.com")
                .password("Password123!")
                .confirmacionPassword("DiferentePassword!")
                .esValido(false)
                .mensajeError("Passwords do not match")
                .build()},
            {ModeloDatosPrueba.builder()
                .casoPrueba("REG_INVALID_003")
                .nombre("")
                .email("")
                .password("")
                .confirmacionPassword("")
                .esValido(false)
                .mensajeError("Required fields empty")
                .build()}
        };
    }
}