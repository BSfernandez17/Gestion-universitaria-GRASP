package com.mycompany.app;

import com.mycompany.app.Factories.DataBaseFactory;
import com.mycompany.app.Persistence.adapters.DataBaseAdapter;
import com.mycompany.app.Persistence.adapters.DatabaseConfig;
import com.mycompany.app.Persistence.adapters.DatabaseConfigOracle;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import com.mycompany.app.seeders.DatabaseOracleSeed;
import com.mycompany.app.seeders.DatabaseSeed;

public class ConnectionDb {

  private static Connection connection;

  private ConnectionDb() {
    // Constructor privado para que no se pueda instanciar
  }

  public static synchronized Connection getConnection() {
    if (connection == null) {
      try {
        // Cargar archivo de configuración
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/main/java/com/mycompany/app/Config/Databaseconf.properties");
        props.load(fis);

        // Require db.type to be explicitly set in the properties file.
        String dbType = props.getProperty("db.type");
        if (dbType == null || dbType.isBlank()) {
          throw new IOException("db.type not set in Databaseconf.properties. Please set db.type to one of: h2, mysql, oracle");
        }

        // Crear adapter con la factory
        DataBaseAdapter adapter = DataBaseFactory.create(props);

        // Obtener conexión
        connection = adapter.getConnection();

        System.out.println("✅ Conexión establecida con " + dbType);
        if ("h2".equals(dbType) || "mysql".equals(dbType)) {
          try {
            DatabaseConfig.init(connection);
            DatabaseSeed.seed(connection);
            System.out.println("✅ Tablas creadas o ya existían");
          } catch (Exception e) {
            System.out.println("⚠️ Error al inicializar la base de datos: " + e.getMessage());
            e.printStackTrace();
          }
        } else {
          DatabaseConfigOracle.init(connection);
          DatabaseOracleSeed.seed(connection);
        }
      } catch (IOException e) {
        System.out.println("⚠️ Error al leer archivo de configuración: " + e.getMessage());
      } catch (Exception e) {
        System.out.println("⚠️ Error al crear la conexión: " + e.getMessage());
        e.printStackTrace();
      }
    }
    return connection;
  }

  /**
   * Compatibility wrapper used by other code in the project. Previously
   * some callers used createConnection()/closeConnection(Connection).
   */
  public static Connection createConnection() {
    return getConnection();
  }

  public static void closeConnection(Connection conn) {
    if (conn == null) return;
    try {
      conn.close();
      // If we closed the shared singleton connection, clear reference
      if (conn == connection) connection = null;
      System.out.println("🔒 Conexión cerrada");
    } catch (Exception e) {
      System.out.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
    }
  }
}
