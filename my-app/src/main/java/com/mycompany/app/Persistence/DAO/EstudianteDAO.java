package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.DTO.EstudianteDTO;

public class EstudianteDAO {

  private Connection connection;

  public EstudianteDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(EstudianteDTO e) {
    String sql = "INSERT INTO estudiantes (nombres, apellidos, email, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, e.getNombres());
      ps.setString(2, e.getApellidos());
      ps.setString(3, e.getEmail());
      ps.setDouble(4, e.getCodigo());
      ps.setDouble(5, e.getPrograma().getID());
      ps.setBoolean(6, e.getActivo());
      ps.setDouble(7, e.getPromedio());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Estudiante> listar() {
    List<Estudiante> estudiantes = new ArrayList<>();
    String sql = "SELECT * FROM estudiantes";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        String email = rs.getString("email");
        Double codigo = rs.getDouble("codigo");
        Boolean activo = rs.getBoolean("activo");
        Double promedio = rs.getDouble("promedio");
        Estudiante est = new Estudiante(id, nombres, apellidos, email, codigo, null, activo, promedio);
        estudiantes.add(est);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return estudiantes;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM estudiantes WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void actualizar(EstudianteDTO e) {
    String sql = "UPDATE estudiantes SET nombres=?, apellidos=?, email=?, codigo=?, programa_id=?, activo=?, promedio=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, e.getNombres());
      ps.setString(2, e.getApellidos());
      ps.setString(3, e.getEmail());
      ps.setDouble(4, e.getCodigo());
      ps.setDouble(5, e.getPrograma().getID());
      ps.setBoolean(6, e.getActivo());
      ps.setDouble(7, e.getPromedio());
      ps.setDouble(8, e.getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
