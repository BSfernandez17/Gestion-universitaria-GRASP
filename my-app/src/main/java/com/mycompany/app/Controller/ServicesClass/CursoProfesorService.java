package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.CursoProfesorMapper;
import com.mycompany.app.DTO.CursoProfesorDTO;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;
import java.util.List;

public class CursoProfesorService {
  private CursoProfesorDAO cursoProfesorDAO;

  public CursoProfesorService(CursoProfesorDAO cursoProfesorDAO) {
    this.cursoProfesorDAO = cursoProfesorDAO;
  }

  public void insertar(CursoProfesorDTO cpDTO) {
    cursoProfesorDAO.insertar(cpDTO);
  }

  public List<CursoProfesorDTO> listar() {
    return CursoProfesorMapper.toDTOList(cursoProfesorDAO.listar());
  }

  public void actualizar(CursoProfesorDTO cpDTO) {
    cursoProfesorDAO.actualizar(cpDTO);
  }

  public void eliminar(double id) {
    cursoProfesorDAO.eliminar(id);
  }
}
