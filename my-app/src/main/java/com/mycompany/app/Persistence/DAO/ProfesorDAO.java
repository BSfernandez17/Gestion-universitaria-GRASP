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
import com.mycompany.app.Persistence.DAO.IdGenerator;

public class ProfesorDAO {

  private Connection connection;

  public ProfesorDAO(Connection connection) {
    this.connection = connection;
  }

  public void insertar(ProfesorDTO p) {
    // Insert persona first, then profesor pointing to that persona.
    // Use a transaction and retry ID generation on collisions.
    String insPersona = "INSERT INTO personas (id, nombre, apellido, email) VALUES (?, ?, ?, ?)";
    String insProfesor = "INSERT INTO profesores (id, persona_id, tipoContrato) VALUES (?, ?, ?)";
    boolean oldAuto = true;
    try {
      oldAuto = connection.getAutoCommit();
      connection.setAutoCommit(false);

      double personaId = -1;
      // try up to 10 times to insert a persona with a unique generated id
      int attempts = 0;
      while (attempts < 10) {
        attempts++;
        personaId = IdGenerator.generateId();
        try (PreparedStatement psp = connection.prepareStatement(insPersona)) {
          psp.setDouble(1, personaId);
          psp.setString(2, p.getNombres());
          psp.setString(3, p.getApellidos());
          psp.setString(4, p.getEmail());
          psp.executeUpdate();
          break; // success
        } catch (SQLException ex) {
          // If constraint violation (id exists), try another id
          if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
            // continue to retry
            if (attempts >= 10) throw ex;
            continue;
          } else {
            throw ex;
          }
        }
      }

      // now insert profesor row with a unique id
      double profesorId = -1;
      attempts = 0;
      while (attempts < 10) {
        attempts++;
        profesorId = IdGenerator.generateId();
        try (PreparedStatement ppr = connection.prepareStatement(insProfesor)) {
          ppr.setDouble(1, profesorId);
          ppr.setDouble(2, personaId);
          ppr.setString(3, p.getTipoContrato());
          ppr.executeUpdate();
          break; // success
        } catch (SQLException ex) {
          if (ex.getMessage() != null && ex.getMessage().toLowerCase().contains("constraint")) {
            if (attempts >= 10) throw ex;
            continue;
          } else {
            throw ex;
          }
        }
      }

      connection.commit();
    } catch (SQLException e) {
      try {
        connection.rollback();
      } catch (SQLException r) {
        r.printStackTrace();
      }
      e.printStackTrace();
    } finally {
      try {
        connection.setAutoCommit(oldAuto);
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  /**
   * Insert a profesor row that points to an existing persona id.
   * Returns the new profesor id or null on failure.
   */
  public Double insertarForPersona(Double personaId, String tipoContrato) {
    String insProfesor = "INSERT INTO profesores (id, persona_id, tipoContrato) VALUES (?, ?, ?)";
    try {
      double profesorId = IdGenerator.generateId();
      try (PreparedStatement ppr = connection.prepareStatement(insProfesor)) {
        ppr.setDouble(1, profesorId);
        ppr.setDouble(2, personaId);
        ppr.setString(3, tipoContrato);
        ppr.executeUpdate();
      }
      return profesorId;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
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


  public List<Profesor> listar() {
    List<Profesor> profesores = new ArrayList<>();
    String sql = "SELECT pr.id as id, p.nombre as nombre, p.apellido as apellido, p.email as email, pr.tipoContrato as tipoContrato "
               + "FROM profesores pr JOIN personas p ON pr.persona_id = p.id";
    try (PreparedStatement ps = connection.prepareStatement(sql);
         ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        Double id = rs.getDouble("id");
        String nombres = rs.getString("nombre");
        String apellidos = rs.getString("apellido");
        String email = rs.getString("email");
        String tipo = rs.getString("tipoContrato");
        Profesor p = new Profesor(id, nombres, apellidos, email, tipo);
        profesores.add(p);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
    return profesores;
  }

  public void actualizar(ProfesorDTO p) {
    // Update persona fields (using persona_id referenced from profesores) and profesor contract
    String updPersona = "UPDATE personas SET nombre=?, apellido=?, email=? WHERE id = (SELECT persona_id FROM profesores WHERE id = ?)";
    String updProfesor = "UPDATE profesores SET tipoContrato=? WHERE id=?";
    try (PreparedStatement psp = connection.prepareStatement(updPersona);
         PreparedStatement ppr = connection.prepareStatement(updProfesor)) {
      psp.setString(1, p.getNombres());
      psp.setString(2, p.getApellidos());
      psp.setString(3, p.getEmail());
      psp.setDouble(4, p.getID());
      psp.executeUpdate();

      ppr.setString(1, p.getTipoContrato());
      ppr.setDouble(2, p.getID());
      ppr.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void eliminar(Double id) {
    String sql = "DELETE FROM profesores WHERE id=?";
    try (PreparedStatement ps = connection.prepareStatement(sql)) {
      ps.setDouble(1, id);
      ps.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}
