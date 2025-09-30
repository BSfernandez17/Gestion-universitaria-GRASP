package com.mycompany.app.seeders;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseOracleSeed {
  public static void seed(Connection connection) {
    try (Statement stmt = connection.createStatement()) {

      // Personas
      stmt.executeUpdate(
          "MERGE INTO personas p " +
              "USING (SELECT 1 AS id, 'Juan' AS nombres, 'Pérez' AS apellidos, 'juan.perez@example.com' AS email FROM dual) d "
              +
              "ON (p.email = d.email) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombres, apellidos, email) VALUES (d.nombres, d.apellidos, d.email)");

      stmt.executeUpdate(
          "MERGE INTO personas p " +
              "USING (SELECT 2 AS id, 'María' AS nombres, 'Gómez' AS apellidos, 'maria.gomez@example.com' AS email FROM dual) d "
              +
              "ON (p.email = d.email) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombres, apellidos, email) VALUES (d.nombres, d.apellidos, d.email)");

      stmt.executeUpdate(
          "MERGE INTO personas p " +
              "USING (SELECT 3 AS id, 'Carlos' AS nombres, 'Rodríguez' AS apellidos, 'carlos.rodriguez@example.com' AS email FROM dual) d "
              +
              "ON (p.email = d.email) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombres, apellidos, email) VALUES (d.nombres, d.apellidos, d.email)");

      // Facultades
      stmt.executeUpdate(
          "MERGE INTO facultades f " +
              "USING (SELECT 1 AS id, 'Ingeniería' AS nombre, 1 AS decano_id, '1234567' AS telefono, 'ing@example.com' AS email FROM dual) d "
              +
              "ON (f.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, decano_id, telefono, email) VALUES (d.nombre, d.decano_id, d.telefono, d.email)");

      stmt.executeUpdate(
          "MERGE INTO facultades f " +
              "USING (SELECT 2 AS id, 'Ciencias Sociales' AS nombre, 2 AS decano_id, '7654321' AS telefono, 'cs@example.com' AS email FROM dual) d "
              +
              "ON (f.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, decano_id, telefono, email) VALUES (d.nombre, d.decano_id, d.telefono, d.email)");

      // Programas
      stmt.executeUpdate(
          "MERGE INTO programas pr " +
              "USING (SELECT 1 AS id, 'Ingeniería de Sistemas' AS nombre, 10 AS duracion, DATE '2020-01-01' AS registro, 1 AS facultad_id FROM dual) d "
              +
              "ON (pr.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, duracion, registro, facultad_id) VALUES (d.nombre, d.duracion, d.registro, d.facultad_id)");

      stmt.executeUpdate(
          "MERGE INTO programas pr " +
              "USING (SELECT 2 AS id, 'Psicología' AS nombre, 8 AS duracion, DATE '2021-01-01' AS registro, 2 AS facultad_id FROM dual) d "
              +
              "ON (pr.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, duracion, registro, facultad_id) VALUES (d.nombre, d.duracion, d.registro, d.facultad_id)");

      // Profesores
      stmt.executeUpdate(
          "MERGE INTO profesores pr " +
              "USING (SELECT 1 AS id, 1 AS persona_id, 'Doctorado' AS nivel, 'Tiempo Completo' AS contrato, 'Y' AS activo FROM dual) d "
              +
              "ON (pr.persona_id = d.persona_id) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (persona_id, nivel, contrato, activo) VALUES (d.persona_id, d.nivel, d.contrato, d.activo)");

      stmt.executeUpdate(
          "MERGE INTO profesores pr " +
              "USING (SELECT 2 AS id, 2 AS persona_id, 'Maestría' AS nivel, 'Cátedra' AS contrato, 'Y' AS activo FROM dual) d "
              +
              "ON (pr.persona_id = d.persona_id) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (persona_id, nivel, contrato, activo) VALUES (d.persona_id, d.nivel, d.contrato, d.activo)");

      // Estudiantes
      stmt.executeUpdate(
          "MERGE INTO estudiantes e " +
              "USING (SELECT 1 AS id, 3 AS persona_id, 2023001 AS codigo, 1 AS programa_id, 'Y' AS activo, 4.0 AS promedio FROM dual) d "
              +
              "ON (e.codigo = d.codigo) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (persona_id, codigo, programa_id, activo, promedio) VALUES (d.persona_id, d.codigo, d.programa_id, d.activo, d.promedio)");

      // Cursos
      stmt.executeUpdate(
          "MERGE INTO cursos c " +
              "USING (SELECT 101 AS id, 'Algoritmos' AS nombre, 1 AS programa_id, 'Y' AS activo FROM dual) d " +
              "ON (c.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, programa_id, activo) VALUES (d.nombre, d.programa_id, d.activo)");

      stmt.executeUpdate(
          "MERGE INTO cursos c " +
              "USING (SELECT 102 AS id, 'Bases de Datos' AS nombre, 1 AS programa_id, 'Y' AS activo FROM dual) d " +
              "ON (c.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, programa_id, activo) VALUES (d.nombre, d.programa_id, d.activo)");

      stmt.executeUpdate(
          "MERGE INTO cursos c " +
              "USING (SELECT 201 AS id, 'Introducción a la Psicología' AS nombre, 2 AS programa_id, 'Y' AS activo FROM dual) d "
              +
              "ON (c.nombre = d.nombre) " +
              "WHEN NOT MATCHED THEN " +
              "INSERT (nombre, programa_id, activo) VALUES (d.nombre, d.programa_id, d.activo)");

      // Curso-Profesor
      stmt.executeUpdate(
          "INSERT /*+ ignore_row_on_dupkey_index(curso_profesor, (profesor_id, curso_id, anio, semestre)) */ " +
              "INTO curso_profesor (curso_id, profesor_id, anio, semestre) VALUES (101, 1, 2023, 1)");

      stmt.executeUpdate(
          "INSERT /*+ ignore_row_on_dupkey_index(curso_profesor, (profesor_id, curso_id, anio, semestre)) */ " +
              "INTO curso_profesor (curso_id, profesor_id, anio, semestre) VALUES (102, 1, 2023, 2)");

      stmt.executeUpdate(
          "INSERT /*+ ignore_row_on_dupkey_index(curso_profesor, (profesor_id, curso_id, anio, semestre)) */ " +
              "INTO curso_profesor (curso_id, profesor_id, anio, semestre) VALUES (201, 2, 2023, 1)");

      // Inscripciones
      stmt.executeUpdate(
          "INSERT /*+ ignore_row_on_dupkey_index(inscripciones, (curso_id, estudiante_id, anio, semestre)) */ " +
              "INTO inscripciones (curso_id, estudiante_id, anio, semestre) VALUES (101, 1, 2023, 1)");

      stmt.executeUpdate(
          "INSERT /*+ ignore_row_on_dupkey_index(inscripciones, (curso_id, estudiante_id, anio, semestre)) */ " +
              "INTO inscripciones (curso_id, estudiante_id, anio, semestre) VALUES (102, 1, 2023, 2)");

      System.out.println("✅ Datos de prueba insertados en Oracle.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
