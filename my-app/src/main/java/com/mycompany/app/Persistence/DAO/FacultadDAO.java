package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.mycompany.app.Model.Facultad;
import com.mycompany.app.Model.Persona;
import com.mycompany.app.DTO.FacultadDTO;

public class FacultadDAO {

  private Connection connection;

  public FacultadDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(FacultadDTO f) {
    String sql = "INSERT INTO facultades (id, nombre, decano_id) VALUES (?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, generateID());
      ps.setString(2, f.getNombre());
      ps.setDouble(3, f.getDecanoDTO().getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public Double generateID() {
    String sql = "SELECT COALESCE(MAX(id), 0) + 1 AS next_id FROM facultades";
    try (PreparedStatement ps = connection.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
      if (rs.next()) return rs.getDouble("next_id");
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return Math.floor(Math.random() * 900000) + 1000;
  }

  public List<Facultad> listar() {
    List<Facultad> facultades = new ArrayList<>();
    String sql = "SELECT * FROM facultades";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        String nombre = rs.getString("nombre");
        Double facultadId = rs.getDouble("id");

        // Traer el decano asociado
        Double decanoId = rs.getDouble("decano_id");
        PersonaDAO personaDAO = new PersonaDAO(connection);
        Persona decano = personaDAO.buscarPorId(decanoId);

        // Crear objeto Facultad
        Facultad facultad = new Facultad(facultadId, nombre, decano);
        facultades.add(facultad);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return facultades;
  }

  public Facultad buscarPorNombre(String nombre) {
    Facultad facultad = null;
    String sql = "SELECT * FROM facultades WHERE nombre = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, nombre);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Double facultadId = rs.getDouble("id");
        String nombreFacultad = rs.getString("nombre");

        // Traer el decano asociado
        Double decanoId = rs.getDouble("decano_id");
        PersonaDAO personaDAO = new PersonaDAO(connection);
        Persona decano = personaDAO.buscarPorId(decanoId);

        // Crear objeto Facultad
        facultad = new Facultad(facultadId, nombreFacultad, decano);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return facultad;
  }

  public Facultad buscarPorId(Double id) {
    Facultad facultad = null;
    String sql = "SELECT * FROM facultades WHERE id = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ResultSet rs = ps.executeQuery();

      if (rs.next()) {
        Double facultadId = rs.getDouble("id");
        String nombre = rs.getString("nombre");

        // Traer el decano asociado
        Double decanoId = rs.getDouble("decano_id");
        PersonaDAO personaDAO = new PersonaDAO(connection);
        Persona decano = personaDAO.buscarPorId(decanoId);

        // Crear objeto Facultad
        facultad = new Facultad(facultadId, nombre, decano);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return facultad;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM facultades WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }

  public void actualizar(FacultadDTO f) {
    String sql = "UPDATE facultades SET nombre=?, decano_id=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, f.getNombre());
      ps.setDouble(2, f.getDecanoDTO().getID());
      ps.setDouble(3, f.getID());
      ps.executeUpdate();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
