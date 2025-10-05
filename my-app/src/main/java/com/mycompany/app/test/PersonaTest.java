package com.mycompany.app.test;

import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.Controller.PersonaController;
import com.mycompany.app.Controller.ServicesClass.PersonaService;
import com.mycompany.app.DTO.PersonaDTO;
import java.sql.Connection;
import java.util.List;

public class PersonaTest {
  PersonaController personaController;
  PersonaController personaService;
  PersonaDAO personaDAO;

  public PersonaTest(Connection connection) {
    PersonaDAO personaDAO = new PersonaDAO(connection);
    PersonaService personaService = new PersonaService(personaDAO);
    personaController = new PersonaController(personaService);
  }

  public List<PersonaDTO> listar() {
    return personaController.listar();
  }

}
