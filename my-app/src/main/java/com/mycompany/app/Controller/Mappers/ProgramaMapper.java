package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Programa;
import com.mycompany.app.DTO.ProgramaDTO;
import java.util.ArrayList;
import java.util.List;

public class ProgramaMapper {
  public static Programa toEntity(ProgramaDTO dto) {
    return new Programa(dto.getID(), dto.getNombre(), dto.getDuracion(), dto.getRegistro(),
        FacultadMapper.toEntity(dto.getFacultadDTO()));
  }

  public static ProgramaDTO toDTO(Programa entity) {
    if (entity == null)
      return null;
    return new ProgramaDTO(entity.getID(), entity.getNombre(), entity.getDuracion(), entity.getRegistro(),
        FacultadMapper.toDTO(entity.getFacultad()));
  }

  public static List<ProgramaDTO> toDTOList(List<Programa> entities) {
    List<ProgramaDTO> dtoList = new ArrayList<>();
<<<<<<< HEAD
    for (Programa entity : entities) {
      dtoList.add(toDTO(entity));
=======

    for (Programa entity : entities) {
      if (entity != null)
        dtoList.add(toDTO(entity));
>>>>>>> feature/oracle-integration
    }
    return dtoList;
  }

  public static List<Programa> toEntityList(List<ProgramaDTO> dtos) {
    List<Programa> entityList = new ArrayList<>();
    for (ProgramaDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
