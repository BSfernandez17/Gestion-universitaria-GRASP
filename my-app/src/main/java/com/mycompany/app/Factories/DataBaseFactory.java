package com.mycompany.app.Factories;

import java.util.Properties;

import com.mycompany.app.Persistence.adapters.*;

public class DataBaseFactory {

  // Simple cache to reuse adapters per DB type (memoization)
  private static final java.util.Map<String, DataBaseAdapter> CACHE = new java.util.concurrent.ConcurrentHashMap<>();

  public static DataBaseAdapter create(Properties props) {

    String type = props.getProperty("db.type").toLowerCase();

    // Return cached adapter if present
    if (CACHE.containsKey(type)) return CACHE.get(type);

    DataBaseAdapter adapter;
    switch (type) {
      case "mysql":
        adapter = new MySqlAdapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
        break;
      case "oracle":
        adapter = new OracleAdapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
        break;
      case "h2":
        adapter = new H2Adapter(
            props.getProperty("db.url"),
            props.getProperty("db.username"),
            props.getProperty("db.password"));
        break;
      default:
        throw new IllegalArgumentException("Unsupported DB type: " + type);
    }

    CACHE.put(type, adapter);
    return adapter;
  }

}
