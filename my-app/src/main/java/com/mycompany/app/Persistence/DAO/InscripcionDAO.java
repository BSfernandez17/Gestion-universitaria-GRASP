package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Inscripcion;
import com.mycompany.app.DTO.InscripcionDTO;

public class InscripcionDAO {

  private Connection connection;

  public InscripcionDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(InscripcionDTO i) {
    String sql = "INSERT INTO inscripciones (id,curso_id, estudiante_id, año, semestre) VALUES (?, ?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setDouble(2, i.getCursoDTO().getID());
      ps.setDouble(3, i.getEstudianteDTO().getID());
      ps.setInt(4, i.getAño());
      ps.setInt(5, i.getSemestre());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
private Double generateID(){
  String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM inscripciones";
  try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
    if (rs.next()) return rs.getDouble("next_id");
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return Math.floor(Math.random() * 900000) + 1000;
}
  public List<Inscripcion> listar() {
    List<Inscripcion> lista = new ArrayList<>();
    EstudianteDAO eDao=new EstudianteDAO(connection);
    CursoDAO cDAO=new CursoDAO(connection);
    String sql = "SELECT * FROM inscripciones";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        Double cursoID=rs.getDouble("curso_id");
        Double estudianteID=rs.getDouble("estudiante_id");
        Integer año = rs.getInt("año");
        Integer semestre = rs.getInt("semestre");
        Inscripcion insc = new Inscripcion(id, cDAO.buscarPorId(cursoID), año, semestre, eDao.buscarPorID(estudianteID));
        lista.add(insc);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return lista;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM inscripciones WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void actualizar(InscripcionDTO i) {
    String sql = "UPDATE inscripciones SET curso_id=?, estudiante_id=?, año=?, semestre=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, i.getCursoDTO().getID());
      ps.setDouble(2, i.getEstudianteDTO().getID());
      ps.setInt(3, i.getAño());
      ps.setInt(4, i.getSemestre());
      ps.setDouble(5, i.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
}
