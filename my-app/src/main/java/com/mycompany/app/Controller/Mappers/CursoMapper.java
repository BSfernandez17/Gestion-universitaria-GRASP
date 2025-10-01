package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Curso;
import com.mycompany.app.DTO.CursoDTO;
import java.util.ArrayList;
import java.util.List;

public class CursoMapper {
  public static Curso toEntity(CursoDTO dto) {
    return new Curso(dto.getID(), dto.getNombre(), ProgramaMapper.toEntity(dto.getProgramaDTO()), dto.getActivo());
  }

  public static CursoDTO toDTO(Curso entity) {
    return new CursoDTO(entity.getID(), entity.getNombre(), ProgramaMapper.toDTO(entity.getPrograma()),
        entity.getActivo());
  }

  public static List<CursoDTO> toDTOList(List<Curso> entities) {
    List<CursoDTO> dtoList = new ArrayList<>();
    for (Curso entity : entities) {
      dtoList.add(toDTO(entity));
    }
    return dtoList;
  }

  public static List<Curso> toEntityList(List<CursoDTO> dtos) {
    List<Curso> entityList = new ArrayList<>();
    for (CursoDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
