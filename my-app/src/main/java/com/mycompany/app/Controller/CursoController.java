package com.mycompany.app.Controller;

import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.Controller.ServicesClass.CursoService;
import java.util.List;

public class CursoController {
  CursoService cursoService;

  private static volatile CursoController instance;

  public CursoController(CursoService cursoService) {
    this.cursoService = cursoService;
  }

  // Thread-safe singleton accessor. Keeps backward compat by accepting the service.
  public static CursoController getInstance(CursoService cursoService) {
    if (instance == null) {
      synchronized (CursoController.class) {
        if (instance == null) {
          instance = new CursoController(cursoService);
        }
      }
    }
    return instance;
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
