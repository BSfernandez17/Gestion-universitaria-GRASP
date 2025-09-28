package com.mycompany.app.Controller.Mappers;

//esta clase pasa de DTO a modelo y viceversa
import com.mycompany.app.Model.Persona;
import com.mycompany.app.DTO.PersonaDTO;

public class PersonaMapper {
  public static Persona toEntity(PersonaDTO dto) {
    return new Persona(dto.getID(), dto.getNombres(), dto.getApellidos(), dto.getEmail());
  }

  public static PersonaDTO toDTO(Persona entity) {
    return new PersonaDTO(entity.getID(), entity.getNombres(), entity.getApellidos(), entity.getEmail());
  }
}
