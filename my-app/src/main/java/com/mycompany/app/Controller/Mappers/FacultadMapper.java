package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Facultad;
import com.mycompany.app.DTO.FacultadDTO;

public class FacultadMapper {

  public static Facultad toEntity(FacultadDTO dto) {
    return new Facultad(dto.getID(), dto.getNombre(), PersonaMapper.toEntity(dto.getDecanoDTO()));
  }

  public static FacultadDTO toDTO(Facultad entity) {
    return new FacultadDTO(entity.getID(), entity.getNombre(), PersonaMapper.toDTO(entity.getDecano()));
  }
}
