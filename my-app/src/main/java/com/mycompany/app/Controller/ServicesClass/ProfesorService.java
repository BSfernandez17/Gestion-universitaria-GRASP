package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.ProfesorMapper;
import com.mycompany.app.DTO.ProfesorDTO;
import com.mycompany.app.Persistence.DAO.ProfesorDAO;
import java.util.List;

public class ProfesorService {
  ProfesorDAO profesorDAO;

  public ProfesorService(ProfesorDAO profesorDAO) {
    this.profesorDAO = profesorDAO;
  }

  public void insertar(ProfesorDTO pDTO) {
    profesorDAO.insertar(pDTO);
  }

  public List<ProfesorDTO> listar() {
    return ProfesorMapper.toDTOList(profesorDAO.listar());
  }

  public void actualizar(ProfesorDTO pDTO) {
    profesorDAO.actualizar(pDTO);
  }

  public void eliminar(double id) {
    profesorDAO.eliminar(id);
  }
}
