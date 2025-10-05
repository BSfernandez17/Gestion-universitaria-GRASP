package com.mycompany.app.DTO;

public class PersonaDTO {
<<<<<<< HEAD
  private Double ID;
=======
  private Double id;
>>>>>>> feature/oracle-integration
  private String nombres;
  private String apellidos;
  private String email;

  public PersonaDTO(Double id, String nombres, String apellidos, String email) {
    this.id = id;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.email = email;
  }

  public String toString() {
    return id + " " + nombres + " " + apellidos + " (" + email + ")";
  }

  public Double getID() {
    return id;
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

  public void setNombres(String nombres) {
    this.nombres = nombres;
  }
}
