package com.mycompany.app;

import java.sql.Connection;
import com.mycompany.app.Controller.ServicesClass.FacultadService;
import com.mycompany.app.Controller.ServicesClass.InscripcionService;
import com.mycompany.app.Controller.CursoController;
import com.mycompany.app.Controller.CursoProfesorController;
import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.Controller.ServicesClass.CursoService;
import com.mycompany.app.Controller.ServicesClass.PersonaService;
import com.mycompany.app.Controller.ServicesClass.ProfesorService;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.DTO.ProfesorDTO;
import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.DTO.CursoProfesorDTO;
import com.mycompany.app.DTO.InscripcionDTO;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.Persistence.DAO.ProfesorDAO;
import com.mycompany.app.Controller.PersonaController;
import com.mycompany.app.Controller.ProfesorController;
import com.mycompany.app.Persistence.DAO.EstudianteDAO;
import com.mycompany.app.Persistence.DAO.FacultadDAO;
import com.mycompany.app.Persistence.DAO.InscripcionDAO;
import com.mycompany.app.Controller.ServicesClass.EstudianteService;
import com.mycompany.app.Controller.EstudianteController;
import com.mycompany.app.Controller.FacultadController;
import com.mycompany.app.Controller.InscripcionController;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.Controller.ServicesClass.ProgramaService;
import com.mycompany.app.Persistence.DAO.CursoDAO;

public class App {
  public static void main(String[] args) {
  Connection conn = ConnectionDb.createConnection();
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
    System.out.println("Lista estudiantes con estudiante prueba");
    estudianteController.listar().forEach(System.out::println);

    System.out.println("--------------------------------------------------");
    System.out.println("Test Profesores");

    ProfesorDAO profesorDAO=new ProfesorDAO(conn);
    ProfesorService profesorService=new ProfesorService(profesorDAO);
    ProfesorController profesorController=new ProfesorController(profesorService);
    
    // 1. Crear y guardar la persona primero
    PersonaDTO personaProfesor = new PersonaDTO(6001.0, "Profesor", "Prueba", "profesor@prueba.com");
    personaController.insertar(personaProfesor);

    // 2. Usar la persona recién creada para el profesor
    ProfesorDTO profesorDTO= new ProfesorDTO(personaProfesor.getID(), personaProfesor.getNombres(), personaProfesor.getApellidos(), personaProfesor.getEmail(), "ocasional");
    
    profesorController.insertar(profesorDTO);

    System.out.println("Lista con profesor prueba");
    profesorController.listar().forEach(System.out::println);

    System.out.println("----------------------------------------------------------");
    System.out.println("TEST DE CURSOS");
    System.out.println("Lista de cursos:");
    CursoDAO cursoDAO=new CursoDAO(conn);
    CursoService cursoService=new CursoService(cursoDAO);
    CursoController cursocontroller=new CursoController(cursoService);
    cursocontroller.listar().forEach(System.out::println);
    CursoDTO cDTO= new CursoDTO(1.0,"cursoPrueba",prDTO,true);
    cursocontroller.insertar(cDTO);

    System.out.println("Cursos con curso prueba");
    cursocontroller.listar().forEach(System.out::println);
  System.out.println("-----------------------------------------------------");
  System.out.println("TEST INSCRIPCION");
  InscripcionDAO inscripcionDAO=new InscripcionDAO(conn);
  InscripcionService inscripcionService=new InscripcionService(inscripcionDAO);
  InscripcionController inscripcionController=new InscripcionController(inscripcionService);
  System.out.println("Lista de inscripciones");
  inscripcionController.listar().forEach(System.out::println);
    
  // Crear una nueva inscripción
  InscripcionDTO nuevaInscripcion = new InscripcionDTO(1.0, cDTO, 2025, 2, eDTO);
  inscripcionController.insertar(nuevaInscripcion);

  System.out.println("Lista de inscripciones con la nueva inscripción:");
  inscripcionController.listar().forEach(System.out::println);

  System.out.println("----------------------------------------------------------");
  System.out.println("TEST CURSOS PROFESOR");

  CursoProfesorDAO cursoProfesorDAO=new CursoProfesorDAO(conn);
  CursoProfesorService cursoProfesorService=new CursoProfesorService(cursoProfesorDAO);
  CursoProfesorController cursoProfesorController= new CursoProfesorController(cursoProfesorService);
  cursoProfesorController.listar().forEach(System.out::println);
  CursoProfesorDTO cpDTO=new CursoProfesorDTO(1.0, profesorDTO, 2024, 1, cDTO);
  cursoProfesorController.insertar(cpDTO);
  System.out.println("LISTA CON CURSO PROFESOR NUEVO:");
  cursoProfesorController.listar().forEach(System.out::println);
    // Cerrar la conexión al finalizar la ejecución del ejemplo
    ConnectionDb.closeConnection(conn);
  }
}
