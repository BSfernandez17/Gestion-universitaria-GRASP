package com.mycompany.app.Model;

public class Estudiante extends Persona {
  private Double codigo;
  private Programa programa;
  private Boolean activo;
  private Double promedio;

  public Estudiante(Double id, String nombres, String apellidos, String email,
      Double codigo, Programa programa, Boolean activo, Double promedio) {
    super(id, nombres, apellidos, email);
    this.codigo = codigo;
    this.programa = programa;
    this.activo = activo;
    this.promedio = promedio;
  }

  @Override
  public String toString() {
    return codigo + " - " + getNombres() + " " + getApellidos();
  }

  // Getters and Setters
  public Double getCodigo() {
    return codigo;
  }

  public Programa getPrograma() {
    return programa;
  }

  public Boolean getActivo() {
    return activo;
  }

  public Double getPromedio() {
    return promedio;
  }

}
