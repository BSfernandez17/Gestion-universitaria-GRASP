package com.mycompany.app.Persistence.adapters;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseConfig {
  public static void init(Connection connection) {
    try (Statement stmt = connection.createStatement()) {
      // For embedded/in-memory databases (H2) we cannot drop/create the database.
      // Instead drop tables if they exist (child tables first to avoid FK errors),
      // then recreate them. This works across H2/MySQL.
      stmt.execute("DROP TABLE IF EXISTS inscripciones");
      stmt.execute("DROP TABLE IF EXISTS curso_profesor");
      stmt.execute("DROP TABLE IF EXISTS cursos");
      stmt.execute("DROP TABLE IF EXISTS estudiantes");
      stmt.execute("DROP TABLE IF EXISTS profesores");
      stmt.execute("DROP TABLE IF EXISTS programas");
      stmt.execute("DROP TABLE IF EXISTS facultades");
      stmt.execute("DROP TABLE IF EXISTS personas");
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS personas (" +
              "id DOUBLE PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "apellido VARCHAR(255)," +
              "email VARCHAR(255)" +
              ")");

      // Tabla Facultad
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS facultades (" +
              "id DOUBLE PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "decano_id DOUBLE," +
              "FOREIGN KEY (decano_id) REFERENCES personas(id)" +
              ")");

      // Tabla Programa
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS programas (" +
              "id DOUBLE PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "duracion DOUBLE," +
              "registro DATE," +
              "facultad_id DOUBLE," +
              "FOREIGN KEY (facultad_id) REFERENCES facultades(id)" +
              ")");

      // Tabla Profesor
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS profesores (" +
              "id DOUBLE PRIMARY KEY," +
              "persona_id DOUBLE," +
              "tipoContrato VARCHAR(255)," +
              "FOREIGN KEY (persona_id) REFERENCES personas(id)" +
              ")");

      // Tabla Estudiante (actualizada con referencia a Programa)
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS estudiantes (" +
              "id DOUBLE PRIMARY KEY," +
              "persona_id DOUBLE," +
              "codigo DOUBLE," +
              "programa_id DOUBLE," +
              "activo BOOLEAN," +
              "promedio DOUBLE," +
              "FOREIGN KEY (persona_id) REFERENCES personas(id)," +
              "FOREIGN KEY (programa_id) REFERENCES programas(id)" +
              ")");

      // Tabla Curso
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS cursos (" +
              "id DOUBLE PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "programa_id DOUBLE," +
              "activo BOOLEAN," +
              "FOREIGN KEY (programa_id) REFERENCES programas(id)" +
              ")");

      // Tabla CursoProfesor (relación many-to-many)
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS curso_profesor (" +
              "id DOUBLE PRIMARY KEY," +
              "curso_id DOUBLE," +
              "profesor_id DOUBLE," +
              "año INTEGER," +
              "semestre INTEGER," +
              "FOREIGN KEY (profesor_id) REFERENCES profesores(id)," +
              "FOREIGN KEY (curso_id) REFERENCES cursos(id)" +
              ")");

      // Tabla Inscripcion (relación many-to-many)
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS inscripciones (" +
              "id DOUBLE PRIMARY KEY," + // <-- ahora es DOUBLE
              "curso_id DOUBLE," + // <-- debe ser DOUBLE
              "estudiante_id DOUBLE," + // <-- debe ser DOUBLE
              "año INTEGER," +
              "semestre INTEGER," +
              "UNIQUE(curso_id, estudiante_id, año, semestre)," +
              "FOREIGN KEY (curso_id) REFERENCES cursos(id)," +
              "FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id)" +
              ")");
      System.out.println("Todas las tablas han sido creadas correctamente.");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
