package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

// NUEVO: importamos la vista de estudiantes
// import kept implicit via same package; no explicit import needed

public class MainMenuView {
    public void show(Stage stage) {
        // Encabezado
        Label header = new Label("Sistema de Gestión Universitaria");
        header.setFont(Font.font("System", FontWeight.BOLD, 20));
        header.setTextFill(Color.web("#2c3e50"));
        header.setPadding(new Insets(10, 0, 10, 0));

        // Botón para ir a Estudiantes
        Button estudiantesBtn = new Button("📚 Estudiantes");
        estudiantesBtn.setOnAction(e -> {
            EstudiantesView estudiantesView = new EstudiantesView();
            estudiantesView.show(stage); // abrimos la vista de estudiantes
        });
        Button profesoresBtn = new Button("👩‍🏫 Profesores");
        profesoresBtn.setOnAction(e -> {
            ProfesoresView profesoresView = new ProfesoresView();
            profesoresView.show(stage);
        });
        Button cursosBtn = new Button("🧾 Cursos");
        cursosBtn.setOnAction(e -> {
            CursosView cursosView = new CursosView();
            cursosView.show(stage);
        });
        Button inscripcionesBtn = new Button("📝 Inscripciones");
        inscripcionesBtn.setOnAction(e -> {
            InscripcionesView inscripcionesView = new InscripcionesView();
            inscripcionesView.show(stage);
        });
        Button programasBtn = new Button("🎓 Programas");
        programasBtn.setOnAction(e -> {
            ProgramasView programasView = new ProgramasView();
            programasView.show(stage);
        });
        Button facultadesBtn = new Button("🏫 Facultades");
        facultadesBtn.setOnAction(e -> {
            FacultadesView facultadesView = new FacultadesView();
            facultadesView.show(stage);
        });
        Button cursoProfesorBtn = new Button("🔗 Cursos-Profesores");
        cursoProfesorBtn.setOnAction(e -> {
            CursoProfesoresView view = new CursoProfesoresView();
            view.show(stage);
        });
        // Estilo común a botones
        String buttonStyle = "-fx-font-size: 14px; -fx-padding: 12 18 12 18; -fx-background-radius: 8; -fx-background-color: #3498db; -fx-text-fill: white;";
        String hoverStyle = "-fx-background-color: #2980b9;";
        Button[] buttons = {estudiantesBtn, profesoresBtn, cursosBtn, inscripcionesBtn, programasBtn, facultadesBtn, cursoProfesorBtn};
        for (Button b : buttons) {
            // asegurar ancho consistente para mejor apariencia
            b.setMinWidth(260);
            b.setStyle(buttonStyle);
            b.setOnMouseEntered(ev -> b.setStyle(buttonStyle + hoverStyle));
            b.setOnMouseExited(ev -> b.setStyle(buttonStyle));
        }

        // Layout en grid para mejor distribución
        GridPane grid = new GridPane();
        grid.setHgap(24);
        grid.setVgap(20);
        grid.setPadding(new Insets(32));
        grid.setAlignment(Pos.CENTER);

    ColumnConstraints col = new ColumnConstraints();
        col.setPercentWidth(50);
        grid.getColumnConstraints().addAll(col, col);
    grid.add(estudiantesBtn, 0, 0);
    grid.add(profesoresBtn, 1, 0);
    grid.add(cursosBtn, 0, 1);
    grid.add(inscripcionesBtn, 1, 1);
    grid.add(programasBtn, 0, 2);
    grid.add(facultadesBtn, 1, 2);
    // Añadir márgenes consistentes alrededor de cada botón
        // Añadir márgenes consistentes alrededor de cada botón iterando los nodos
        for (javafx.scene.Node node : grid.getChildren()) {
            Integer row = GridPane.getRowIndex(node);
            Integer colIndex = GridPane.getColumnIndex(node);
            // solo para las celdas definidas en filas 0..2 y columnas 0..1
            if (row != null && colIndex != null && row >= 0 && row <= 2 && colIndex >= 0 && colIndex <= 1) {
                GridPane.setMargin(node, new Insets(8, 8, 8, 8));
            }
        }
        // cursoProfesor ocupa toda la fila
    HBox bottom = new HBox(cursoProfesorBtn);
    bottom.setAlignment(Pos.CENTER);
    bottom.setPadding(new Insets(12, 0, 0, 0));
    GridPane.setMargin(bottom, new Insets(10, 0, 0, 0));
    grid.add(bottom, 0, 3, 2, 1);

    VBox root = new VBox(18, header, grid);
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(20));

    Scene scene = new Scene(root, 760, 520);
        stage.setTitle("Menú Principal - Gestión Universitaria");
        stage.setScene(scene);
        stage.show();
    }
}
