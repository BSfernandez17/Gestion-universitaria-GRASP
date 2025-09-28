package com.mycompany.app.Model;

public class Facultad {
  private Double id;
  private String nombre;
  private Persona decano;
  private String email;

  public Facultad() {
    // Constructor vacío
  }

  public Facultad(Double id, String nombre, Persona decano) {
    this.id = id;
    this.nombre = nombre;
    this.decano = decano;
  }

  @Override
  public String toString() {
    return "Facultad{id=" + id + ", nombre='" + nombre + "', decano=" + decano + "}";
  }

  // Getters and Setters
  public Double getID() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public Persona getDecano() {
    return decano;
  }

  public String getEmail() {
    return email;
  }

}
