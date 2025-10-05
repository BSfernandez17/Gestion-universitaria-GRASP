package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Curso;
import com.mycompany.app.Model.Programa;
import com.mycompany.app.DTO.CursoDTO;

public class CursoDAO {

  private Connection connection;

  public CursoDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(CursoDTO c) {
    ProgramaDAO prDAO=new ProgramaDAO(connection);
    String sql = "INSERT INTO cursos (id,nombre, programa_id, activo) VALUES (?,?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setString(2, c.getNombre());
      ps.setDouble(3, prDAO.buscarPorNombre(c.getProgramaDTO().getNombre()).getID());
      ps.setBoolean(4, c.getActivo());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  private Integer generateID(){
    Integer counter= listar().size();
    return counter++;
  }
  public List<Curso> listar() {
    List<Curso> cursos = new ArrayList<>();
    String sql = "SELECT * FROM cursos";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombre = rs.getString("nombre");
        // Paso 1: obtener el programa_id
        Double programaId = rs.getDouble("programa_id");

        // Paso 2: usar ProgramaDAO para obtener el objeto Programa
        ProgramaDAO programaDao = new ProgramaDAO(connection);
        Programa programa = programaDao.buscarPorId(programaId);
        Curso c = new Curso(id, nombre, programa, rs.getBoolean("activo"));
        cursos.add(c);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return cursos;
  }

  public Curso buscarPorId(Double id) {
    Curso curso = null;
    String sql = "SELECT * FROM cursos WHERE id = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Double cursoId = rs.getDouble("id");
        String nombre = rs.getString("nombre");
        Boolean activo = rs.getBoolean("activo");

        // Paso 1: obtener el programa_id
        Double programaId = rs.getDouble("programa_id");

        // Paso 2: usar ProgramaDAO para obtener el objeto Programa
        ProgramaDAO programaDao = new ProgramaDAO(connection);
        Programa programa = programaDao.buscarPorId(programaId);

        // Paso 3: crear el objeto Curso
        curso = new Curso(cursoId, nombre, programa, activo);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return curso;
  }
 public Curso buscarPorNombre(String nombre){
  Curso curso = null;
  String sql = "SELECT * FROM cursos WHERE nombre = ?";
  try (PreparedStatement ps = connection.prepareStatement(sql)) {
    ps.setString(1, nombre);
    ResultSet rs = ps.executeQuery();
    if (rs.next()) {
      Double cursoId = rs.getDouble("id");
      String nombreCurso = rs.getString("nombre");
      Boolean activo = rs.getBoolean("activo");
      Double programaId = rs.getDouble("programa_id");
      ProgramaDAO programaDao = new ProgramaDAO(connection);
      Programa programa = programaDao.buscarPorId(programaId);
      curso = new Curso(cursoId, nombreCurso, programa, activo);
    }
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return curso;
 }
  public void eliminar(Integer id) {
    String sql = "DELETE FROM cursos WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setInt(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void actualizar(CursoDTO c) {
    String sql = "UPDATE cursos SET nombre=?, programa_id=?, activo=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, c.getNombre());
      ps.setDouble(2, c.getProgramaDTO().getID());
      ps.setBoolean(3, c.getActivo());
      ps.setDouble(4, c.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
