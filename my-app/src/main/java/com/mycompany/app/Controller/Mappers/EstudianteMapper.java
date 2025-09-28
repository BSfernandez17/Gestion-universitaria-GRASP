package com.mycompany.app.Controller.Mappers;

import com.mycompany.app.Model.Estudiante;
import com.mycompany.app.DTO.EstudianteDTO;

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
}
