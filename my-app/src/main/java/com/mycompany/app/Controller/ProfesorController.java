package com.mycompany.app.Controller;

import com.mycompany.app.DTO.ProfesorDTO;
import com.mycompany.app.Controller.ServicesClass.ProfesorService;
import java.util.List;

public class ProfesorController {
  ProfesorService profesorService;

  public ProfesorController(ProfesorService profesorService) {
    this.profesorService = profesorService;
  }

  public void insertar(ProfesorDTO pDTO) {
    profesorService.insertar(pDTO);
  }

  public List<ProfesorDTO> listar() {
    return profesorService.listar();
  }

  public void actualizar(ProfesorDTO pDTO) {
    profesorService.actualizar(pDTO);
  }

  public void eliminar(double id) {
    profesorService.eliminar(id);
  }
}
