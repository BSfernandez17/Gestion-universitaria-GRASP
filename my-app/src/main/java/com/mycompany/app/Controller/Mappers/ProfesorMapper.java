package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Profesor;
import com.mycompany.app.DTO.ProfesorDTO;

public class ProfesorMapper {
  public static Profesor toEntity(ProfesorDTO dto) {
    return new Profesor(dto.getID(), dto.getNombres(), dto.getApellidos(), dto.getEmail(), dto.getTipoContrato());
  }

  public static ProfesorDTO toDTO(Profesor entity) {
    return new ProfesorDTO(entity.getID(), entity.getNombres(), entity.getApellidos(), entity.getEmail(),
        entity.getTipoContrato());
  }

}
