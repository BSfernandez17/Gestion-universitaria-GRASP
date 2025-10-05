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
    public Persona buscarPorId(Double id) {
        Persona persona = null;
        String sql = "SELECT * FROM personas WHERE id = ?";

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDouble(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Double personaId = rs.getDouble("id");
                String nombres = rs.getString("nombre");
                String apellidos = rs.getString("apellido");
                String email = rs.getString("email");

                persona = new Persona(personaId, nombres, apellidos, email);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return persona;
    }
  public Double insertar(PersonaDTO p) {
    String pStatement = "INSERT INTO personas (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
    Double id = IdGenerator.generateId();
    try (PreparedStatement ps = connection.prepareStatement(pStatement)) {
      ps.setDouble(1, id);
      ps.setString(2, p.getNombres());
      ps.setString(3, p.getApellidos());
      ps.setString(4, p.getEmail());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
    return id;
  }

  public List<Persona> listar() {
    List<Persona> personas = new ArrayList<>();
    String sql = "SELECT * FROM personas";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombre");
        String apellidos = rs.getString("apellido");
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
    String sql = "UPDATE personas SET nombre=?, apellido=?, email=? WHERE id=?";

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
