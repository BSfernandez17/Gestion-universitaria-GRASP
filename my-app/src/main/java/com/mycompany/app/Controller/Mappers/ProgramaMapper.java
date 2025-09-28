package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Programa;
import com.mycompany.app.DTO.ProgramaDTO;

public class ProgramaMapper {
  public static Programa toEntity(ProgramaDTO dto) {
    return new Programa(dto.getID(), dto.getNombre(), dto.getDuracion(), dto.getRegistro(),
        FacultadMapper.toEntity(dto.getFacultadDTO()));
  }

  public static ProgramaDTO toDTO(Programa entity) {
    return new ProgramaDTO(entity.getID(), entity.getNombre(), entity.getDuracion(), entity.getRegistro(),
        FacultadMapper.toDTO(entity.getFacultad()));
  }
}
