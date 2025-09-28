package com.mycompany.app.Model;

public class CursoProfesor {
  private Double ID;
  private Profesor profesor;
  private Integer año;
  private Integer semestre;
  private Curso curso;

  public CursoProfesor(Double ID, Profesor profesor, Integer año, Integer semestre, Curso curso) {
    this.profesor = profesor;
    this.año = año;
    this.semestre = semestre;
    this.curso = curso;
    this.ID = ID;
  }

  @Override
  public String toString() {
    return "CursoProfesor{profesor=" + getProfesor() + ", año=" + getAño() + ", semestre=" + getSemestre() + ", curso="
        + getCurso() + "}";
  }

  // Getters
  public Profesor getProfesor() {
    return profesor;
  }

  public Double getID() {
    return ID;
  }

  public Integer getAño() {
    return año;
  }

  public Integer getSemestre() {
    return semestre;
  }

  public Curso getCurso() {
    return curso;
  }
}
