package com.mycompany.app.Controller;

import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.Controller.ServicesClass.PersonaService;

import java.util.List;

public class PersonaController {
  PersonaService personaService;

  public PersonaController(PersonaService personaService) {
    this.personaService = personaService;
  }

  public void insertar(PersonaDTO pDTO) {
    personaService.insertar(pDTO);
  }

  public List<PersonaDTO> listar() {
    return personaService.listar();
  }

  public void actualizar(PersonaDTO pDTO) {
    personaService.actualizar(pDTO);
  }

  public void eliminar(double id) {
    personaService.eliminar(id);
  }
}
