package com.mycompany.app.Model;

public class Curso {
  private Double id;
  private String nombre;
  private Programa programa;
  private Boolean activo;

  public Curso(Double id, String nombre, Programa programa, Boolean activo) {
    this.id = id;
    this.nombre = nombre;
    this.programa = programa;
    this.activo = activo;
  }

  @Override
  public String toString() {
    return id + " - " + nombre;
  }

  // Getters and Setters
  public Double getID() {
    return id;
  }

  public String getNombre() {
    return nombre;
  }

  public Programa getPrograma() {
    return programa;
  }

  public Boolean getActivo() {
    return activo;
  }

}
