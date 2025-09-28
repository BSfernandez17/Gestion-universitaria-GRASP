package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Curso;
import com.mycompany.app.DTO.CursoDTO;

public class CursoMapper {
  public static Curso toEntity(CursoDTO dto) {
    return new Curso(dto.getID(), dto.getNombre(), ProgramaMapper.toEntity(dto.getProgramaDTO()), dto.getActivo());
  }

  public static CursoDTO toDTO(Curso entity) {
    return new CursoDTO(entity.getID(), entity.getNombre(), ProgramaMapper.toDTO(entity.getPrograma()),
        entity.getActivo());
  }
}
