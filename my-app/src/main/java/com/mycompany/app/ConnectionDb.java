package com.mycompany.app;

import com.mycompany.app.Factories.DataBaseFactory;
import com.mycompany.app.Persistence.adapters.DataBaseAdapter;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

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
        FileInputStream fis = new FileInputStream("Config/Databaseconf.properties");
        props.load(fis);

        // Crear adapter con la factory
        DataBaseAdapter adapter = DataBaseFactory.create(props);

        // Obtener conexión
        connection = adapter.getConnection();
        System.out.println("✅ Conexión establecida con " + props.getProperty("db.type"));

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
