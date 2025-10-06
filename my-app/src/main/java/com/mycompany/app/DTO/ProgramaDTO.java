package com.mycompany.app.DTO;

import java.util.Date;

public class ProgramaDTO {
  private Double ID;
  private String nombre;
  private Double duracion;
  private Date registro;
  private FacultadDTO facultadDTO;

  public ProgramaDTO(Double ID, String nombre, Double duracion, Date registro, FacultadDTO facultadDTO) {
    this.ID = ID;
    this.nombre = nombre;
    this.duracion = duracion;
    this.registro = registro;
    this.facultadDTO = facultadDTO;
  }

  public Double getID() {
    return ID;
  }

  public String getNombre() {
    return nombre;
  }

  public Double getDuracion() {
    return duracion;
  }

  public Date getRegistro() {
    return registro;
  }

  public FacultadDTO getFacultadDTO() {
    return facultadDTO;
  }

  @Override
  public String toString() {
    return "Programa: " + nombre + " (ID: " + ID + ")";
  }
}
