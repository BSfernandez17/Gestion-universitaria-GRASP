package com.mycompany.app.DTO;

public class PersonaDTO {
  private Double ID;
  private String nombres;
  private String apellidos;
  private String email;

  public PersonaDTO(Double ID, String nombres, String apellidos, String email) {
    this.ID = ID;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.email = email;
  }

public String toString() {
    return ID + " - " + nombres + " " + apellidos + " (" + email + ")";
  }

  public Double getID() {
    return ID;
  }

  public String getNombres() {
    return nombres;
  }

  public String getApellidos() {
    return apellidos;
  }

  public String getEmail() {
    return email;
  }
}
