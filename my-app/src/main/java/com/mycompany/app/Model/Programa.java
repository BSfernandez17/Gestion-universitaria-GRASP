package com.mycompany.app.Model;

import java.util.Date;

public class Programa {
  private Double id;
  private String nombre;
  private Double duracion;
  private Date registro;
  private Facultad facultad;

  public Programa(Double id, String nombre, Double duracion, Date registro, Facultad facultad) {
    this.id = id;
    this.nombre = nombre;
    this.duracion = duracion;
    this.registro = registro;
    this.facultad = facultad;
  }

  @Override
  public String toString() {
    return "Programa{id=" + id + ", nombre='" + nombre + "', duracion=" + duracion + ", registro=" + registro + "}";
  }

  // Getters
  public Double getId() {
    return id;
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

  public Facultad getFacultad() {
    return facultad;
  }

}
