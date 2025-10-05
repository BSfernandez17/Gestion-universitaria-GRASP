package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.DTO.InscripcionDTO;
import com.mycompany.app.Controller.Mappers.InscripcionMappper;
import com.mycompany.app.Persistence.DAO.InscripcionDAO;
import java.util.List;

public class InscripcionService {
  InscripcionDAO inscripcionDAO;

<<<<<<< HEAD
  InscripcionService(InscripcionDAO inscripcionDAO) {
=======
  public InscripcionService(InscripcionDAO inscripcionDAO) {
>>>>>>> feature/oracle-integration
    this.inscripcionDAO = inscripcionDAO;
  }

  public void insertar(InscripcionDTO iDTO) {
    inscripcionDAO.insertar(iDTO);
  }

  public List<InscripcionDTO> listar() {
    return InscripcionMappper.toDTOList(inscripcionDAO.listar());
  }

  public void actualizar(InscripcionDTO iDTO) {
    inscripcionDAO.actualizar(iDTO);
  }

  public void eliminar(double id) {
    inscripcionDAO.eliminar(id);
  }
}
