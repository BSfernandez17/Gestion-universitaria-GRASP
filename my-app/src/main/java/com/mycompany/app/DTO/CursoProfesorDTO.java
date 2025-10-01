package com.mycompany.app.DTO;

public class CursoProfesorDTO {
  private Double ID;
  private CursoDTO curso;
  private ProfesorDTO profesor;
  private Integer año;
  private Integer semestre;

  public CursoProfesorDTO(Double ID, ProfesorDTO profesor, Integer año, Integer semestre, CursoDTO curso) {
    this.ID = ID;
    this.curso = curso;
    this.profesor = profesor;
    this.año = año;
    this.semestre = semestre;
  }

  public Double getID() {
    return ID;
  }

  public CursoDTO getCursoDTO() {
    return curso;
  }

  public ProfesorDTO getProfesorDTO() {
    return profesor;
  }

  public Integer getAño() {
    return año;
  }

  public Integer getSemestre() {
    return semestre;
  }
  @Override
public String toString() {
    return curso.getNombre() + " - " + profesor.getNombres() + " " + profesor.getApellidos() +
          " (" + profesor.getEmail() + ") Año: " + año + " Semestre: " + semestre;
}

}
