package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Inscripcion;
import com.mycompany.app.DTO.InscripcionDTO;
import java.util.ArrayList;
import java.util.List;

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

  public static List<InscripcionDTO> toDTOList(List<Inscripcion> entities) {
    List<InscripcionDTO> dtoList = new ArrayList<>();
    for (Inscripcion entity : entities) {
      dtoList.add(toDTO(entity));
    }
    return dtoList;
  }

  public static List<Inscripcion> toEntityList(List<InscripcionDTO> dtos) {
    List<Inscripcion> entityList = new ArrayList<>();
    for (InscripcionDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
