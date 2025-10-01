package com.mycompany.app.Controller;

import com.mycompany.app.DTO.InscripcionDTO;
import com.mycompany.app.Controller.ServicesClass.InscripcionService;
import java.util.List;

public class InscripcionController {
  InscripcionService inscripcionService;

  public InscripcionController(InscripcionService inscripcionService) {
    this.inscripcionService = inscripcionService;
  }

  public void insertar(InscripcionDTO iDTO) {
    inscripcionService.insertar(iDTO);
  }

  public List<InscripcionDTO> listar() {
    return inscripcionService.listar();
  }

  public void actualizar(InscripcionDTO iDTO) {
    inscripcionService.actualizar(iDTO);
  }

  public void eliminar(Double id) {
    inscripcionService.eliminar(id);
  }
}
