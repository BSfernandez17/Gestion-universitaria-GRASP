package com.mycompany.app.Model;

public class Inscripcion {
  private Curso curso;
  private Integer año;
  private Integer semestre;
  private Estudiante estudiante;

  public Inscripcion(Curso curso, Integer año, Integer semestre, Estudiante estudiante) {
    this.curso = curso;
    this.año = año;
    this.semestre = semestre;
    this.estudiante = estudiante;
  }

  @Override
  public String toString() {
    return "Inscripcion{curso=" + getCurso() + ", año=" + getAño() + ", semestre=" + getSemestre() + ", estudiante="
        + getEstudiante() + "}";
  }

  // Getters and Setters
  public Curso getCurso() {
    return curso;
  }

  public Integer getAño() {
    return año;
  }

  public Integer getSemestre() {
    return semestre;
  }

  public Estudiante getEstudiante() {
    return estudiante;
  }

}
