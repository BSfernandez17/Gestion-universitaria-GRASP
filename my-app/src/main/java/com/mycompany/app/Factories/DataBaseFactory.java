package com.mycompany.app.Factories;

import java.util.Properties;

import com.mycompany.app.Persistence.adapters.*;

public class DataBaseFactory {
  public static DataBaseAdapter create(Properties props) {

    String type = props.getProperty("db.type").toLowerCase();

    switch (type) {
      case "mysql":
        return new MySqlAdapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
      case "oracle":
        return new OracleAdapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
      case "h2":
        return new H2Adapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
      default:
        throw new IllegalArgumentException("Unsupported DB type: " + type);
    }
  }

}
