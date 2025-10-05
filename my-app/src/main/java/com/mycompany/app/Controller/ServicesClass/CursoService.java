package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.CursoMapper;
import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.Persistence.DAO.CursoDAO;
import java.util.List;

public class CursoService {
  CursoDAO cursoDAO;

  public CursoService(CursoDAO cursoDAO) {
    this.cursoDAO = cursoDAO;
  }

  public void insertar(CursoDTO cDTO) {
    cursoDAO.insertar(cDTO);
  }

  public List<CursoDTO> listar() {
    return CursoMapper.toDTOList(cursoDAO.listar());
  }

  public void actualizar(CursoDTO cDTO) {
    cursoDAO.actualizar(cDTO);
  }

  public void eliminar(Integer id) {
    cursoDAO.eliminar(id);
  }
}
