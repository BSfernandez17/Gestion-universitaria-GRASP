package com.mycompany.app.Persistence.adapters;

import java.sql.Connection;
import java.sql.SQLException;

public interface DataBaseAdapter {
  Connection getConnection() throws SQLException;
}
