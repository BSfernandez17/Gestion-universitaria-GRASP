package com.mycompany.app.seeders;

import java.sql.Connection;
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
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
