package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.mycompany.app.Model.Curso;
import com.mycompany.app.Model.CursoProfesor;
import com.mycompany.app.Model.Profesor;
import com.mycompany.app.DTO.CursoProfesorDTO;

public class CursoProfesorDAO {

  private Connection connection;

  public CursoProfesorDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(CursoProfesorDTO cp) {
    CursoDAO cursoDAO = new CursoDAO(connection);
    ProfesorDAO profesorDAO = new ProfesorDAO(connection);
    Profesor profesor = profesorDAO.buscarPorEmail(cp.getProfesorDTO().getEmail());
    if (profesor == null) {
      System.out.println("Error: El profesor con email '" + cp.getProfesorDTO().getEmail() + "' no existe. No se puede asociar al curso.");
      return;
    }
    String sql = "INSERT INTO curso_profesor (id,curso_id, profesor_id, año, semestre) VALUES (?, ?, ?, ?,?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, cp.getID());
      ps.setDouble(2, cursoDAO.buscarPorNombre(cp.getCursoDTO().getNombre()).getID());
      ps.setDouble(3, profesor.getID());
      ps.setInt(4, cp.getAño());
      ps.setInt(5, cp.getSemestre());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  private Integer generateID(){
    Integer counter=listar().size();
    return counter++;
  }

public List<CursoProfesor> listar() {
    List<CursoProfesor> lista = new ArrayList<>();
    String sql = "SELECT * FROM curso_profesor";

    // Instanciamos los DAOs de Curso y Profesor
    CursoDAO cursoDAO = new CursoDAO(connection);
    ProfesorDAO profesorDAO = new ProfesorDAO(connection);

    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {

        while (rs.next()) {
            Double id = rs.getDouble("id");
            Double cursoId = rs.getDouble("curso_id");
            Double profesorId = rs.getDouble("profesor_id");
            Integer año = rs.getInt("año");
            Integer semestre = rs.getInt("semestre");

            // Traemos los objetos completos desde la base
            Curso curso = cursoDAO.buscarPorId(cursoId);
            Profesor profesor = profesorDAO.buscarPorId(profesorId);

            CursoProfesor cp = new CursoProfesor(id, profesor, año, semestre, curso);
            lista.add(cp);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return lista;
}



  public void eliminar(Double id) {
    String sql = "DELETE FROM curso_profesor WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void actualizar(CursoProfesorDTO cp) {
    String sql = "UPDATE curso_profesor SET curso_id=?, profesor_id=?, año=?, semestre=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, cp.getCursoDTO().getID());
      ps.setDouble(2, cp.getProfesorDTO().getID());
      ps.setInt(3, cp.getAño());
      ps.setInt(4, cp.getSemestre());
      ps.setDouble(5, cp.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}