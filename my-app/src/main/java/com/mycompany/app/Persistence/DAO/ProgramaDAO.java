package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.mycompany.app.Model.Facultad;
import com.mycompany.app.Model.Programa;
import com.mycompany.app.DTO.ProgramaDTO;

public class ProgramaDAO {

  private Connection connection;

  public ProgramaDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(ProgramaDTO p) {
    FacultadDAO facultadDAO = new FacultadDAO(connection);
    String sql = "INSERT INTO programas (id,nombre, duracion, registro, facultad_id) VALUES (?,?, ?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setString(2, p.getNombre());
      ps.setDouble(3, p.getDuracion());
      ps.setDate(4, new java.sql.Date(p.getRegistro().getTime()));
      ps.setDouble(5, facultadDAO.buscarPorNombre(p.getFacultadDTO().getNombre()).getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  private Double generateID() {
    Double counter = (double) listar().size();
    return counter++;
  }

  public List<Programa> listar() {
    List<Programa> programas = new ArrayList<>();
    String sql = "SELECT * FROM programas";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombre = rs.getString("nombre");
        Double duracion = rs.getDouble("duracion");
        Date registro = rs.getDate("registro");
        Programa prog = new Programa(id, nombre, duracion, registro, null);
        programas.add(prog);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return programas;
  }

  public Programa buscarPorNombre(String nombre) {
    Programa programa = null;
    String sql = "SELECT * FROM programas WHERE nombre = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, nombre);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Double programaId = rs.getDouble("id");
        String progNombre = rs.getString("nombre");
        Double duracion = rs.getDouble("duracion");
        Date registro = rs.getDate("registro");

        // Traer la facultad asociada
        Double facultadId = rs.getDouble("facultad_id");
        FacultadDAO facultadDAO = new FacultadDAO(connection);
        Facultad facultad = facultadDAO.buscarPorId(facultadId);

        // Crear objeto Programa
        programa = new Programa(programaId, progNombre, duracion, registro, facultad);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return programa;
  }

  public Programa buscarPorId(Double id) {
    Programa programa = null;
    String sql = "SELECT * FROM programas WHERE id = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Double programaId = rs.getDouble("id");
        String nombre = rs.getString("nombre");
        Double duracion = rs.getDouble("duracion");
        Date registro = rs.getDate("registro");

        // Traer la facultad asociada
        Double facultadId = rs.getDouble("facultad_id");
        FacultadDAO facultadDAO = new FacultadDAO(connection);
        Facultad facultad = facultadDAO.buscarPorId(facultadId);

        // Crear objeto Programa
        programa = new Programa(programaId, nombre, duracion, registro, facultad);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }

    return programa;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM programas WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void actualizar(ProgramaDTO p) {
    String sql = "UPDATE programas SET nombre=?, duracion=?, registro=?, facultad_id=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, p.getNombre());
      ps.setDouble(2, p.getDuracion());
      ps.setDate(3, new java.sql.Date(p.getRegistro().getTime()));
      ps.setDouble(4, p.getFacultadDTO().getID());
      ps.setDouble(5, p.getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
