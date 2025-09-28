package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.Model.Persona;
import com.mycompany.app.DTO.PersonaDTO;

public class PersonaDAO {

  private Connection connection;

  public PersonaDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(PersonaDTO p) {
    String pStatement = "SQL INSERT INTO personas (nombres, apellidos, email) VALUES (?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(pStatement)) {
      ps.setString(1, p.getNombres());
      ps.setString(2, p.getApellidos());
      ps.setString(3, p.getEmail());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public List<Persona> listar() {
    List<Persona> personas = new ArrayList<>();
    String sql = "SELECT * FROM personas";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombres");
        String apellidos = rs.getString("apellidos");
        String email = rs.getString("email");
        Persona p = new Persona(id, nombres, apellidos, email);
        personas.add(p);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return personas;
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM personas WHERE id=?";
    try (
        PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void actualizar(PersonaDTO pDto) {
    String sql = "UPDATE personas SET nombres=?, apellidos=?, email=? WHERE id=?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, pDto.getNombres());
      ps.setString(2, pDto.getApellidos());
      ps.setString(3, pDto.getEmail());
      ps.setDouble(4, pDto.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
