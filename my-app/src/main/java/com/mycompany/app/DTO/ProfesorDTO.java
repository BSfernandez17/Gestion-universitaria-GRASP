package com.mycompany.app.DTO;

public class ProfesorDTO extends PersonaDTO {
  private String TipoContrato;

  public ProfesorDTO(Double ID, String nombres, String apellidos, String email, String TipoContrato) {
    super(ID, nombres, apellidos, email);
    this.TipoContrato = TipoContrato;
  }

  public String getTipoContrato() {
    return TipoContrato;
  }
}
