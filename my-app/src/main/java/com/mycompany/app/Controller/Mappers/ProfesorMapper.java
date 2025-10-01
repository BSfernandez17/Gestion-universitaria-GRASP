package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Profesor;
import com.mycompany.app.DTO.ProfesorDTO;
import java.util.ArrayList;
import java.util.List;

public class ProfesorMapper {
  public static Profesor toEntity(ProfesorDTO dto) {
    return new Profesor(dto.getID(), dto.getNombres(), dto.getApellidos(), dto.getEmail(), dto.getTipoContrato());
  }

public static ProfesorDTO toDTO(Profesor entity) {
    if (entity == null) return null; // evita NPE
    return new ProfesorDTO(
        entity.getID(),
        entity.getNombres(),
        entity.getApellidos(),
        entity.getEmail(),
        entity.getTipoContrato()
    );
}

public static List<ProfesorDTO> toDTOList(List<Profesor> entities) {
    List<ProfesorDTO> dtoList = new ArrayList<>();
    for (Profesor entity : entities) {
        if(entity != null) { // solo agrega si no es null
            dtoList.add(toDTO(entity));
        }
    }
    return dtoList;
}

  public static List<Profesor> toEntityList(List<ProfesorDTO> dtos) {
    List<Profesor> entityList = new ArrayList<>();
    for (ProfesorDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
