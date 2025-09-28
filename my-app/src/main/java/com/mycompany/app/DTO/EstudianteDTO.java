package com.mycompany.app.DTO;

public class EstudianteDTO extends PersonaDTO {
  private Double codigo;
  private ProgramaDTO programaDTO;
  private Boolean activo;
  private Double promedio;

  public EstudianteDTO(Double ID, String nombres, String apellidos, String email, Double codigo,
      ProgramaDTO programaDTO,
      Boolean activo, Double promedio) {
    super(ID, nombres, apellidos, email);

    this.codigo = codigo;
    this.programaDTO = programaDTO;
    this.activo = activo;
    this.promedio = promedio;
  }

  public Double getCodigo() {
    return codigo;
  }

  public ProgramaDTO getPrograma() {
    return programaDTO;
  }

  public Boolean getActivo() {
    return activo;
  }

  public Double getPromedio() {
    return promedio;
  }
}
