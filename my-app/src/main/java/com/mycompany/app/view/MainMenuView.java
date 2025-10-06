package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import javafx.scene.effect.DropShadow;

public class MainMenuView {
    private static final String BUTTON_STYLE = """
            -fx-background-color: white;
            -fx-border-color: #cccccc;
            -fx-border-radius: 5;
            -fx-background-radius: 5;
            -fx-font-size: 14px;
            -fx-padding: 10 20;
            -fx-min-width: 250;
            """;
    
    private static final String BUTTON_HOVER_STYLE = """
            -fx-background-color: #f8f9fa;
            -fx-border-color: #0056b3;
            """;

    public void show(Stage stage) {
        // Contenedor principal
        VBox root = new VBox(20);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(30));
        root.setStyle("-fx-background-color: white;");

        // Header
        Label title = new Label("Sistema de Gestión Universitaria");
        title.setFont(Font.font("System", FontWeight.BOLD, 24));
        title.setTextFill(Color.web("#2c3e50"));
        
        Label subtitle = new Label("Seleccione una opción para continuar");
        subtitle.setFont(Font.font("System", FontWeight.NORMAL, 14));
        subtitle.setTextFill(Color.web("#7f8c8d"));

        VBox header = new VBox(10, title, subtitle);
        header.setAlignment(Pos.CENTER);
        header.setPadding(new Insets(0, 0, 20, 0));

        // Separador
        Separator separator = new Separator();
        separator.setStyle("-fx-background-color: #e0e0e0;");

        // Sección de Personas
        Label personasLabel = new Label("Gestión de Personas");
        personasLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        personasLabel.setTextFill(Color.web("#34495e"));

        Button estudiantesBtn = createMenuButton("👨‍🎓 Gestión de Estudiantes");
        estudiantesBtn.setOnAction(e -> new EstudiantesView().show(stage));

        Button profesoresBtn = createMenuButton("👨‍🏫 Gestión de Profesores");
        profesoresBtn.setOnAction(e -> new ProfesoresView().show(stage));

        VBox personasBox = new VBox(10, personasLabel, estudiantesBtn, profesoresBtn);
        personasBox.setAlignment(Pos.CENTER);

        // Sección Académica
        Label academicaLabel = new Label("Gestión Académica");
        academicaLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        academicaLabel.setTextFill(Color.web("#34495e"));

        Button cursosBtn = createMenuButton("📚 Gestión de Cursos");
        cursosBtn.setOnAction(e -> new CursosView().show(stage));

        Button inscripcionesBtn = createMenuButton("📝 Gestión de Inscripciones");
        inscripcionesBtn.setOnAction(e -> new InscripcionesView().show(stage));

        Button cursoProfesorBtn = createMenuButton("👥 Asignar Cursos a Profesores");
        cursoProfesorBtn.setOnAction(e -> new CursoProfesoresView().show(stage));

        VBox academicaBox = new VBox(10, academicaLabel, cursosBtn, inscripcionesBtn, cursoProfesorBtn);
        academicaBox.setAlignment(Pos.CENTER);

        // Sección Administrativa
        Label adminLabel = new Label("Gestión Administrativa");
        adminLabel.setFont(Font.font("System", FontWeight.BOLD, 16));
        adminLabel.setTextFill(Color.web("#34495e"));

        Button programasBtn = createMenuButton("🎓 Gestión de Programas");
        programasBtn.setOnAction(e -> new ProgramasView().show(stage));

        Button facultadesBtn = createMenuButton("🏛️ Gestión de Facultades");
        facultadesBtn.setOnAction(e -> new FacultadesView().show(stage));

        VBox adminBox = new VBox(10, adminLabel, programasBtn, facultadesBtn);
        adminBox.setAlignment(Pos.CENTER);

        // Añadir todo al contenedor principal
        root.getChildren().addAll(
            header,
            separator,
            personasBox,
            new Separator(),
            academicaBox,
            new Separator(),
            adminBox
        );

        // Configurar la escena
        Scene scene = new Scene(root, 600, 700);
        stage.setTitle("Sistema de Gestión Universitaria");
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
    }

    private Button createMenuButton(String text) {
        Button button = new Button(text);
        button.setStyle(BUTTON_STYLE);
        button.setMaxWidth(Double.MAX_VALUE);
        
        // Efecto de sombra
        DropShadow shadow = new DropShadow();
        shadow.setRadius(5.0);
        shadow.setOffsetY(3.0);
        shadow.setColor(Color.color(0, 0, 0, 0.2));
        button.setEffect(shadow);

        // Eventos de hover
        button.setOnMouseEntered(e -> {
            button.setStyle(BUTTON_STYLE + BUTTON_HOVER_STYLE);
            shadow.setRadius(8.0);
            shadow.setOffsetY(5.0);
        });
        
        button.setOnMouseExited(e -> {
            button.setStyle(BUTTON_STYLE);
            shadow.setRadius(5.0);
            shadow.setOffsetY(3.0);
        });

        return button;
    }
}
