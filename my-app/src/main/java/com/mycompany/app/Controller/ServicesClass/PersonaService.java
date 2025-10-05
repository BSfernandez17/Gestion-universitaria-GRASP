package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.PersonaMapper;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import java.util.List;

public class PersonaService {
  PersonaDAO personaDAO;

  public PersonaService(PersonaDAO personaDAO) {
    this.personaDAO = personaDAO;
  }

  public void insertar(PersonaDTO pDTO) {
    try {
      personaDAO.insertar(pDTO);
    } catch (Exception e) {
      System.out.println("Error al insertar persona: " + e.getMessage());
    }
  }

  public List<PersonaDTO> listar() {
    return PersonaMapper.toDTOList(personaDAO.listar());
  }

  public void actualizar(PersonaDTO pDTO) {
    personaDAO.actualizar(pDTO);
  }

  public void eliminar(double id) {
    personaDAO.eliminar(id);
  }
}
