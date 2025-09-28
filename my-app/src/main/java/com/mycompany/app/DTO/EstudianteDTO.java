package com.mycompany.app.DTO;

public class EstudianteDTO extends PersonaDTO {
  private Double codigo;
  private String nombrePrograma;
  private Boolean activo;
  private Double promedio;

  public EstudianteDTO(Double ID, String nombres, String apellidos, String email, Double codigo, String nombrePrograma,
      Boolean activo, Double promedio) {
    super(ID, nombres, apellidos, email);

    this.codigo = codigo;
    this.nombrePrograma = nombrePrograma;
    this.activo = activo;
    this.promedio = promedio;
  }

  public Double getCodigo() {
    return codigo;
  }

  public String getNombrePrograma() {
    return nombrePrograma;
  }

  public Boolean getActivo() {
    return activo;
  }

  public Double getPromedio() {
    return promedio;
  }
}
