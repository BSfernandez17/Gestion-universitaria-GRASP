package com.mycompany.app.view;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

// NUEVO: importamos la vista de estudiantes
import com.mycompany.app.view.EstudiantesView;

public class MainMenuView {
    public void show(Stage stage) {
        // Botón para ir a Estudiantes
        Button estudiantesBtn = new Button("Gestión de Estudiantes");
        estudiantesBtn.setOnAction(e -> {
            EstudiantesView estudiantesView = new EstudiantesView();
            estudiantesView.show(stage); // abrimos la vista de estudiantes
        });

        Button profesoresBtn = new Button("Gestión de Profesores");
        Button cursosBtn = new Button("Gestión de Cursos");
        Button inscripcionesBtn = new Button("Gestión de Inscripciones");
        Button programasBtn = new Button("Gestión de Programas");
        Button facultadesBtn = new Button("Gestión de Facultades");

        // VBox con todos los botones
        VBox vbox = new VBox(15, estudiantesBtn, profesoresBtn, cursosBtn, inscripcionesBtn, programasBtn, facultadesBtn);
        vbox.setAlignment(Pos.CENTER);
        vbox.setStyle("-fx-padding: 40;");

        Scene scene = new Scene(vbox, 400, 400);
        stage.setTitle("Menú Principal - Gestión Universitaria");
        stage.setScene(scene);
        stage.show();
    }
}
