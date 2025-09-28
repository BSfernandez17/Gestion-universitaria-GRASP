package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Inscripcion;
import com.mycompany.app.DTO.InscripcionDTO;

public class InscripcionMappper {
  public static Inscripcion toEntity(InscripcionDTO dto) {
    return new Inscripcion(dto.getID(), CursoMapper.toEntity(dto.getCursoDTO()), dto.getAño(), dto.getSemestre(),
        EstudianteMapper.toEntity(dto.getEstudianteDTO()));
  }

  public static InscripcionDTO toDTO(Inscripcion entity) {
    return new InscripcionDTO(entity.getID(), CursoMapper.toDTO(entity.getCurso()), entity.getAño(),
        entity.getSemestre(),
        EstudianteMapper.toDTO(entity.getEstudiante()));
  }
}
