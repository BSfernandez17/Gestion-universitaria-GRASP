package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.CursoProfesor;
import com.mycompany.app.DTO.CursoProfesorDTO;
import java.util.ArrayList;
import java.util.List;

public class CursoProfesorMapper {
  public static CursoProfesor toEntity(CursoProfesorDTO dto) {
    return new CursoProfesor(dto.getID(), ProfesorMapper.toEntity(dto.getProfesorDTO()), dto.getAño(),
        dto.getSemestre(),
        CursoMapper.toEntity(dto.getCursoDTO()));
  }

  public static CursoProfesorDTO toDTO(CursoProfesor entity) {
    return new CursoProfesorDTO(entity.getID(), ProfesorMapper.toDTO(entity.getProfesor()), entity.getAño(),
        entity.getSemestre(),
        CursoMapper.toDTO(entity.getCurso()));
  }

  public static List<CursoProfesorDTO> toDTOList(List<CursoProfesor> entities) {
    List<CursoProfesorDTO> dtoList = new ArrayList<>();
    for (CursoProfesor entity : entities) {
      dtoList.add(toDTO(entity));
    }
    return dtoList;
  }

  public static List<CursoProfesor> toEntityList(List<CursoProfesorDTO> dtos) {
    List<CursoProfesor> entityList = new ArrayList<>();
    for (CursoProfesorDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
