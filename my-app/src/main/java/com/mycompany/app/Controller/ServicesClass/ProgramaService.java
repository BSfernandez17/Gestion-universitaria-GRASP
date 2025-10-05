package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.ProgramaMapper;
import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import java.util.List;

public class ProgramaService {
  ProgramaDAO programaDAO;

  public ProgramaService(ProgramaDAO programaDAO) {
    this.programaDAO = programaDAO;
  }

  public void insertar(ProgramaDTO pDTO) {
    programaDAO.insertar(pDTO);
  }

  public List<ProgramaDTO> listar() {
    return ProgramaMapper.toDTOList(programaDAO.listar());
  }

  public void actualizar(ProgramaDTO pDTO) {
    programaDAO.actualizar(pDTO);
  }

  public void eliminar(Double id) {
    programaDAO.eliminar(id);
  }
}
