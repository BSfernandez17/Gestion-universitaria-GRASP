package com.mycompany.app;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Properties;

import com.mycompany.app.Controller.CursoProfesorController;
import com.mycompany.app.Controller.ServicesClass.CursoProfesorService;
import com.mycompany.app.Persistence.DAO.CursoProfesorDAO;

public class App {
  public static void main(String[] args) {
    Connection conn = ConnectionDb.getConnection();
    CursoProfesorDAO cursoProfesorDAO = new CursoProfesorDAO(conn);
    CursoProfesorService cursoProfesorService = new CursoProfesorService(cursoProfesorDAO);
    CursoProfesorController cursoProfesorController = new CursoProfesorController(cursoProfesorService);

  }
}
