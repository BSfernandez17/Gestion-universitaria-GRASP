package com.mycompany.app.Controller;

import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.Controller.ServicesClass.ProgramaService;
import java.util.List;

public class ProgramaController {
  ProgramaService programaService;

  public ProgramaController(ProgramaService programaService) {
    this.programaService = programaService;
  }

  public void insertar(ProgramaDTO pDTO) {
    programaService.insertar(pDTO);
  }

  public List<ProgramaDTO> listar() {
    return programaService.listar();
  }

  public void actualizar(ProgramaDTO pDTO) {
    programaService.actualizar(pDTO);
  }

  public void eliminar(Double id) {
    programaService.eliminar(id);
  }
}
