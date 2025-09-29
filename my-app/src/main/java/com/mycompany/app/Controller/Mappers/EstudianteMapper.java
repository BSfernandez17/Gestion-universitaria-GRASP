package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.DTO.EstudianteDTO;
import java.util.ArrayList;
import java.util.List;

public class EstudianteMapper {

  public static Estudiante toEntity(EstudianteDTO dto) {
    return new Estudiante(dto.getID(), dto.getNombres(), dto.getApellidos(), dto.getEmail(), dto.getCodigo(),
        ProgramaMapper.toEntity(dto.getPrograma()),
        dto.getActivo(), dto.getPromedio());
  }

  public static EstudianteDTO toDTO(Estudiante entity) {
    return new EstudianteDTO(entity.getID(), entity.getNombres(), entity.getApellidos(), entity.getEmail(),
        entity.getCodigo(), ProgramaMapper.toDTO(entity.getPrograma()), entity.getActivo(), entity.getPromedio());
  }

  public static List<EstudianteDTO> toDTOList(List<Estudiante> entities) {
    java.util.List<EstudianteDTO> dtoList = new ArrayList<>();
    for (Estudiante entity : entities) {
      dtoList.add(toDTO(entity));
    }
    return dtoList;
  }

  public static List<Estudiante> toEntityList(List<EstudianteDTO> dtos) {
    java.util.List<Estudiante> entityList = new ArrayList<>();
    for (EstudianteDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }
}
