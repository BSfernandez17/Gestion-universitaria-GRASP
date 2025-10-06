package com.mycompany.app.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// NUEVO: importamos la vista de estudiantes
// import kept implicit via same package; no explicit import needed

public class MainMenuView {
    public void show(Stage stage) {
        // Botón para ir a Estudiantes
        Button estudiantesBtn = new Button("Gestión de Estudiantes");
        estudiantesBtn.setOnAction(e -> {
            EstudiantesView estudiantesView = new EstudiantesView();
            estudiantesView.show(stage); // abrimos la vista de estudiantes
        });

        Button profesoresBtn = new Button("Gestión de Profesores");
        profesoresBtn.setOnAction(e -> {
            ProfesoresView profesoresView = new ProfesoresView();
            profesoresView.show(stage);
        });
        Button cursosBtn = new Button("Gestión de Cursos");
        cursosBtn.setOnAction(e -> {
            CursosView cursosView = new CursosView();
            cursosView.show(stage);
        });
        Button inscripcionesBtn = new Button("Gestión de Inscripciones");
        inscripcionesBtn.setOnAction(e -> {
            InscripcionesView inscripcionesView = new InscripcionesView();
            inscripcionesView.show(stage);
        });
        Button programasBtn = new Button("Gestión de Programas");
        programasBtn.setOnAction(e -> {
            ProgramasView programasView = new ProgramasView();
            programasView.show(stage);
        });
        Button facultadesBtn = new Button("Gestión de Facultades");
        facultadesBtn.setOnAction(e -> {
            FacultadesView facultadesView = new FacultadesView();
            facultadesView.show(stage);
        });
        Button cursoProfesorBtn = new Button("Asignar Cursos a Profesores");
        cursoProfesorBtn.setOnAction(e -> {
            CursoProfesoresView view = new CursoProfesoresView();
            view.show(stage);
        });

        // VBox con todos los botones
    VBox vbox = new VBox(15, estudiantesBtn, profesoresBtn, cursosBtn, inscripcionesBtn, programasBtn, facultadesBtn, cursoProfesorBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 40;");

        Scene scene = new Scene(vbox, 400, 400);
        stage.setTitle("Menú Principal - Gestión Universitaria");
        stage.setScene(scene);
        stage.show();
    }
}
