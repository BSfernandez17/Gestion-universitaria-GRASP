package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Model.Programa;

public class EstudianteDAO {

  private Connection connection;

  public EstudianteDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(EstudianteDTO e) {
    ProgramaDAO programaDAO = new ProgramaDAO(connection);
    String sql = "INSERT INTO estudiantes (id,persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?,?,?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setDouble(2, e.getID());
      ps.setDouble(3, e.getCodigo());
      ps.setDouble(4, programaDAO.buscarPorNombre(e.getPrograma().getNombre()).getID());
      ps.setBoolean(5, e.getActivo());
      ps.setDouble(6, e.getPromedio());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private Integer generateID() {
    Integer counter = listar().size();
    return counter++;
  }

  public List<Estudiante> listar() {
    ProgramaDAO programaDAO = new ProgramaDAO(connection);
    List<Estudiante> estudiantes = new ArrayList<>();
    String sql = "SELECT e.id AS estudiante_id," +
        "p.nombre," +
        "p.apellido," +
        "p.email," +
        "pr.id AS programa," +
        "e.codigo," +
        "e.promedio," +
        "e.activo " +
        "FROM estudiantes e " +
        "JOIN personas p ON e.persona_id=p.id " +
        "JOIN programas pr ON e.programa_id=pr.id";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("estudiante_id");
        String nombres = rs.getString("nombre");
        String apellidos = rs.getString("apellido");
        String email = rs.getString("email");
        Double codigo = rs.getDouble("codigo");
        Boolean activo = rs.getBoolean("activo");
        Double promedio = rs.getDouble("promedio");
        Programa programa = programaDAO.buscarPorId(id);
        Estudiante est = new Estudiante(id, nombres, apellidos, email, codigo, programa, activo, promedio);
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
    String sql = "UPDATE estudiantes SET nombre=?, apellido=?, email=?, codigo=?, programa_id=?, activo=?, promedio=? WHERE id=?";
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
