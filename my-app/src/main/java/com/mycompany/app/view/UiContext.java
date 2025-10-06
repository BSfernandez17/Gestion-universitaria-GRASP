package com.mycompany.app.view;

import java.sql.Connection;
import java.util.Date;

import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Controller.EstudianteController;
import com.mycompany.app.Controller.PersonaController;
import com.mycompany.app.Controller.FacultadController;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.Controller.ServicesClass.EstudianteService;
import com.mycompany.app.Controller.ServicesClass.ProfesorService;
import com.mycompany.app.Controller.ServicesClass.PersonaService;
import com.mycompany.app.Controller.ServicesClass.FacultadService;
import com.mycompany.app.Controller.ServicesClass.ProgramaService;
import com.mycompany.app.Controller.ServicesClass.CursoService;
import com.mycompany.app.Controller.ServicesClass.InscripcionService;
import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.Persistence.DAO.EstudianteDAO;
import com.mycompany.app.Persistence.DAO.ProfesorDAO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.Persistence.DAO.FacultadDAO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Persistence.DAO.CursoDAO;
import com.mycompany.app.Persistence.DAO.InscripcionDAO;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.DTO.ProgramaDTO;

/**
 * Provee instancias compartidas de controladores/servicios/DAOs para la UI.
 */
public class UiContext {
    private static Connection connection;
    // Register the default observer for cursos on class load so the observer
    // receives notifications when DAOs call CursoSubject.getInstance().notify(...)
    static {
        try {
            com.mycompany.app.Patterns.Observer.CursoSubject.getInstance()
                    .register(new com.mycompany.app.Patterns.Observer.CursoCreationObserver());
            System.out.println("[UiContext] CursoCreationObserver registered");
        } catch (Throwable t) {
            System.out.println("[UiContext] failed to register CursoCreationObserver: " + t.getMessage());
        }
    }
    private static EstudianteController estudianteController;
    private static ProgramaController programaController;
    private static PersonaController personaController;
    private static com.mycompany.app.Controller.ProfesorController profesorController;
    private static com.mycompany.app.Controller.CursoController cursoController;
    private static com.mycompany.app.Controller.InscripcionController inscripcionController;
    private static com.mycompany.app.Controller.CursoProfesorController cursoProfesorController;
    private static FacultadController facultadController;

    private static Connection conn() {
        if (connection == null) {
            connection = ConnectionDb.getConnection();
        }
        return connection;
    }

    /**
     * Cierra la conexión usada por el contexto UI. Llamar al shutdown al
     * terminar la aplicación si es necesario.
     */
    public static void shutdown() {
        if (connection != null) {
            ConnectionDb.closeConnection(connection);
            connection = null;
        }
    }

    /**
     * Devuelve la conexión compartida utilizada por el contexto UI.
     * Útil para diagnósticos (no crea una nueva conexión si ya existe).
     */
    public static Connection getSharedConnection() {
        return conn();
    }
    public static EstudianteController estudianteController() {
        if (estudianteController == null) {
            EstudianteDAO dao = new EstudianteDAO(conn());
            EstudianteService service = new EstudianteService(dao);
            estudianteController = new EstudianteController(service);
        }
        return estudianteController;
    }

    public static ProgramaController programaController() {
        if (programaController == null) {
            ProgramaDAO dao = new ProgramaDAO(conn());
            ProgramaService service = new ProgramaService(dao);
            programaController = new ProgramaController(service);
        }
        return programaController;
    }

    public static PersonaController personaController() {
        if (personaController == null) {
            PersonaDAO dao = new PersonaDAO(conn());
            PersonaService service = new PersonaService(dao);
            personaController = new PersonaController(service);
        }
        return personaController;
    }

    public static com.mycompany.app.Controller.ProfesorController profesorController() {
        if (profesorController == null) {
            ProfesorDAO dao = new ProfesorDAO(conn());
            ProfesorService service = new ProfesorService(dao);
            profesorController = new com.mycompany.app.Controller.ProfesorController(service);
        }
        return profesorController;
    }

    public static com.mycompany.app.Controller.CursoController cursoController() {
        if (cursoController == null) {
            CursoDAO dao = new CursoDAO(conn());
            CursoService service = new CursoService(dao);
            cursoController = new com.mycompany.app.Controller.CursoController(service);
        }
        return cursoController;
    }

    public static com.mycompany.app.Controller.InscripcionController inscripcionController() {
        if (inscripcionController == null) {
            InscripcionDAO dao = new InscripcionDAO(conn());
            InscripcionService service = new InscripcionService(dao);
            inscripcionController = new com.mycompany.app.Controller.InscripcionController(service);
        }
        return inscripcionController;
    }

    public static com.mycompany.app.Controller.CursoProfesorController cursoProfesorController() {
        if (cursoProfesorController == null) {
            CursoProfesorDAO dao = new CursoProfesorDAO(conn());
            CursoProfesorService service = new CursoProfesorService(dao);
            cursoProfesorController = new com.mycompany.app.Controller.CursoProfesorController(service);
        }
        return cursoProfesorController;
    }

    public static FacultadController facultadController() {
        if (facultadController == null) {
            FacultadDAO dao = new FacultadDAO(conn());
            FacultadService service = new FacultadService(dao);
            facultadController = new FacultadController(service);
        }
        return facultadController;
    }

    /**
     * Garantiza que existan registros mínimos para operar la UI (Persona/Facultad/Programa).
     */
    public static void ensureSeedForUi() {
        // Persona decano
        PersonaController pCtrl = personaController();
        java.util.List<PersonaDTO> personas = pCtrl.listar();
        PersonaDTO decano;
        if (personas.isEmpty()) {
            decano = new PersonaDTO(9001.0, "Decano UI", "Demo", "decano.ui@demo");
            pCtrl.insertar(decano);
        } else {
            decano = personas.get(0);
        }

        // Facultad
        FacultadController fCtrl = facultadController();
        java.util.List<FacultadDTO> facultades = fCtrl.listar();
        FacultadDTO fac;
        if (facultades.isEmpty()) {
            fac = new FacultadDTO(8001.0, "Facultad UI", decano);
            fCtrl.insertar(fac);
        } else {
            fac = facultades.get(0);
        }

        // Programa
        ProgramaController prgCtrl = programaController();
        java.util.List<ProgramaDTO> programas = prgCtrl.listar();
        if (programas.isEmpty()) {
            ProgramaDTO pr = new ProgramaDTO(7001.0, "Programa UI", 8.0, new Date(), fac);
            prgCtrl.insertar(pr);
        }
    }
}
