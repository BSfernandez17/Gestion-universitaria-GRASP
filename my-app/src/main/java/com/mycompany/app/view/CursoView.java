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
import com.mycompany.app.Model.Curso;
import com.mycompany.app.Model.Programa;
import com.mycompany.app.Persistence.DAO.CursoDAO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.DTO.CursoDTO;

public class CursoView {
    private static final Logger LOGGER = Logger.getLogger(CursoView.class.getName());
    private TableView<Curso> table;
    private TextField nombreField;
    private ComboBox<Programa> programaBox;
    private CursoDAO cursoDAO;

    public void show(Stage stage) {
        try (Connection conn = ConnectionDb.getConnection()) {
            cursoDAO = new CursoDAO(conn);

            Label title = new Label("Gestión de Cursos");
            title.getStyleClass().add("title-label");

            TableColumn<Curso, Double> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getID()));

            TableColumn<Curso, String> nombreCol = new TableColumn<>("Nombre");
            nombreCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));

            TableColumn<Curso, String> programaCol = new TableColumn<>("Programa");
            programaCol.setCellValueFactory(cell -> {
                Programa p = cell.getValue().getPrograma();
                String name = p == null ? "-" : p.getNombre();
                return new javafx.beans.property.ReadOnlyStringWrapper(name);
            });

            table = new TableView<>();
            table.getColumns().addAll(idCol, nombreCol, programaCol);
            table.setColumnResizePolicy(param -> true);

            // form
            nombreField = new TextField();
            nombreField.setPromptText("Nombre");
            programaBox = new ComboBox<>();
            programaBox.setPrefWidth(220);
            programaBox.setConverter(new javafx.util.StringConverter<Programa>() {
                @Override
                public String toString(Programa p) { return p == null ? "" : p.getNombre(); }
                @Override
                public Programa fromString(String s) { return null; }
            });

            GridPane form = new GridPane();
            form.setHgap(10); form.setVgap(8);
            form.add(new Label("Nombre:"), 0, 0); form.add(nombreField, 1, 0);
            form.add(new Label("Programa:"), 0, 1); form.add(programaBox, 1, 1);

            Button addBtn = new Button("Agregar");
            Button updateBtn = new Button("Actualizar");
            Button deleteBtn = new Button("Eliminar");
            Button backBtn = new Button("Volver");

            addBtn.getStyleClass().add("menu-button"); updateBtn.getStyleClass().add("menu-button"); deleteBtn.getStyleClass().add("menu-button"); backBtn.getStyleClass().add("menu-button");

            addBtn.setOnAction(e -> onAdd());
            updateBtn.setOnAction(e -> onUpdate());
            deleteBtn.setOnAction(e -> onDelete());
            backBtn.setOnAction(e -> new MainMenuView().show(stage));

            HBox botones = new HBox(8, addBtn, updateBtn, deleteBtn, backBtn);
            botones.setAlignment(Pos.CENTER_LEFT);

            VBox root = new VBox(12, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER); root.setPadding(new Insets(18)); root.getStyleClass().add("menu-container");

            // load programas and cursos
            ProgramaDAO programaDAO = new ProgramaDAO(conn);
            programaBox.getItems().setAll(programaDAO.listar());
            table.getItems().setAll(cursoDAO.listar());

            Scene scene = new Scene(root, 760, 520);
            java.net.URL cssUrl = CursoView.class.getResource("/styles/main.css"); if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("Gestión de Cursos"); stage.setScene(scene); stage.show();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error CursoView", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onAdd() {
        if (nombreField.getText().trim().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nombre es obligatorio").showAndWait(); return; }
        try (Connection conn = ConnectionDb.getConnection()) {
            Programa selected = programaBox.getValue();
            com.mycompany.app.DTO.ProgramaDTO progDto = selected == null ? null : new com.mycompany.app.DTO.ProgramaDTO(selected.getID(), selected.getNombre(), selected.getDuracion(), selected.getRegistro(), null);
            CursoDTO dto = new CursoDTO(null, nombreField.getText().trim(), progDto, true);
            cursoDAO.insertar(dto);
            table.getItems().setAll(cursoDAO.listar());
            clearForm();
            new Alert(Alert.AlertType.INFORMATION, "Curso agregado").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al agregar curso", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onUpdate() {
        Curso sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un curso").showAndWait(); return; }
        try {
            Programa selected = programaBox.getValue();
            com.mycompany.app.DTO.ProgramaDTO progDto = selected == null ? null : new com.mycompany.app.DTO.ProgramaDTO(selected.getID(), selected.getNombre(), selected.getDuracion(), selected.getRegistro(), null);
            CursoDTO dto = new CursoDTO(sel.getID(), nombreField.getText().trim(), progDto, true);
            cursoDAO.actualizar(dto);
            table.getItems().setAll(cursoDAO.listar());
            clearForm();
            new Alert(Alert.AlertType.INFORMATION, "Curso actualizado").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al actualizar curso", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onDelete() {
        Curso sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un curso").showAndWait(); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar?", ButtonType.YES, ButtonType.NO);
        java.util.Optional<ButtonType> r = c.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            try { cursoDAO.eliminar(sel.getID().intValue()); table.getItems().setAll(cursoDAO.listar()); new Alert(Alert.AlertType.INFORMATION, "Curso eliminado").showAndWait(); } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al eliminar curso", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
        }
    }

    private void clearForm() { nombreField.clear(); programaBox.setValue(null); }
}
