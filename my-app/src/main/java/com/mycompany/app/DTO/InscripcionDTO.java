package com.mycompany.app.DTO;

public class InscripcionDTO {
  private Double ID;
  private EstudianteDTO estudiante;
  private CursoDTO curso;
  private Integer año;
  private Integer semestre;

  public InscripcionDTO(Double ID, CursoDTO curso, Integer año, Integer semestre, EstudianteDTO estudiante) {
    this.ID = ID;
    this.estudiante = estudiante;
    this.curso = curso;
    this.año = año;
    this.semestre = semestre;
  }

  public Double getID() {
    return ID;
  }

  public EstudianteDTO getEstudianteDTO() {
    return estudiante;
  }

  public CursoDTO getCursoDTO() {
    return curso;
  }

  public Integer getAño() {
    return año;
  }

  public Integer getSemestre() {
    return semestre;
  }

  @Override
  public String toString() {
    return "Inscripcion (ID: " + ID + ") - Estudiante: " + estudiante.getNombres() + " " + estudiante.getApellidos()
        + " | Curso: " + curso.getNombre() + " | Periodo: " + año + "-" + semestre;
  }
}
