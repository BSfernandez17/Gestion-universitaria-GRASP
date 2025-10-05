package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.Controller.Mappers.FacultadMapper;
import com.mycompany.app.Persistence.DAO.FacultadDAO;
import java.util.List;

public class FacultadService {
  FacultadDAO facultadDAO;

<<<<<<< HEAD
  FacultadService(FacultadDAO facultadDAO) {
=======
  public FacultadService(FacultadDAO facultadDAO) {
>>>>>>> feature/oracle-integration
    this.facultadDAO = facultadDAO;
  }

  public void insertar(FacultadDTO fDTO) {
    facultadDAO.insertar(fDTO);
  }

  public List<FacultadDTO> listar() {
    return FacultadMapper.toDTOList(facultadDAO.listar());
  }

  public void actualizar(FacultadDTO fDTO) {
    facultadDAO.actualizar(fDTO);
  }

  public void eliminar(Double id) {
    facultadDAO.eliminar(id);
  }
}
