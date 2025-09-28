package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Facultad;
import com.mycompany.app.DTO.FacultadDTO;

public class FacultadDAO {

  private Connection connection;

  public FacultadDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(FacultadDTO f) {
    String sql = "INSERT INTO facultades (nombre, decano_id) VALUES (?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, f.getNombre());
      ps.setDouble(2, f.getDecanoDTO().getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Facultad> listar() {
    List<Facultad> facultades = new ArrayList<>();
    String sql = "SELECT * FROM facultades";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombre = rs.getString("nombre");
        Facultad fac = new Facultad(id, nombre, null);
        facultades.add(fac);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return facultades;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM facultades WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
