package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.CursoProfesor;
import com.mycompany.app.DTO.CursoProfesorDTO;

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
}
