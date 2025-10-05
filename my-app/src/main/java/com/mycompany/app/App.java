package com.mycompany.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import com.mycompany.app.Controller.CursoProfesorController;
import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.Controller.ServicesClass.PersonaService;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.seeders.DatabaseSeed;
import com.mycompany.app.Controller.PersonaController;
;
public class App {
  public static void main(String[] args) {
    Connection conn = ConnectionDb.getConnection();
    PersonaDAO personaDAO = new PersonaDAO(conn);
    PersonaService personaService = new PersonaService(personaDAO);
    PersonaController personaController = new PersonaController(personaService);
    System.out.println(personaController.listar()); 

    CursoProfesorDAO cursoProfesorDAO = new CursoProfesorDAO(conn);
    CursoProfesorService cursoProfesorService = new CursoProfesorService(cursoProfesorDAO);
    CursoProfesorController cursoProfesorController = new CursoProfesorController(cursoProfesorService);
    System.out.println(cursoProfesorController.listar());
    // 🔗 Establecer la conexión a la base de datos
        Connection con = ConnectionDb.getConnection();

        // 🌱 Ejecutar el seed UNA SOLA VEZ (inserta datos si las tablas están vacías)
        DatabaseSeed.seed(conn);

        // 🚀 Lanzar la aplicación JavaFX
        MainFX.main(args);

    
      
  }
  
}
