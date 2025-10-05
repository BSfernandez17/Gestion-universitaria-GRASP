package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.PersonaMapper;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import java.util.List;

public class PersonaService {
  PersonaDAO personaDAO;

<<<<<<< HEAD
  PersonaService(PersonaDAO personaDAO) {
=======
  public PersonaService(PersonaDAO personaDAO) {
>>>>>>> feature/oracle-integration
    this.personaDAO = personaDAO;
  }

  public void insertar(PersonaDTO pDTO) {
<<<<<<< HEAD
    personaDAO.insertar(pDTO);
=======
    try {
      personaDAO.insertar(pDTO);
    } catch (Exception e) {
      System.out.println("Error al insertar persona: " + e.getMessage());
    }
>>>>>>> feature/oracle-integration
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
