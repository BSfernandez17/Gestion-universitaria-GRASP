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
        // Título
        Label title = new Label("Gestión Universitaria");
        title.getStyleClass().add("title-label");

        // Botones con atajos de teclado y tooltips
        Button estudiantesBtn = createButton("_Gestión de Estudiantes");
        estudiantesBtn.setOnAction(e -> new EstudiantesView().show(stage));
        estudiantesBtn.setMnemonicParsing(true);
        estudiantesBtn.setTooltip(new javafx.scene.control.Tooltip("Alt+E - Ir a Gestión de Estudiantes"));

        Button profesoresBtn = createButton("Gestión de Profesores");
        
        Button personasBtn = createButton("_Gestión de Personas");
        personasBtn.setOnAction(e -> new PersonaView().show(stage));
        personasBtn.setMnemonicParsing(true);
        personasBtn.setTooltip(new javafx.scene.control.Tooltip("Alt+P - Ir a Gestión de Personas"));

        Button cursosBtn = createButton("Gestión de Cursos");
        
        Button inscripcionesBtn = createButton("Gestión de Inscripciones");
        
        Button programasBtn = createButton("Gestión de Programas");
        
        Button facultadesBtn = createButton("Gestión de Facultades");

        // Agrupar botones en dos columnas
        VBox col1 = new VBox(12, estudiantesBtn, profesoresBtn, personasBtn);
        VBox col2 = new VBox(12, cursosBtn, inscripcionesBtn, programasBtn, facultadesBtn);
        col1.setAlignment(Pos.CENTER);
        col2.setAlignment(Pos.CENTER);

        HBox buttonsRow = new HBox(20, col1, col2);
        buttonsRow.setAlignment(Pos.CENTER);

        Label footer = new Label("v1.0 - Gestión Universitaria");
        footer.setStyle("-fx-text-fill: #666; -fx-padding: 10 0 0 0;");

        // Layout principal con menú y toolbar
        VBox vbox = new VBox();
        vbox.setSpacing(0); // Sin espacio entre barras
        
        // Agregar MenuBar y ToolBar
        MenuBar menuBar = createMenuBar();
        ToolBar toolBar = createToolBar();
        
        // Contenedor para el contenido principal
        VBox contentBox = new VBox();
        contentBox.setSpacing(25);
        contentBox.getChildren().addAll(title, buttonsRow, footer);
        contentBox.setAlignment(Pos.CENTER);
        contentBox.getStyleClass().add("menu-container");
        contentBox.setPadding(new Insets(30, 0, 30, 0));
        
        // Agregar todos los componentes
        vbox.getChildren().addAll(menuBar, toolBar, contentBox);

        Scene scene = new Scene(vbox, 760, 460);
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
