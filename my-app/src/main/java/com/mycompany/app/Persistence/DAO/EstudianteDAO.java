package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.Model.Persona;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Model.Programa;

public class EstudianteDAO {

  private Connection connection;

  public EstudianteDAO(Connection connection) {
    this.connection = connection;
  }
  
public Estudiante buscarPorCodigo(Double codigo) {
    String sql = """
        SELECT 
            e.id AS estudiante_id,
            e.codigo,
            e.promedio,
            e.activo,
            p.id AS persona_id,
            p.nombre,
            p.apellido,
            p.email,
            pr.id as programa_id
        FROM estudiantes e
        JOIN personas p ON e.persona_id = p.id
        JOIN programas pr ON e.programa_id = pr.id
        WHERE e.codigo = ?
        """;
    ProgramaDAO programaDAO = new ProgramaDAO(connection);
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDouble(1, codigo);
        try (ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                Programa programa = programaDAO.buscarPorId(rs.getDouble("programa_id"));
                Estudiante estudiante = new Estudiante(
                    rs.getDouble("estudiante_id"),
                    rs.getString("nombre"),
                    rs.getString("apellido"),
                    rs.getString("email"),
                    rs.getDouble("codigo"),
                    programa,
                    rs.getBoolean("activo"),
                    rs.getDouble("promedio")
                );

                return estudiante;
            }
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }

    return null; // Si no se encontró
}



  
  public Estudiante buscarPorNombre(String nombre){
    String sql = "SELECT e.id AS estudiante_id, p.nombre, p.apellido, p.email, pr.id AS programa, e.codigo, e.promedio, e.activo " +
           "FROM estudiantes e " +
           "JOIN personas p ON e.persona_id = p.id " +
           "JOIN programas pr ON e.programa_id = pr.id " +
           "WHERE p.nombre = ?";
    ProgramaDAO programaDAO = new ProgramaDAO(connection);
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, nombre);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Double id = rs.getDouble("estudiante_id");
          String nombres = rs.getString("nombre");
          String apellidos = rs.getString("apellido");
          String email = rs.getString("email");
          Double codigo = rs.getDouble("codigo");
          Boolean activo = rs.getBoolean("activo");
          Double promedio = rs.getDouble("promedio");
          Programa programa = programaDAO.buscarPorId(rs.getDouble("programa"));
          return new Estudiante(id, nombres, apellidos, email, codigo, programa, activo, promedio);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }
  public Estudiante buscarPorID(double ID){
    String sql = "SELECT e.id AS estudiante_id, p.nombre, p.apellido, p.email, pr.id AS programa, e.codigo, e.promedio, e.activo " +
           "FROM estudiantes e " +
           "JOIN personas p ON e.persona_id = p.id " +
           "JOIN programas pr ON e.programa_id = pr.id " +
           "WHERE e.id = ?";
    ProgramaDAO programaDAO = new ProgramaDAO(connection);
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, ID);
      try (ResultSet rs = ps.executeQuery()) {
        if (rs.next()) {
          Double id = rs.getDouble("estudiante_id");
          String nombres = rs.getString("nombre");
          String apellidos = rs.getString("apellido");
          String email = rs.getString("email");
          Double codigo = rs.getDouble("codigo");
          Boolean activo = rs.getBoolean("activo");
          Double promedio = rs.getDouble("promedio");
          Programa programa = programaDAO.buscarPorId(rs.getDouble("programa"));
          return new Estudiante(id, nombres, apellidos, email, codigo, programa, activo, promedio);
        }
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return null;
  }
  public void insertar(EstudianteDTO e) {
    // Asegurar que exista la persona referenciada por persona_id
    PersonaDAO personaDAO = new PersonaDAO(connection);
    Persona persona = personaDAO.buscarPorId(e.getID());
    if (persona == null) {
      // Crear persona a partir de los datos del EstudianteDTO respetando el ID
      com.mycompany.app.DTO.PersonaDTO p = new com.mycompany.app.DTO.PersonaDTO(
          e.getID(), e.getNombres(), e.getApellidos(), e.getEmail());
      personaDAO.insertarConId(p);
    }

    String sql = "INSERT INTO estudiantes (id, persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setDouble(2, e.getID());
      ps.setDouble(3, e.getCodigo());
      ps.setDouble(4, e.getPrograma().getID());
      ps.setBoolean(5, e.getActivo());
      ps.setDouble(6, e.getPromedio());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private Double generateID() {
    String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM estudiantes";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      if (rs.next()) {
        return rs.getDouble("next_id");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    // Fallback si falla la consulta
    return Math.floor(Math.random() * 900000) + 1000;
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
        Programa programa = programaDAO.buscarPorId(rs.getDouble("programa"));
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
    // Actualiza primero persona (nombre, apellido, email)
    String updatePersona = "UPDATE personas SET nombre=?, apellido=?, email=? WHERE id=(SELECT persona_id FROM estudiantes WHERE id=?)";
    try (PreparedStatement ps = connection.prepareStatement(updatePersona)) {
      ps.setString(1, e.getNombres());
      ps.setString(2, e.getApellidos());
      ps.setString(3, e.getEmail());
      ps.setDouble(4, e.getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }

    // Luego actualiza datos propios del estudiante
    String updateEstudiante = "UPDATE estudiantes SET codigo=?, programa_id=?, activo=?, promedio=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(updateEstudiante)) {
      ps.setDouble(1, e.getCodigo());
      ps.setDouble(2, e.getPrograma().getID());
      ps.setBoolean(3, e.getActivo());
      ps.setDouble(4, e.getPromedio());
      ps.setDouble(5, e.getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
