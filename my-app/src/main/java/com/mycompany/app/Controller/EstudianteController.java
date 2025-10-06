package com.mycompany.app.Controller;

import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Controller.ServicesClass.EstudianteService;
import java.util.List;

public class EstudianteController {
  EstudianteService estudianteService;

  private static volatile EstudianteController instance;

  public EstudianteController(EstudianteService estudianteService) {
    this.estudianteService = estudianteService;
  }

  public static EstudianteController getInstance(EstudianteService estudianteService) {
    if (instance == null) {
      synchronized (EstudianteController.class) {
        if (instance == null) {
          instance = new EstudianteController(estudianteService);
        }
      }
    }
    return instance;
  }

  public void insertar(EstudianteDTO eDTO) {
    estudianteService.insertar(eDTO);
  }

  public List<EstudianteDTO> listar() {
    return estudianteService.listar();
  }

  public void actualizar(EstudianteDTO eDTO) {
    estudianteService.actualizar(eDTO);
  }

  public void eliminar(double id) {
    estudianteService.eliminar(id);
  }
}
