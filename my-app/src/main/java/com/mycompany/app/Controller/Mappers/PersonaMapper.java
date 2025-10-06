package com.mycompany.app.Controller.Mappers;

//esta clase pasa de DTO a modelo y viceversa
import com.mycompany.app.Model.Persona;
import com.mycompany.app.DTO.PersonaDTO;
import java.util.List;
import java.util.ArrayList;

public class PersonaMapper {
  public static Persona toEntity(PersonaDTO dto) {
    if (dto == null) return null;
    return new Persona(dto.getID(), dto.getNombres(), dto.getApellidos(), dto.getEmail());
  }

  public static List<Persona> toEntityList(List<PersonaDTO> dtos) {
    java.util.List<Persona> entityList = new ArrayList<>();
    for (PersonaDTO dto : dtos) {
      entityList.add(toEntity(dto));
    }
    return entityList;
  }

  public static List<PersonaDTO> toDTOList(List<Persona> entities) {
    java.util.List<PersonaDTO> dtoList = new ArrayList<>();
    for (Persona entity : entities) {
      PersonaDTO dto = toDTO(entity);
      if (dto != null) dtoList.add(dto);
    }
    return dtoList;
  }

  public static PersonaDTO toDTO(Persona entity) {
    if (entity == null) return null;
    return new PersonaDTO(entity.getID(), entity.getNombres(), entity.getApellidos(), entity.getEmail());
  }
}
