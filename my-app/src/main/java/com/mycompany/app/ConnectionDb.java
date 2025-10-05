package com.mycompany.app;

import java.sql.Connection;
import java.sql.DriverManager;
import com.mycompany.app.Persistence.adapters.DatabaseConfig;

/**
 * Clase encargada de manejar la conexión única (singleton) a la base de datos.
 * 
 * Esta versión:
 *  - Establece la conexión una sola vez y la reutiliza.
 *  - Inicializa la estructura de tablas si no existen.
 *  - NO ejecuta el seeder automáticamente (se ejecuta manualmente desde App.java o MainFX.java).
 */
public class ConnectionDb {

    // Conexión única compartida en toda la aplicación
    private static Connection connection;

    /**
     * Devuelve una conexión activa a la base de datos.
     * Si no existe, crea una nueva y configura la base.
     */
    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                // ⚙️ URL de la base de datos (ajusta según tu entorno)
                String url = "jdbc:sqlite:database.db"; // Ejemplo con SQLite local
                connection = DriverManager.getConnection(url);
                System.out.println("✅ Conexión establecida con la base de datos.");
                // Inicializar esquema de la base de datos (crear tablas si no existen)
                try {
                    DatabaseConfig.init(connection);
                } catch (Exception e) {
                    System.err.println("⚠️ Error al inicializar esquema DB: " + e.getMessage());
                    e.printStackTrace();
                }
                System.out.println("ℹ️ Esquema DB inicializado (si fue necesario). No se ejecuta automáticamente el seed.");
            }
        } catch (Exception e) {
            System.err.println("❌ Error al conectar con la base de datos: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Cierra la conexión de forma segura.
     */
    public static void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔒 Conexión cerrada correctamente.");
            }
        } catch (Exception e) {
            System.err.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
