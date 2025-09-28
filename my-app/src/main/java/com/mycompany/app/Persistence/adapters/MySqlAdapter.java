package com.mycompany.app.Persistence.adapters;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySqlAdapter implements DataBaseAdapter {
  private String url;
  private String username;
  private String password;

  public MySqlAdapter(String url, String username, String password) {
    this.url = url;
    this.username = username;
    this.password = password;
  }

  @Override
  public Connection getConnection() throws SQLException {
    return DriverManager.getConnection(url, username, password);
  }
}
