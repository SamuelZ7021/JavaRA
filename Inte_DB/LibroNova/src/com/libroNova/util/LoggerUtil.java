package com.libroNova.util;

import java.io.IOException;
import java.util.logging.*;


public class LoggerUtil {

    private static final Logger logger = Logger.getLogger(LoggerUtil.class.getName());
    static {
        try {
            // Leemos la ruta del archivo de log desde config.properties
            String logFilePath = PropertiesLoader.getProperty("log.file.path");
            if (logFilePath == null || logFilePath.trim().isEmpty()) {
                logFilePath = "app.log"; // Un valor por defecto si no está en el properties
            }

            // Creamos un FileHandler para escribir los logs en un archivo.
            // El 'true' indica que se añadirán logs al archivo existente (append).
            FileHandler fileHandler = new FileHandler(logFilePath, true);

            // Usamos un SimpleFormatter para un formato de log legible (Fecha, Nivel, Mensaje).
            SimpleFormatter formatter = new SimpleFormatter();
            fileHandler.setFormatter(formatter);

            // Añadimos nuestro manejador de archivo al logger.
            logger.addHandler(fileHandler);

            // Evitamos que los logs se muestren también en la consola.
            logger.setUseParentHandlers(false);

            // Configuramos el nivel de log. INFO y superiores serán registrados.
            logger.setLevel(Level.INFO);

        } catch (IOException e) {
            System.err.println("Error: No se pudo configurar el logger. Los logs no funcionarán.");
            e.printStackTrace();
        }
    }

    /**
     * Registra un mensaje informativo en el log.
     * @param message El mensaje a registrar.
     */
    public static void logInfo(String message) {
        logger.info(message);
    }

    /**
     * Registra una advertencia en el log.
     * @param message El mensaje de advertencia.
     */
    public static void logWarning(String message) {
        logger.warning(message);
    }

    /**
     * Registra un error grave en el log, incluyendo la traza de la excepción.
     * @param message Un mensaje descriptivo del error.
     * @param thrown La excepción que se capturó.
     */
    public static void logError(String message, Throwable thrown) {
        logger.log(Level.SEVERE, message, thrown);
    }
}