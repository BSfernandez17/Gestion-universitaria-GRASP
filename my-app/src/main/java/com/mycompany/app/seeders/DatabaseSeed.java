package com.mycompany.app.seeders;

import java.sql.Connection;
<<<<<<< HEAD
import java.sql.Statement;

public class DatabaseSeed {
  public static void seed(Connection connection) {
    try (Statement stmt = connection.createStatement()) {

      // Personas
      stmt.executeUpdate(
          "INSERT INTO personas (id, nombres, apellidos, email) VALUES " +
              "(1, 'Juan', 'Pérez', 'juan.perez@example.com')," +
              "(2, 'María', 'Gómez', 'maria.gomez@example.com')," +
              "(3, 'Carlos', 'Rodríguez', 'carlos.rodriguez@example.com')" +
              "ON DUPLICATE KEY UPDATE email = VALUES(email)");

      // Facultades
      stmt.executeUpdate(
          "INSERT INTO facultades (id, nombre, decano_id, telefono, email) VALUES " +
              "(1, 'Ingeniería', 1, '1234567', 'ing@example.com')," +
              "(2, 'Ciencias Sociales', 2, '7654321', 'cs@example.com')" +
              "ON DUPLICATE KEY UPDATE email = VALUES(email)");

      // Programas
      stmt.executeUpdate(
          "INSERT INTO programas (id, nombre, duracion, registro, facultad_id) VALUES " +
              "(1, 'Ingeniería de Sistemas', 10, '2020-01-01', 1)," +
              "(2, 'Psicología', 8, '2021-01-01', 2)" +
              "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre)");

      // Profesores
      stmt.executeUpdate(
          "INSERT INTO profesores (id, persona_id, nivel, contrato, activo) VALUES " +
              "(1, 1, 'Doctorado', 'Tiempo Completo', true)," +
              "(2, 2, 'Maestría', 'Cátedra', true)" +
              "ON DUPLICATE KEY UPDATE nivel = VALUES(nivel)");

      // Estudiantes
      stmt.executeUpdate(
          "INSERT INTO estudiantes (id, persona_id, codigo, programa_id, activo, promedio) VALUES " +
              "(1, 3, 2023001, 1, true, 4.0)" +
              "ON DUPLICATE KEY UPDATE promedio = VALUES(promedio)");

      // Cursos
      stmt.executeUpdate(
          "INSERT INTO cursos (id, nombre, programa_id, activo) VALUES " +
              "(101, 'Algoritmos', 1, true)," +
              "(102, 'Bases de Datos', 1, true)," +
              "(201, 'Introducción a la Psicología', 2, true)" +
              "ON DUPLICATE KEY UPDATE nombre = VALUES(nombre)");

      // Curso-Profesor
      stmt.executeUpdate(
          "INSERT INTO curso_profesor (curso_id, profesor_id, año, semestre) VALUES " +
              "(101, 1, 2023, 1)," +
              "(102, 1, 2023, 2)," +
              "(201, 2, 2023, 1)" +
              "ON DUPLICATE KEY UPDATE semestre = VALUES(semestre)");

      // Inscripciones
      stmt.executeUpdate(
          "INSERT INTO inscripciones (curso_id, estudiante_id, año, semestre) VALUES " +
              "(101, 1, 2023, 1)," +
              "(102, 1, 2023, 2)" +
              "ON DUPLICATE KEY UPDATE semestre = VALUES(semestre)");

      System.out.println("✅ Datos de prueba insertados correctamente.");
=======
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
          "INSERT INTO facultades (id, nombre, decano_id) VALUES (?, ?, ?)");
      ps.setDouble(1, 2001);
      ps.setString(2, "Facultad de Ciencias");
      ps.setDouble(3, 1001); // decano
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

>>>>>>> feature/oracle-integration
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
