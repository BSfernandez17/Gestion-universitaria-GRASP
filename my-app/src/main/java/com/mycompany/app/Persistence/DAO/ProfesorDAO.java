package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.DTO.ProfesorDTO;
import com.mycompany.app.Model.Profesor;

public class ProfesorDAO {

  private Connection connection;

  public ProfesorDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(ProfesorDTO p) {
    String pStatement = "SQL INSERT INTO profesores (nombres, apellidos, email, especialidad) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(pStatement)) {
      ps.setString(1, p.getNombres());
      ps.setString(2, p.getApellidos());
      ps.setString(3, p.getEmail());
      ps.setString(4, p.getTipoContrato());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Profesor> listar() {
    List<Profesor> profesores = new ArrayList<>();
    String sql = "SELECT * FROM profesores";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        String email = rs.getString("email");
        String especialidad = rs.getString("especialidad");
        Profesor p = new Profesor(id, nombres, apellidos, email, especialidad);
        profesores.add(p);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return profesores;
  }

  public void actualizar(ProfesorDTO p) {
    String sql = "UPDATE profesores SET nombres=?, apellidos=?, email=?, especialidad=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, p.getNombres());
      ps.setString(2, p.getApellidos());
      ps.setString(3, p.getEmail());
      ps.setString(4, p.getTipoContrato());
      ps.setDouble(5, p.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM profesores WHERE id=?";
    try (
        PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
