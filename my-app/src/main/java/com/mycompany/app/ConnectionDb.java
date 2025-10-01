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

  public static Connection getConnection() {
    if (connection == null) {
      try {
        // Cargar archivo de configuración
        Properties props = new Properties();
        FileInputStream fis = new FileInputStream("src/main/java/com/mycompany/app/Config/Databaseconf.properties");
        props.load(fis);

        // Crear adapter con la factory
        DataBaseAdapter adapter = DataBaseFactory.create(props);

        // Obtener conexión
        connection = adapter.getConnection();

        System.out.println("✅ Conexión establecida con " + props.getProperty("db.type"));
        if (props.getProperty("db.type").equals("h2") || props.getProperty("db.type").equals("mysql")) {

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
}
