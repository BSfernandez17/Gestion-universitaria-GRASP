package com.mycompany.app.seeders;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.Date;

public class DatabaseSeed {

  public static void seed(Connection conn) {
    try {
      // Limpieza previa
      String[] tablas = { "inscripciones", "curso_profesor", "cursos", "estudiantes",
          "profesores", "programas", "facultades", "personas" };
      for (String tabla : tablas) {
        conn.createStatement().execute("DELETE FROM " + tabla);
      }

      // --- PERSONAS ---
      PreparedStatement ps = conn.prepareStatement(
          "INSERT INTO personas (id, nombre, apellido, email) VALUES (?, ?, ?, ?)");
      Object[][] personas = {
          { 1001.0, "Juan", "Perez", "juan@example.com" },
          { 1002.0, "Maria", "Lopez", "maria@example.com" },
          { 1003.0, "Carlos", "Gomez", "carlos@example.com" }
      };
      for (Object[] p : personas) {
        ps.setDouble(1, (Double) p[0]);
        ps.setString(2, (String) p[1]);
        ps.setString(3, (String) p[2]);
        ps.setString(4, (String) p[3]);
        ps.executeUpdate();
      }

      // --- FACULTADES ---
      ps = conn.prepareStatement(
          "INSERT INTO facultades (id, nombre, decano_id, telefono, email) VALUES (?, ?, ?, ?, ?)");
      ps.setDouble(1, 2001);
      ps.setString(2, "Facultad de Ciencias");
      ps.setDouble(3, 1001); // decano
      ps.setString(4, "1234567");
      ps.setString(5, "ciencias@uni.com");
      ps.executeUpdate();

      // --- PROGRAMAS ---
      ps = conn.prepareStatement(
          "INSERT INTO programas (id, nombre, duracion, registro, facultad_id) VALUES (?, ?, ?, ?, ?)");
      Object[][] programas = {
          { 3001.0, "Ingeniería", 5.0, "2025-01-01", 2001.0 },
          { 3002.0, "Matemáticas", 4.0, "2025-01-01", 2001.0 }
      };
      for (Object[] pr : programas) {
        ps.setDouble(1, (Double) pr[0]);
        ps.setString(2, (String) pr[1]);
        ps.setDouble(3, (Double) pr[2]);
        ps.setDate(4, Date.valueOf((String) pr[3]));
        ps.setDouble(5, (Double) pr[4]);
        ps.executeUpdate();
      }

      // --- PROFESORES ---
      ps = conn.prepareStatement(
          "INSERT INTO profesores (id, persona_id, tipoContrato) VALUES (?, ?, ?)");
      Object[][] profesores = {
          { 4001.0, 1001.0, "Tiempo Completo" },
          { 4002.0, 1002.0, "Medio Tiempo" }
      };
      ps = conn.prepareStatement(
          "INSERT INTO profesores (id, persona_id, tipoContrato) VALUES (?, ?, ?)");
      for (Object[] prf : profesores) {
        ps.setDouble(1, (Double) prf[0]);
        ps.setDouble(2, (Double) prf[1]);
        ps.setString(3, (String) prf[2]);
        ps.executeUpdate();
      }

      // --- ESTUDIANTES ---
      ps = conn.prepareStatement(
          "INSERT INTO estudiantes (id, persona_id, codigo, programa_id, activo, promedio) VALUES (?, ?, ?, ?, ?, ?)");
      Object[][] estudiantes = {
          { 5001.0, 1003.0, 2025001.0, 3001.0, true, 4.5 }
      };
      for (Object[] est : estudiantes) {
        ps.setDouble(1, (Double) est[0]);
        ps.setDouble(2, (Double) est[1]);
        ps.setDouble(3, (Double) est[2]);
        ps.setDouble(4, (Double) est[3]);
        ps.setBoolean(5, (Boolean) est[4]);
        ps.setDouble(6, (Double) est[5]);
        ps.executeUpdate();
      }

      // --- CURSOS ---
      ps = conn.prepareStatement(
          "INSERT INTO cursos (id, nombre, programa_id, activo) VALUES (?, ?, ?, ?)");
      Object[][] cursos = {
          { 6001.0, "Cálculo I", 3001.0, true },
          { 6002.0, "Física I", 3001.0, true }
      };
      for (Object[] cur : cursos) {
        ps.setDouble(1, (Double) cur[0]);
        ps.setString(2, (String) cur[1]);
        ps.setDouble(3, (Double) cur[2]);
        ps.setBoolean(4, (Boolean) cur[3]);
        ps.executeUpdate();
      }

      // --- CURSO_PROFESOR ---
      ps = conn.prepareStatement(
          "INSERT INTO curso_profesor (id, curso_id, profesor_id, año, semestre) VALUES (?, ?, ?, ?, ?)");
      Object[][] cursoProfesor = {
          { 7001.0, 6001.0, 4001.0, 2025, 1 },
          { 7002.0, 6002.0, 4002.0, 2025, 1 }
      };
      for (Object[] cp : cursoProfesor) {
        ps.setDouble(1, (Double) cp[0]);
        ps.setDouble(2, (Double) cp[1]);
        ps.setDouble(3, (Double) cp[2]);
        ps.setInt(4, (Integer) cp[3]);
        ps.setInt(5, (Integer) cp[4]);
        ps.executeUpdate();
      }

      // --- INSCRIPCIONES ---
      ps = conn.prepareStatement(
          "INSERT INTO inscripciones (id, curso_id, estudiante_id, año, semestre) VALUES (?, ?, ?, ?, ?)");
      Object[][] inscripciones = {
          { 8001.0, 6001.0, 5001.0, 2025, 1 }
      };
      for (Object[] ins : inscripciones) {
        ps.setDouble(1, (Double) ins[0]);
        ps.setDouble(2, (Double) ins[1]);
        ps.setDouble(3, (Double) ins[2]);
        ps.setInt(4, (Integer) ins[3]);
        ps.setInt(5, (Integer) ins[4]);
        ps.executeUpdate();
      }

      System.out.println("Datos de prueba insertados correctamente.");

    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
