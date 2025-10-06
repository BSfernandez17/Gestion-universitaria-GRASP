package com.mycompany.app.Controller;

import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.Controller.ServicesClass.FacultadService;
import java.util.List;

public class FacultadController {
  FacultadService facultadService;

  public FacultadController(FacultadService facultadService) {
    this.facultadService = facultadService;
  }

  public void insertar(FacultadDTO fDTO) {
    facultadService.insertar(fDTO);
  }

  public List<FacultadDTO> listar() {
    return facultadService.listar();
  }

  public void actualizar(FacultadDTO fDTO) {
    facultadService.actualizar(fDTO);
  }

  public void eliminar(Double id) {
    facultadService.eliminar(id);
  }
}
