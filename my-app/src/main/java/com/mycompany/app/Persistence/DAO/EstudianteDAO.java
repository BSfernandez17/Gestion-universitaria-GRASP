package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Model.Programa;

public class EstudianteDAO {

  private Connection connection;

  public EstudianteDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(EstudianteDTO e) {
    // Generate an ID when missing and insert persona + estudiante
    try {
      Double id = e.getID();
      if (id == null) {
        id = IdGenerator.generateId();
      }

      // personas insert (if not exists)
      String upsertPersona = "INSERT OR IGNORE INTO personas (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
      try (PreparedStatement ps1 = connection.prepareStatement(upsertPersona)) {
        ps1.setDouble(1, id);
        ps1.setString(2, e.getNombres());
        ps1.setString(3, e.getApellidos());
        ps1.setString(4, e.getEmail());
        ps1.executeUpdate();
      }

      String sqlEst = "INSERT INTO estudiantes (id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";
      try (PreparedStatement ps2 = connection.prepareStatement(sqlEst)) {
        ps2.setDouble(1, id);
        ps2.setDouble(2, e.getCodigo() == null ? 0.0 : e.getCodigo());
        if (e.getPrograma() != null && e.getPrograma().getID() != null) ps2.setDouble(3, e.getPrograma().getID()); else ps2.setNull(3, java.sql.Types.DOUBLE);
        ps2.setBoolean(4, e.getActivo() == null ? false : e.getActivo());
        ps2.setDouble(5, e.getPromedio() == null ? 0.0 : e.getPromedio());
        ps2.executeUpdate();
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public List<Estudiante> listar() {
    List<Estudiante> estudiantes = new ArrayList<>();
    // Join estudiantes with personas and programas to get human-readable fields inserted by the seeders
  String sql = "SELECT e.id AS id, p.nombre AS nombres, p.apellido AS apellidos, p.email AS email, e.codigo AS codigo, e.activo AS activo, e.promedio AS promedio, e.programa_id AS programa_id, pr.nombre AS programa_nombre, pr.duracion AS programa_duracion, pr.registro AS programa_registro "
    + "FROM estudiantes e LEFT JOIN personas p ON e.id = p.id LEFT JOIN programas pr ON e.programa_id = pr.id";
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
        // Programa fields
        Double programaId = null;
        String programaNombre = null;
        Double programaDuracion = null;
        java.util.Date programaRegistro = null;
        try {
          programaId = rs.getObject("programa_id") == null ? null : rs.getDouble("programa_id");
          programaNombre = rs.getString("programa_nombre");
          programaDuracion = rs.getObject("programa_duracion") == null ? null : rs.getDouble("programa_duracion");
          java.sql.Date reg = rs.getDate("programa_registro");
          if (reg != null) programaRegistro = new java.util.Date(reg.getTime());
        } catch (Exception ex) {
          // ignore if no program
        }
        Programa prog = null;
        if (programaId != null) {
          prog = new Programa(programaId, programaNombre, programaDuracion, programaRegistro, null);
        }
        Estudiante est = new Estudiante(id, nombres, apellidos, email, codigo, prog, activo, promedio);
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

  public Estudiante buscarPorId(Double id) {
    Estudiante est = null;
    String sql = "SELECT e.id AS id, p.nombre AS nombres, p.apellido AS apellidos, p.email AS email, e.codigo AS codigo, e.activo AS activo, e.promedio AS promedio, e.programa_id AS programa_id "
      + "FROM estudiantes e LEFT JOIN personas p ON e.id = p.id WHERE e.id = ?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Double eid = rs.getDouble("id");
          String nombres = rs.getString("nombres");
          String apellidos = rs.getString("apellidos");
          String email = rs.getString("email");
          Double codigo = rs.getDouble("codigo");
          Boolean activo = rs.getBoolean("activo");
          Double promedio = rs.getDouble("promedio");
          Double programaId = rs.getObject("programa_id") == null ? null : rs.getDouble("programa_id");
          com.mycompany.app.Model.Programa prog = null;
          if (programaId != null) {
            com.mycompany.app.Persistence.DAO.ProgramaDAO pdao = new com.mycompany.app.Persistence.DAO.ProgramaDAO(connection);
            prog = pdao.buscarPorId(programaId);
          }
          est = new Estudiante(eid, nombres, apellidos, email, codigo, prog, activo, promedio);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return est;
  }

  // Insert an estudiante row referencing an existing persona id
  public boolean insertarForPersona(Double personaId, Double codigo, Double programaId) {
    String sql = "INSERT INTO estudiantes (id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, personaId);
      ps.setDouble(2, codigo == null ? 0.0 : codigo);
      if (programaId != null) ps.setDouble(3, programaId); else ps.setNull(3, java.sql.Types.DOUBLE);
      ps.setBoolean(4, true);
      ps.setDouble(5, 0.0);
      ps.executeUpdate();
      return true;
    } catch (SQLException ex) {
      ex.printStackTrace();
      return false;
    }
  }
}
