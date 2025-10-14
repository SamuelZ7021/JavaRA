package com.bliNva.util;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Clase de utilidad para cargar el archivo de configuración config.properties.
 * Esto nos permite centralizar la lectura del archivo en un solo lugar.
 */
public class PropertiesLoader {

    private static final String CONFIG_FILE_PATH = "src/resources/config.properties";
    private static final Properties properties = new Properties();

    // Bloque estático para cargar las propiedades una sola vez cuando la clase es cargada por la JVM.
    static {
        try {
            // Usamos FileReader para leer el archivo.
            properties.load(new FileReader(CONFIG_FILE_PATH));
        } catch (IOException e) {
            // Si el archivo no se encuentra o no se puede leer, es un error crítico.
            // Imprimimos el error y terminamos la aplicación.
            System.err.println("Error: No se pudo cargar el archivo de configuración en la ruta: " + CONFIG_FILE_PATH);
            e.printStackTrace();
            // Podríamos lanzar una excepción más específica aquí si quisiéramos.
        }
    }

    /**
     * Obtiene una propiedad del archivo de configuración a través de su clave.
     * @param key La clave de la propiedad (ej. "db.url").
     * @return El valor de la propiedad como un String.
     */
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}