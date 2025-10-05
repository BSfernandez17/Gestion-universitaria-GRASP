package com.mycompany.app.Persistence.DAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import com.mycompany.app.DTO.ProfesorDTO;
import com.mycompany.app.Model.Persona;
import com.mycompany.app.Model.Profesor;

public class ProfesorDAO {

  private Connection connection;

  public ProfesorDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(ProfesorDTO p) {
    PersonaDAO personaDAO=new PersonaDAO(connection);
    String pStatement = "INSERT INTO profesores (id,persona_id,tipoContrato) VALUES (?, ?, ?)";
    try (PreparedStatement ps = connection.prepareStatement(pStatement)) {
      ps.setDouble(1, generateID());
      ps.setDouble(2,personaDAO.buscarPorEmail(p.getEmail()).getID());
      ps.setString(3, p.getTipoContrato());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }
  private Integer generateID(){
    Integer counter=listar().size();
    return counter++;
  }
public Profesor buscarPorId(Double id) {
    Profesor profesor = null;
   String sql = "SELECT p.id, p.nombre, p.apellido, p.email, pr.tipoContrato " +
             "FROM profesores pr JOIN personas p ON pr.persona_id = p.id WHERE pr.id = ?";

    try (PreparedStatement ps = connection.prepareStatement(sql)) {
        ps.setDouble(1, id);
        ResultSet rs = ps.executeQuery();

        if (rs.next()) {
            profesor = new Profesor(
                rs.getDouble("id"),
                rs.getString("nombre"),
                rs.getString("apellido"),
                rs.getString("email"),
                rs.getString("tipoContrato")
            );
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return profesor;
}
public Profesor buscarPorEmail(String email){
  Profesor profesor = null;
  String sql = "SELECT pr.id AS profesor_id, p.nombre, p.apellido, p.email, pr.tipoContrato " +
         "FROM profesores pr JOIN personas p ON pr.persona_id = p.id WHERE p.email = ?";
  try (PreparedStatement ps = connection.prepareStatement(sql)) {
    // You need to pass the email as a parameter, so this method should accept String email
    // For now, let's assume you add String email as a parameter to this method
    ps.setString(1, email);
    ResultSet rs = ps.executeQuery();
     if (rs.next()) {
         profesor = new Profesor(
             rs.getDouble("profesor_id"), // ID de la tabla profesores
             rs.getString("nombre"),
             rs.getString("apellido"),
             rs.getString("email"),
             rs.getString("tipoContrato")
        );
     }
  } catch (SQLException e) {
    e.printStackTrace();
  }
  return profesor;
}

  public List<Profesor> listar() {
    List<Profesor> profesores = new ArrayList<>();
    String sql = "SELECT " + 
            "    p.id AS persona_id," +
            "    p.nombre," + 
            "    p.apellido," + 
            "    p.email," + 
            "    pr.id AS profesor_id," +
            "    pr.tipoContrato " + 
            "FROM " + 
            "    profesores pr " + 
            "JOIN " + 
            "    personas p ON pr.persona_id = p.id;";
    try (PreparedStatement ps = connection.prepareStatement(sql);
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("persona_id");
        String nombres = rs.getString("nombre");
        String apellidos = rs.getString("apellido");
        String email = rs.getString("email");
   
        String TipoContrato=rs.getString("tipoContrato");
        Profesor p = new Profesor(id, nombres, apellidos, email, TipoContrato);
        profesores.add(p);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return profesores;
  }

  public void actualizar(ProfesorDTO p) {
    String sql = "UPDATE profesores SET nombres=?, apellidos=?, email=?, especialidad=? WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setString(1, p.getNombres());
      ps.setString(2, p.getApellidos());
      ps.setString(3, p.getEmail());
      ps.setString(4, p.getTipoContrato());
      ps.setDouble(5, p.getID());
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM profesores WHERE id=?";
    try (
        PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
