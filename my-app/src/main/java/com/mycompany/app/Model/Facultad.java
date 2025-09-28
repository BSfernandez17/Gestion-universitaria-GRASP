package com.mycompany.app.Model;

public class Facultad {
  private Double id;
  private String nombre;
  private Persona decano;
  private String telefono;
  private String email;

  public Facultad() {
    // Constructor vacío
  }

  public Facultad(Double id, String nombre, Persona decano) {
    this.id = id;
    this.nombre = nombre;
    this.decano = decano;
  }

  public Facultad(Double id, String nombre, Persona decano, String telefono, String email) {
    this.id = id;
    this.nombre = nombre;
    this.decano = decano;
    this.telefono = telefono;
    this.email = email;
  }

  @Override
  public String toString() {
    return "Facultad{id=" + id + ", nombre='" + nombre + "', decano=" + decano + "}";
  }

  // Getters and Setters
  public Double getId() {
    return id;
  }

  public void setId(Double id) {
    this.id = id;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public Persona getDecano() {
    return decano;
  }

  public void setDecano(Persona decano) {
    this.decano = decano;
  }

  public String getTelefono() {
    return telefono;
  }

  public void setTelefono(String telefono) {
    this.telefono = telefono;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
