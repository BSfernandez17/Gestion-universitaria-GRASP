package com.mycompany.app.Model;

public class Persona {
  private static long nexId = 1;
  private double id;
  private String nombres;
  private String apellidos;
  private String email;

  public Persona(double id, String nombres, String apellidos, String email) {
    this.id = id;
    this.nombres = nombres;
    this.apellidos = apellidos;
    this.email = email;
  }

  @Override
  public String toString() {
    return "Persona{id=" + id + ", nombres='" + nombres + "', apellidos='" + apellidos + "', email='" + email + "'}";
  }

  public double getID() {
    return id;
  }

  // JavaBean-style accessor expected by PropertyValueFactory for property 'id'
  public double getId() {
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

}
