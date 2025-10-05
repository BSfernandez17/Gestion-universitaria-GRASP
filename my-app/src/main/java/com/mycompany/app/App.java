package com.mycompany.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;
import com.mycompany.app.Controller.ServicesClass.FacultadService;
import com.mycompany.app.Controller.CursoProfesorController;
import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.Controller.ServicesClass.PersonaService;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.Controller.PersonaController;
import com.mycompany.app.Persistence.DAO.EstudianteDAO;
import com.mycompany.app.Persistence.DAO.FacultadDAO;
import com.mycompany.app.Controller.ServicesClass.EstudianteService;
import com.mycompany.app.Controller.EstudianteController;
import com.mycompany.app.Controller.FacultadController;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.Controller.ServicesClass.ProgramaService;

public class App {
  public static void main(String[] args) {
    Connection conn = ConnectionDb.getConnection();
    PersonaDAO personaDAO = new PersonaDAO(conn);
    PersonaService personaService = new PersonaService(personaDAO);
    PersonaController personaController = new PersonaController(personaService);
    System.out.println(personaController.listar());
    PersonaDTO persona = new PersonaDTO(1.0, "PeronsaPrueba", "ApellidoPrueba", "emailPrueba@prubea");
    personaController.insertar(persona);
    System.out.println("LISTA PERSONAS CON PERSONA PRUEBA");
    System.out.println(personaController.listar());
    System.out.println("------------------------------------------------------------");
    persona.setNombres("PersonaPruebaActualizada");
    personaController.actualizar(persona);
    System.out.println("LISTA PERSONAS CON PERSONA PRUEBA ACTUALIZADA");
    personaController.listar().forEach(System.out::println);

    System.out.println("------------------------------------------------------------");
    personaController.eliminar(4.0);

    System.out.println("LISTA PERSONAS SIN PERSONA PRUEBA");
    System.out.println(personaController.listar());
    personaController.insertar(persona);
    System.out.println("------------------------------------------------------------");
    System.out.println("TEST ESTUANTES");
    EstudianteDAO estudianteDAO = new EstudianteDAO(conn);
    EstudianteService estudianteService = new EstudianteService(estudianteDAO);
    EstudianteController estudianteController = new EstudianteController(estudianteService);
    estudianteController.listar().forEach(System.out::println);
    FacultadDTO facultadDTO = new FacultadDTO(1.0, "FacultadPrueba", persona);
    FacultadDAO facultadDAO = new FacultadDAO(conn);
    FacultadService facultadService = new FacultadService(facultadDAO);
    FacultadController facultadController = new FacultadController(facultadService);
    facultadController.insertar(facultadDTO);
    System.out.println("LISTA FACULTADES CON FACULTAD PRUEBA");

    facultadController.listar().forEach(System.out::println);

    ProgramaDTO prDTO = new ProgramaDTO(1.0, "programaPrueba", 10.0, java.sql.Date.valueOf("2025-10-04"), facultadDTO);
    ProgramaDAO programaDAO = new ProgramaDAO(conn);
    ProgramaService programaService = new ProgramaService(programaDAO);
    ProgramaController programaController = new ProgramaController(programaService);
    programaController.insertar(prDTO);

    programaController.listar().forEach(System.out::println);
    EstudianteDTO eDTO = new EstudianteDTO(4.0, "estudiantePruba", "prueba", "estudiante@prueba", 2202002.0, prDTO,
        true,
        3.8);

    estudianteController.insertar(eDTO);

  }
}
