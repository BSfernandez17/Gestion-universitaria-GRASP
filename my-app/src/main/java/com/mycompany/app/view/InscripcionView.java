package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.app.ConnectionDb;
// InscripcionDAO not yet used in this placeholder view

public class InscripcionView {
    private static final Logger LOGGER = Logger.getLogger(InscripcionView.class.getName());
    private TableView<com.mycompany.app.Model.Inscripcion> table;
    private ComboBox<com.mycompany.app.Model.Curso> cursoBox;
    private ComboBox<com.mycompany.app.Model.Estudiante> estudianteBox;
    private TextField añoField, semestreField;
    private com.mycompany.app.Persistence.DAO.InscripcionDAO inscripcionDAO;

    public void show(Stage stage) {
        Connection conn = ConnectionDb.getConnection();
        try {
            Label title = new Label("Gestión de Inscripciones"); title.getStyleClass().add("title-label");

            TableColumn<com.mycompany.app.Model.Inscripcion, Double> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getID()));
            TableColumn<com.mycompany.app.Model.Inscripcion, String> cursoCol = new TableColumn<>("Curso");
            cursoCol.setCellValueFactory(cell -> {
                com.mycompany.app.Model.Curso c = cell.getValue().getCurso();
                return new javafx.beans.property.ReadOnlyStringWrapper(c == null ? "-" : c.getNombre());
            });
            TableColumn<com.mycompany.app.Model.Inscripcion, String> estudianteCol = new TableColumn<>("Estudiante");
            estudianteCol.setCellValueFactory(cell -> {
                com.mycompany.app.Model.Estudiante e = cell.getValue().getEstudiante();
                return new javafx.beans.property.ReadOnlyStringWrapper(e == null ? "-" : e.getNombres() + " " + e.getApellidos());
            });
            TableColumn<com.mycompany.app.Model.Inscripcion, Integer> añoCol = new TableColumn<>("Año");
            añoCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Integer>(c.getValue().getAño()));
            TableColumn<com.mycompany.app.Model.Inscripcion, Integer> semCol = new TableColumn<>("Semestre");
            semCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Integer>(c.getValue().getSemestre()));

            table = new TableView<>();
            table.getColumns().addAll(idCol, cursoCol, estudianteCol, añoCol, semCol);
            table.setColumnResizePolicy(param -> true);

            cursoBox = new ComboBox<>(); estudianteBox = new ComboBox<>(); añoField = new TextField(); semestreField = new TextField();
            cursoBox.setPrefWidth(260); estudianteBox.setPrefWidth(260); añoField.setPromptText("Año"); semestreField.setPromptText("Semestre");

            GridPane form = new GridPane(); form.setHgap(10); form.setVgap(8);
            form.add(new Label("Curso:"), 0, 0); form.add(cursoBox, 1, 0);
            form.add(new Label("Estudiante:"), 0, 1); form.add(estudianteBox, 1, 1);
            form.add(new Label("Año:"), 2, 0); form.add(añoField, 3, 0);
            form.add(new Label("Semestre:"), 2, 1); form.add(semestreField, 3, 1);

            Button add = new Button("Agregar"); Button del = new Button("Eliminar"); Button back = new Button("Volver");
            add.getStyleClass().add("menu-button"); del.getStyleClass().add("menu-button"); back.getStyleClass().add("menu-button");

            add.setOnAction(e -> onAdd()); del.setOnAction(e -> onDelete()); back.setOnAction(e -> new MainMenuView().show(stage));

            HBox botones = new HBox(8, add, del, back); botones.setAlignment(Pos.CENTER_LEFT);

            VBox root = new VBox(12, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER); root.setPadding(new Insets(18)); root.getStyleClass().add("menu-container");

            // load DAOs and data
            inscripcionDAO = new com.mycompany.app.Persistence.DAO.InscripcionDAO(conn);
            com.mycompany.app.Persistence.DAO.CursoDAO cursoDAO = new com.mycompany.app.Persistence.DAO.CursoDAO(conn);
            com.mycompany.app.Persistence.DAO.EstudianteDAO estudianteDAO = new com.mycompany.app.Persistence.DAO.EstudianteDAO(conn);
            cursoBox.getItems().setAll(cursoDAO.listar());
            estudianteBox.getItems().setAll(estudianteDAO.listar());
            table.getItems().setAll(inscripcionDAO.listar());

            Scene scene = new Scene(root, 920, 520); java.net.URL cssUrl = InscripcionView.class.getResource("/styles/main.css"); if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            stage.setTitle("Gestión de Inscripciones"); stage.setScene(scene); stage.show();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error InscripcionView", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onAdd() {
        if (cursoBox.getValue() == null || estudianteBox.getValue() == null) { new Alert(Alert.AlertType.WARNING, "Seleccione curso y estudiante").showAndWait(); return; }
        try {
            com.mycompany.app.DTO.CursoDTO cursoDto = new com.mycompany.app.DTO.CursoDTO(cursoBox.getValue().getID(), cursoBox.getValue().getNombre(), null, cursoBox.getValue().getActivo());
            com.mycompany.app.DTO.EstudianteDTO estDto = new com.mycompany.app.DTO.EstudianteDTO(estudianteBox.getValue().getID(), estudianteBox.getValue().getNombres(), estudianteBox.getValue().getApellidos(), estudianteBox.getValue().getEmail(), estudianteBox.getValue().getCodigo(), null, estudianteBox.getValue().getActivo(), estudianteBox.getValue().getPromedio());
            Integer año = Integer.parseInt(añoField.getText().trim());
            Integer semestre = Integer.parseInt(semestreField.getText().trim());
            com.mycompany.app.DTO.InscripcionDTO dto = new com.mycompany.app.DTO.InscripcionDTO(null, cursoDto, año, semestre, estDto);
            inscripcionDAO.insertar(dto);
            table.getItems().setAll(inscripcionDAO.listar()); clearForm(); new Alert(Alert.AlertType.INFORMATION, "Inscripción agregada").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error agregar inscripcion", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onDelete() {
        com.mycompany.app.Model.Inscripcion sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona una inscripción").showAndWait(); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar?", ButtonType.YES, ButtonType.NO); java.util.Optional<ButtonType> r = c.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) { try { inscripcionDAO.eliminar(sel.getID()); table.getItems().setAll(inscripcionDAO.listar()); new Alert(Alert.AlertType.INFORMATION, "Inscripción eliminada").showAndWait(); } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error eliminar inscripcion", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); } }
    }

    private void clearForm() { añoField.clear(); semestreField.clear(); cursoBox.setValue(null); estudianteBox.setValue(null); }
}
