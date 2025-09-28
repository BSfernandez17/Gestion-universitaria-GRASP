package com.mycompany.app.Model;

public class Profesor extends Persona {
  private String tipoContrato;

  public Profesor(Double id, String nombres, String apellidos, String email, String tipoContrato) {
    super(id, nombres, apellidos, email);
    this.tipoContrato = tipoContrato;
  }

  @Override
  public String toString() {
    return getID() + " - " + getNombres() + " " + getApellidos() + " (" + tipoContrato + ")";
  }

  // Getter and Setter
  public String getTipoContrato() {
    return tipoContrato;
  }

  public void setTipoContrato(String tipoContrato) {
    this.tipoContrato = tipoContrato;
  }
}
