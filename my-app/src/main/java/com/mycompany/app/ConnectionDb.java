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

/**
 * Helper que crea conexiones a la base de datos a partir de la
 * configuración y la factory. No mantiene estado ni singleton — la
 * responsabilidad de reutilización/gestión de la conexión queda en la
 * capa superior (por ejemplo, una factory o el contexto de la aplicación).
 *
 * Uso: Connection conn = ConnectionDb.createConnection();
 * Cerrar: ConnectionDb.closeConnection(conn);
 */
public final class ConnectionDb {

  private ConnectionDb() {
    // no instanciable
  }

  /**
   * Crea y devuelve una nueva conexión según la configuración en
   * `Config/Databaseconf.properties`. No cachea la conexión.
   */
  public static Connection createConnection() {
    Connection conn = null;
    try {
      Properties props = new Properties();
      FileInputStream fis = new FileInputStream("src/main/java/com/mycompany/app/Config/Databaseconf.properties");
      props.load(fis);

      DataBaseAdapter adapter = DataBaseFactory.create(props);
      conn = adapter.getConnection();

      System.out.println("✅ Conexión establecida con " + props.getProperty("db.type"));

      if (props.getProperty("db.type").equals("h2") || props.getProperty("db.type").equals("mysql")) {
        try {
          DatabaseConfig.init(conn);
          DatabaseSeed.seed(conn);
          System.out.println("✅ Tablas creadas o ya existían");
        } catch (Exception e) {
          System.out.println("⚠️ Error al inicializar la base de datos: " + e.getMessage());
          e.printStackTrace();
        }
      } else {
        DatabaseConfigOracle.init(conn);
        DatabaseOracleSeed.seed(conn);
      }

    } catch (IOException e) {
      System.out.println("⚠️ Error al leer archivo de configuración: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("⚠️ Error al crear la conexión: " + e.getMessage());
      e.printStackTrace();
    }

    return conn;
  }

  /**
   * Cierra la conexión pasada si no es null.
   */
  public static void closeConnection(Connection conn) {
    if (conn == null) return;
    try {
      conn.close();
      System.out.println("🔒 Conexión cerrada");
    } catch (Exception e) {
      System.out.println("⚠️ Error al cerrar la conexión: " + e.getMessage());
    }
  }
}
