package com.mycompany.app.DTO;

public class FacultadDTO {
  private Double ID;
  private String nombre;
  private PersonaDTO decanoDTO;

  // costructor
  public FacultadDTO(Double ID, String nombre, PersonaDTO decanoDTO) {
    this.ID = ID;
    this.nombre = nombre;
    this.decanoDTO = decanoDTO;
  }

  public Double getID() {
    return ID;
  }

  public String getNombre() {
    return nombre;
  }

  public PersonaDTO getDecanoDTO() {
    return decanoDTO;
  }

  @Override
  public String toString() {
    return "Facultad: " + nombre + " (ID: " + ID + ")";
  }
}
