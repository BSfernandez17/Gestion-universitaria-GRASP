package com.mycompany.app.DTO;

public class CursoDTO {
  private Integer ID;
  private String nombre;
  private ProgramaDTO programa;
  private Boolean activo;

  public CursoDTO(Integer ID, String nombre, ProgramaDTO programa, Boolean activo) {
    this.ID = ID;
    this.nombre = nombre;

    this.programa = programa;
    this.activo = activo;
  }

  public Integer getID() {
    return ID;
  }

  public String getNombre() {
    return nombre;
  }

  public ProgramaDTO getProgramaDTO() {
    return programa;
  }

  public Boolean getActivo() {
    return activo;
  }
}
