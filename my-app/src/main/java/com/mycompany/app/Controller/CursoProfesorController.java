package com.mycompany.app.Controller;

import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.DTO.CursoProfesorDTO;

import java.util.List;

public class CursoProfesorController {
  CursoProfesorService cursoProfesorService;

  public CursoProfesorController(CursoProfesorService cursoProfesorService) {
    this.cursoProfesorService = cursoProfesorService;
  }

  public void insertar(CursoProfesorDTO cpDTO) {
    cursoProfesorService.insertar(cpDTO);
  }

  public List<CursoProfesorDTO> listar() {
    return cursoProfesorService.listar();
  }

  public void actualizar(CursoProfesorDTO cpDTO) {
    cursoProfesorService.actualizar(cpDTO);
  }

  public void eliminar(double id) {
    cursoProfesorService.eliminar(id);
  }
}
