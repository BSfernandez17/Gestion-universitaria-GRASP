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
    String sql = "INSERT INTO cursos (id, nombre, programa_id, activo) VALUES (?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setString(2, c.getNombre());
      ps.setDouble(3, c.getProgramaDTO().getID());
      ps.setBoolean(4, c.getActivo());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  private Double generateID(){
    String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM cursos";
    try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      if (rs.next()) return rs.getDouble("next_id");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Math.floor(Math.random() * 900000) + 1000;
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
  public void eliminar(Double id) {
    // We'll delete dependent rows first to satisfy FK constraints, inside a transaction.
    boolean initialAutoCommit = true;
    try {
      initialAutoCommit = connection.getAutoCommit();
      connection.setAutoCommit(false);

      try (PreparedStatement delCursoProfesor = connection.prepareStatement("DELETE FROM curso_profesor WHERE curso_id = ?");
           PreparedStatement delInscripciones = connection.prepareStatement("DELETE FROM inscripciones WHERE curso_id = ?");
           PreparedStatement delCurso = connection.prepareStatement("DELETE FROM cursos WHERE id = ?")) {

        System.out.println("[CursoDAO] eliminar transaction start for id=" + id);

        delCursoProfesor.setDouble(1, id);
        int cpDeleted = delCursoProfesor.executeUpdate();
        System.out.println("[CursoDAO] deleted " + cpDeleted + " rows from curso_profesor for curso_id=" + id);

        delInscripciones.setDouble(1, id);
        int insDeleted = delInscripciones.executeUpdate();
        System.out.println("[CursoDAO] deleted " + insDeleted + " rows from inscripciones for curso_id=" + id);

        delCurso.setDouble(1, id);
        int cursosDeleted = delCurso.executeUpdate();
        System.out.println("[CursoDAO] deleted " + cursosDeleted + " rows from cursos for id=" + id);

        connection.commit();
        System.out.println("[CursoDAO] transaction committed for delete id=" + id);

        // Notify observers only after successful commit
        com.mycompany.app.Patterns.Observer.CursoSubject.getInstance().notify(new com.mycompany.app.Patterns.Observer.CursoEvent(com.mycompany.app.Patterns.Observer.CursoEvent.Action.DELETE, id));
        System.out.println("[CursoDAO] notified SUBJECT for DELETE id=" + id);
      }
    } catch (SQLException e) {
      try {
        connection.rollback();
        System.out.println("[CursoDAO] transaction rolled back due to error: " + e.getMessage());
      } catch (SQLException ex) {
        System.out.println("[CursoDAO] rollback failed: " + ex.getMessage());
      }
      e.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(initialAutoCommit);
      } catch (SQLException ex) {
        System.out.println("[CursoDAO] could not restore autoCommit: " + ex.getMessage());
      }
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
