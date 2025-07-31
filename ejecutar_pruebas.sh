#!/bin/bash

echo "=========================================================="
echo "    SUITE DE AUTOMATIZACIÓN FUNCIONAL"
echo "    Roberto Rivas López - Curso de Automatización"
echo "=========================================================="
echo

# Verificar si Java y Maven están instalados
if ! command -v java &> /dev/null; then
    echo "ERROR: Java no está instalado o no está en el PATH"
    echo "Por favor instale Java 21 o superior"
    exit 1
fi

if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven no está instalado o no está en el PATH"
    echo "Por favor instale Maven 3.9.x o superior"
    exit 1
fi

echo "✓ Java y Maven detectados correctamente"
echo

# Mostrar menú de opciones
while true; do
    echo "=========================================================="
    echo "                    MENÚ DE OPCIONES"
    echo "=========================================================="
    echo
    echo "1. Ejecutar TODAS las pruebas"
    echo "2. Ejecutar solo pruebas de LOGIN"
    echo "3. Ejecutar solo pruebas de REGISTRO"
    echo "4. Ejecutar pruebas SMOKE (críticas)"
    echo "5. Ejecutar Cross-Browser Testing"
    echo "6. Ejecutar en modo HEADLESS"
    echo "7. Generar reporte Allure"
    echo "8. Limpiar proyecto"
    echo "9. Salir"
    echo
    read -p "Seleccione una opción (1-9): " opcion

    case $opcion in
        1)
            echo
            echo "Ejecutando TODAS las pruebas..."
            echo "=========================================================="
            mvn clean test
            ;;
        2)
            echo
            echo "Ejecutando pruebas de LOGIN..."
            echo "=========================================================="
            mvn clean test -Dtest=PruebasLogin
            ;;
        3)
            echo
            echo "Ejecutando pruebas de REGISTRO..."
            echo "=========================================================="
            mvn clean test -Dtest=PruebasRegistro
            ;;
        4)
            echo
            echo "Ejecutando pruebas SMOKE (críticas)..."
            echo "=========================================================="
            mvn clean test -Dgroups=smoke
            ;;
        5)
            echo
            echo "Ejecutando Cross-Browser Testing..."
            echo "=========================================================="
            mvn clean test -Dgroups=cross-browser
            ;;
        6)
            echo
            echo "Ejecutando pruebas en modo HEADLESS..."
            echo "=========================================================="
            mvn clean test -DmodoSinCabeza=true
            ;;
        7)
            echo
            echo "Generando reporte Allure..."
            echo "=========================================================="
            mvn allure:report
            echo
            read -p "¿Desea abrir el reporte en el navegador? (s/n): " abrir
            if [[ $abrir == "s" || $abrir == "S" ]]; then
                mvn allure:serve
            fi
            ;;
        8)
            echo
            echo "Limpiando proyecto..."
            echo "=========================================================="
            mvn clean
            rm -rf capturas allure-results allure-report logs
            echo "Proyecto limpiado exitosamente."
            ;;
        9)
            echo
            echo "=========================================================="
            echo "           ¡Gracias por usar la Suite de"
            echo "            Automatización Funcional!"
            echo
            echo "        Desarrollado por: Roberto Rivas López"
            echo "        Curso: Automatización de Pruebas"
            echo "=========================================================="
            echo
            exit 0
            ;;
        *)
            echo "Opción inválida. Por favor seleccione 1-9."
            ;;
    esac
    
    echo
    read -p "Presione Enter para continuar..."
    echo
done
