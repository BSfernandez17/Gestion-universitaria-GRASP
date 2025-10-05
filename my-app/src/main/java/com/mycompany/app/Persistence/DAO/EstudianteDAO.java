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
    // Insert person first (persona.id), then insert estudiante using the same id
    try {
      // personas insert (if not exists)
      String upsertPersona = "INSERT OR IGNORE INTO personas (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
      try (PreparedStatement ps1 = connection.prepareStatement(upsertPersona)) {
        ps1.setDouble(1, e.getID());
        ps1.setString(2, e.getNombres());
        ps1.setString(3, e.getApellidos());
        ps1.setString(4, e.getEmail());
        ps1.executeUpdate();
      }

      String sqlEst = "INSERT INTO estudiantes (id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement ps2 = connection.prepareStatement(sqlEst)) {
        ps2.setDouble(1, e.getID());
        ps2.setDouble(2, e.getCodigo());
        if (e.getPrograma() != null) ps2.setDouble(3, e.getPrograma().getID()); else ps2.setNull(3, java.sql.Types.DOUBLE);
        ps2.setBoolean(4, e.getActivo());
        ps2.setDouble(5, e.getPromedio());
        ps2.executeUpdate();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Estudiante> listar() {
    List<Estudiante> estudiantes = new ArrayList<>();
    // Join estudiantes with personas to get human-readable fields inserted by the seeders
  String sql = "SELECT e.id AS id, p.nombre AS nombres, p.apellido AS apellidos, p.email AS email, e.codigo AS codigo, e.activo AS activo, e.promedio AS promedio "
    + "FROM estudiantes e LEFT JOIN personas p ON e.id = p.id";
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
    // Update persona and estudiante tables
    try {
      String updPersona = "UPDATE personas SET nombre=?, apellido=?, email=? WHERE id=?";
      try (PreparedStatement ps1 = connection.prepareStatement(updPersona)) {
        ps1.setString(1, e.getNombres());
        ps1.setString(2, e.getApellidos());
        ps1.setString(3, e.getEmail());
        ps1.setDouble(4, e.getID());
        ps1.executeUpdate();
      }

      String updEst = "UPDATE estudiantes SET codigo=?, programa_id=?, activo=?, promedio=? WHERE id=?";
      try (PreparedStatement ps2 = connection.prepareStatement(updEst)) {
        ps2.setDouble(1, e.getCodigo());
        if (e.getPrograma() != null) ps2.setDouble(2, e.getPrograma().getID()); else ps2.setNull(2, java.sql.Types.DOUBLE);
        ps2.setBoolean(3, e.getActivo());
        ps2.setDouble(4, e.getPromedio());
        ps2.setDouble(5, e.getID());
        ps2.executeUpdate();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
