package com.mycompany.app.DTO;

public class InscripcionDTO {
  private Double ID;
  private EstudianteDTO estudiante;
  private CursoDTO curso;
  private Integer año;

  public InscripcionDTO(Double ID, EstudianteDTO estudiante, CursoDTO curso, Integer año) {
    this.ID = ID;
    this.estudiante = estudiante;
    this.curso = curso;
    this.año = año;
  }

  public Double getID() {
    return ID;
  }

  public EstudianteDTO getEstudiante() {
    return estudiante;
  }

  public CursoDTO getCurso() {
    return curso;
  }

  public Integer getAño() {
    return año;
  }

}
