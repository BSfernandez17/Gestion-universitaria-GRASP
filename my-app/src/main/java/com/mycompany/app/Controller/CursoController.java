package com.mycompany.app.Controller;

import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.Controller.ServicesClass.CursoService;
import java.util.List;

public class CursoController {
  CursoService cursoService;

  public CursoController(CursoService cursoService) {
    this.cursoService = cursoService;
  }

  public void insertar(CursoDTO cDTO) {
    cursoService.insertar(cDTO);
  }

  public List<CursoDTO> listar() {
    return cursoService.listar();
  }

  public void actualizar(CursoDTO cDTO) {
    cursoService.actualizar(cDTO);
  }

  public void eliminar(Integer id) {
    cursoService.eliminar(id);
  }
}
