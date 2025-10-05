package com.mycompany.app.Controller.ServicesClass;

import com.mycompany.app.Controller.Mappers.EstudianteMapper;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Persistence.DAO.EstudianteDAO;

public class EstudianteService {
  EstudianteDAO estudianteDAO;

<<<<<<< HEAD
  EstudianteService(EstudianteDAO estudianteDAO) {
=======
  public EstudianteService(EstudianteDAO estudianteDAO) {
>>>>>>> feature/oracle-integration
    this.estudianteDAO = estudianteDAO;
  }

  public void insertar(EstudianteDTO eDTO) {
    estudianteDAO.insertar(eDTO);
  }

  public java.util.List<EstudianteDTO> listar() {
    return EstudianteMapper.toDTOList(estudianteDAO.listar());
  }

  public void actualizar(EstudianteDTO eDTO) {
    estudianteDAO.actualizar(eDTO);
  }

  public void eliminar(double id) {
    estudianteDAO.eliminar(id);
  }
}
