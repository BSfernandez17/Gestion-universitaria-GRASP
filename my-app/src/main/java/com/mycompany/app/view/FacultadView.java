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
// FacultadDAO not yet used in this placeholder view

public class FacultadView {
    private static final Logger LOGGER = Logger.getLogger(FacultadView.class.getName());
    private TableView<com.mycompany.app.Model.Facultad> table;
    private TextField nombreField;
    private ComboBox<com.mycompany.app.Model.Persona> decanoBox;
    private com.mycompany.app.Persistence.DAO.FacultadDAO facultadDAO;

    public void show(Stage stage) {
        try (Connection conn = ConnectionDb.getConnection()) {
            facultadDAO = new com.mycompany.app.Persistence.DAO.FacultadDAO(conn);

            Label title = new Label("Gestión de Facultades"); title.getStyleClass().add("title-label");

            TableColumn<com.mycompany.app.Model.Facultad, Double> idCol = new TableColumn<>("ID");
            idCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getID()));
            TableColumn<com.mycompany.app.Model.Facultad, String> nombreCol = new TableColumn<>("Nombre");
            nombreCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));
            TableColumn<com.mycompany.app.Model.Facultad, String> decanoCol = new TableColumn<>("Decano");
            decanoCol.setCellValueFactory(cell -> {
                com.mycompany.app.Model.Persona p = cell.getValue().getDecano();
                return new javafx.beans.property.ReadOnlyStringWrapper(p == null ? "-" : p.getNombres() + " " + p.getApellidos());
            });

            table = new TableView<>();
            table.getColumns().addAll(idCol, nombreCol, decanoCol);
            table.setColumnResizePolicy(param -> true);

            nombreField = new TextField(); nombreField.setPromptText("Nombre");
            decanoBox = new ComboBox<>(); decanoBox.setPrefWidth(260);
            decanoBox.setConverter(new javafx.util.StringConverter<com.mycompany.app.Model.Persona>() {
                @Override public String toString(com.mycompany.app.Model.Persona p) { return p == null ? "" : p.getNombres() + " " + p.getApellidos(); }
                @Override public com.mycompany.app.Model.Persona fromString(String s) { return null; }
            });

            GridPane form = new GridPane(); form.setHgap(10); form.setVgap(8);
            form.add(new Label("Nombre:"), 0, 0); form.add(nombreField, 1, 0);
            form.add(new Label("Decano:"), 0, 1); form.add(decanoBox, 1, 1);

            Button add = new Button("Agregar"); Button upd = new Button("Actualizar"); Button del = new Button("Eliminar"); Button back = new Button("Volver");
            add.getStyleClass().addAll("menu-button"); upd.getStyleClass().addAll("menu-button"); del.getStyleClass().addAll("menu-button"); back.getStyleClass().addAll("menu-button");
            add.setOnAction(e -> onAdd()); upd.setOnAction(e -> onUpdate()); del.setOnAction(e -> onDelete()); back.setOnAction(e -> new MainMenuView().show(stage));

            HBox botones = new HBox(8, add, upd, del, back); botones.setAlignment(Pos.CENTER_LEFT); botones.setPadding(new Insets(6,0,0,0));

            VBox root = new VBox(12, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER); root.setPadding(new Insets(18)); root.getStyleClass().add("menu-container");

            // load data
            com.mycompany.app.Persistence.DAO.PersonaDAO personaDAO = new com.mycompany.app.Persistence.DAO.PersonaDAO(conn);
            decanoBox.getItems().setAll(personaDAO.listar());
            table.getItems().setAll(facultadDAO.listar());

            // double click to edit
            table.setRowFactory(tv -> {
                TableRow<com.mycompany.app.Model.Facultad> row = new TableRow<>();
                row.setOnMouseClicked(evt -> {
                    if (!row.isEmpty() && evt.getClickCount() == 2 && evt.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                        com.mycompany.app.Model.Facultad f = row.getItem();
                        nombreField.setText(f.getNombre());
                        decanoBox.setValue(f.getDecano());
                    }
                });
                return row;
            });

            Scene scene = new Scene(root, 820, 520); java.net.URL cssUrl = FacultadView.class.getResource("/styles/main.css"); if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());
            stage.setTitle("Gestión de Facultades"); stage.setScene(scene); stage.show();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error FacultadView", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onAdd() {
        if (nombreField.getText().trim().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nombre obligatorio").showAndWait(); return; }
        try {
            com.mycompany.app.DTO.PersonaDTO decDto = decanoBox.getValue() == null ? null : new com.mycompany.app.DTO.PersonaDTO(decanoBox.getValue().getID(), decanoBox.getValue().getNombres(), decanoBox.getValue().getApellidos(), decanoBox.getValue().getEmail());
            com.mycompany.app.DTO.FacultadDTO dto = new com.mycompany.app.DTO.FacultadDTO(null, nombreField.getText().trim(), decDto);
            facultadDAO.insertar(dto);
            table.getItems().setAll(facultadDAO.listar()); clearForm(); new Alert(Alert.AlertType.INFORMATION, "Facultad agregada").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error agregar facultad", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onUpdate() {
        com.mycompany.app.Model.Facultad sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona una facultad").showAndWait(); return; }
        try {
            com.mycompany.app.DTO.PersonaDTO decDto = decanoBox.getValue() == null ? null : new com.mycompany.app.DTO.PersonaDTO(decanoBox.getValue().getID(), decanoBox.getValue().getNombres(), decanoBox.getValue().getApellidos(), decanoBox.getValue().getEmail());
            com.mycompany.app.DTO.FacultadDTO dto = new com.mycompany.app.DTO.FacultadDTO(sel.getID(), nombreField.getText().trim(), decDto);
            facultadDAO.actualizar(dto);
            table.getItems().setAll(facultadDAO.listar()); clearForm(); new Alert(Alert.AlertType.INFORMATION, "Facultad actualizada").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error actualizar facultad", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onDelete() {
        com.mycompany.app.Model.Facultad sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona una facultad").showAndWait(); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar?", ButtonType.YES, ButtonType.NO); java.util.Optional<ButtonType> r = c.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) { try { facultadDAO.eliminar(sel.getID()); table.getItems().setAll(facultadDAO.listar()); new Alert(Alert.AlertType.INFORMATION, "Facultad eliminada").showAndWait(); } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error eliminar facultad", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); } }
    }

    private void clearForm() { nombreField.clear(); decanoBox.setValue(null); }
}
