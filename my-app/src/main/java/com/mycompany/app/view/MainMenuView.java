package com.mycompany.app.view;

import javafx.geometry.Pos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.input.KeyCombination;

import com.mycompany.app.view.EstudiantesView;
import com.mycompany.app.view.PersonaView;

public class MainMenuView {
    public void show(Stage stage) {
    // entering MainMenuView.show()
    // Header
    Label title = new Label("Gestión Universitaria");
    title.getStyleClass().add("title-label");

    // Card container for main content
    VBox card = new VBox();
    card.getStyleClass().add("menu-card");
    card.setPadding(new Insets(24));
    card.setSpacing(16);
    card.setMaxWidth(720);
    card.setStyle("-fx-background-radius: 10; -fx-background-color: rgba(255,255,255,0.9); -fx-effect: dropshadow(one-pass-box, rgba(0,0,0,0.08), 8, 0, 0, 4);");

    // Botones con atajos de teclado y tooltips
    Button estudiantesBtn = createButton("_Gestión de Estudiantes");
        estudiantesBtn.setOnAction(e -> new EstudiantesView().show(stage));
        estudiantesBtn.setMnemonicParsing(true);
        estudiantesBtn.setTooltip(new javafx.scene.control.Tooltip("Alt+E - Ir a Gestión de Estudiantes"));

    Button profesoresBtn = createButton("Gestión de Profesores");
    profesoresBtn.setOnAction(e -> new ProfesorView().show(stage));

        Button personasBtn = createButton("_Gestión de Personas");
        personasBtn.setOnAction(e -> new PersonaView().show(stage));
        personasBtn.setMnemonicParsing(true);
        personasBtn.setTooltip(new javafx.scene.control.Tooltip("Alt+P - Ir a Gestión de Personas"));

    Button cursosBtn = createButton("Gestión de Cursos");
    cursosBtn.setOnAction(e -> new CursoView().show(stage));

    Button inscripcionesBtn = createButton("Gestión de Inscripciones");
    inscripcionesBtn.setOnAction(e -> new InscripcionView().show(stage));

    Button programasBtn = createButton("Gestión de Programas");
    programasBtn.setOnAction(e -> new ProgramaView().show(stage));

    Button facultadesBtn = createButton("Gestión de Facultades");
    facultadesBtn.setOnAction(e -> new FacultadView().show(stage));

        // Agrupar botones en dos columnas
        VBox col1 = new VBox(12, estudiantesBtn, profesoresBtn, personasBtn);
        VBox col2 = new VBox(12, cursosBtn, inscripcionesBtn, programasBtn, facultadesBtn);
        col1.setAlignment(Pos.CENTER);
        col2.setAlignment(Pos.CENTER);

    HBox buttonsRow = new HBox(28, col1, col2);
    buttonsRow.setAlignment(Pos.CENTER);

    Label footer = new Label("v1.0 - Gestión Universitaria");
    footer.getStyleClass().add("menu-footer");

    // Assemble card
    card.getChildren().addAll(title, buttonsRow, footer);

    // Layout principal con menú y toolbar
    BorderPane root = new BorderPane();
    root.setTop(new VBox(createMenuBar(), createToolBar()));

    StackPane center = new StackPane();
    center.getChildren().add(card);
    center.setPadding(new Insets(36));
    center.getStyleClass().add("menu-container");
    root.setCenter(center);

    // small footer at bottom
    HBox bottom = new HBox(footer);
    bottom.setAlignment(Pos.CENTER_RIGHT);
    bottom.setPadding(new Insets(6, 16, 12, 16));
    root.setBottom(bottom);

    Scene scene = new Scene(root, 880, 560);
        // Aplicar stylesheet
        java.net.URL cssUrl = MainMenuView.class.getResource("/styles/main.css");
        if (cssUrl != null) {
            scene.getStylesheets().add(cssUrl.toExternalForm());
        }

        stage.setTitle("Menú Principal - Gestión Universitaria");
        stage.setScene(scene);
        stage.show();
    }

    private Button createButton(String text) {
        Button button = new Button(text);
        button.setPrefWidth(260); // Ancho unificado para todos los botones
        button.setPrefHeight(40); // Altura consistente
        button.getStyleClass().add("menu-button");
        return button;
    }

    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        // Menú Archivo
        Menu fileMenu = new Menu("Archivo");
        MenuItem exitItem = new MenuItem("Salir");
        exitItem.setAccelerator(KeyCombination.keyCombination("Ctrl+Q"));
        exitItem.setOnAction(e -> System.exit(0));
        fileMenu.getItems().add(exitItem);

        // Menú Ver
        Menu viewMenu = new Menu("Ver");
        MenuItem refreshItem = new MenuItem("Actualizar");
        refreshItem.setAccelerator(KeyCombination.keyCombination("F5"));
        viewMenu.getItems().add(refreshItem);

        // Menú Ayuda
        Menu helpMenu = new Menu("Ayuda");
        MenuItem shortcutsItem = new MenuItem("Atajos de teclado");
        MenuItem aboutItem = new MenuItem("Acerca de");
        helpMenu.getItems().addAll(shortcutsItem, aboutItem);

        menuBar.getMenus().addAll(fileMenu, viewMenu, helpMenu);
        return menuBar;
    }

    private ToolBar createToolBar() {
        ToolBar toolBar = new ToolBar();

        Button refreshBtn = new Button("Actualizar");
        refreshBtn.setTooltip(new Tooltip("Actualizar vista (F5)"));

        Separator separator = new Separator();

        ComboBox<String> viewSelector = new ComboBox<>();
        viewSelector.getItems().addAll(
            "Vista Principal",
            "Estudiantes",
            "Profesores",
            "Personas",
            "Cursos"
        );
        viewSelector.setValue("Vista Principal");
        viewSelector.setTooltip(new Tooltip("Cambiar vista actual"));

        toolBar.getItems().addAll(refreshBtn, separator, viewSelector);
        return toolBar;
    }
}
