package com.mycompany.app.Persistence.adapters;

import java.sql.Connection;
import java.sql.Statement;

public class DatabaseConfig {
  public static void init(Connection connection) {
    try (Statement stmt = connection.createStatement()) {

      stmt.execute(
          "CREATE TABLE IF NOT EXISTS personas (" +
              "id DOUBLE PRIMARY KEY," +
              "nombres VARCHAR(255)," +
              "apellidos VARCHAR(255)," +
              "email VARCHAR(255)" +
              ")");

      // Tabla Facultad
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS facultades (" +
              "id DOUBLE PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "decano_id DOUBLE," +
              "telefono VARCHAR(255)," +
              "email VARCHAR(255)," +
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
              "nivel VARCHAR(255)," +
              "contrato VARCHAR(255)," +
              "activo BOOLEAN," +
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
              "id INTEGER PRIMARY KEY," +
              "nombre VARCHAR(255)," +
              "programa_id DOUBLE," +
              "activo BOOLEAN," +
              "FOREIGN KEY (programa_id) REFERENCES programas(id)" +
              ")");

      // Tabla CursoProfesor (relación many-to-many)
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS curso_profesor (" +
              "curso_id INTEGER," +
              "profesor_id DOUBLE," +
              "año INTEGER," +
              "semestre INTEGER," +
              "PRIMARY KEY (profesor_id, curso_id, año, semestre)," +
              "FOREIGN KEY (profesor_id) REFERENCES profesores(id)," +
              "FOREIGN KEY (curso_id) REFERENCES cursos(id)" +
              ")");

      // Tabla Inscripcion (relación many-to-many)
      stmt.execute(
          "CREATE TABLE IF NOT EXISTS inscripciones (" +
              "id INTEGER PRIMARY KEY AUTO_INCREMENT," +
              "curso_id INTEGER," +
              "estudiante_id DOUBLE," +
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
