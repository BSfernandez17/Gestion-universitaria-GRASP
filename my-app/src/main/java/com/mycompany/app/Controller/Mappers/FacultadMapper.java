package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Facultad;
import com.mycompany.app.DTO.FacultadDTO;
import java.util.ArrayList;
import java.util.List;

public class FacultadMapper {

  public static Facultad toEntity(FacultadDTO dto) {
    return new Facultad(dto.getID(), dto.getNombre(), PersonaMapper.toEntity(dto.getDecanoDTO()));
  }

  public static FacultadDTO toDTO(Facultad entity) {
    if (entity == null)
      return null;
    return new FacultadDTO(entity.getID(), entity.getNombre(), PersonaMapper.toDTO(entity.getDecano()));
  }

  public static List<FacultadDTO> toDTOList(List<Facultad> entities) {
    List<FacultadDTO> dtoList = new ArrayList<>();
    for (Facultad entity : entities) {
<<<<<<< HEAD
      dtoList.add(toDTO(entity));
=======
      if (entity != null)
        dtoList.add(toDTO(entity));
>>>>>>> feature/oracle-integration
    }
    return dtoList;
  }

  public static List<Facultad> toEntityList(List<FacultadDTO> dtos) {
    List<Facultad> entityList = new ArrayList<>();
    for (FacultadDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
