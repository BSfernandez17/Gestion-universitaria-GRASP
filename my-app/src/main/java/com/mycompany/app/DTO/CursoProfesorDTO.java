package com.mycompany.app.DTO;

public class CursoProfesorDTO {
  private Double ID;
  private CursoDTO curso;
  private ProfesorDTO profesor;
  private Integer año;
  private Integer semestre;

  public CursoProfesorDTO(Double ID, CursoDTO curso, ProfesorDTO profesor, Integer año, Integer semestre) {
    this.ID = ID;
    this.curso = curso;
    this.profesor = profesor;
    this.año = año;
    this.semestre = semestre;
  }

  public Double getID() {
    return ID;
  }

  public CursoDTO getCurso() {
    return curso;
  }

  public ProfesorDTO getProfesor() {
    return profesor;
  }

  public Integer getAño() {
    return año;
  }

  public Integer getSemestre() {
    return semestre;
  }
}
