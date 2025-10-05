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

import com.mycompany.app.Model.Profesor;
import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Persistence.DAO.ProfesorDAO;
import com.mycompany.app.DTO.ProfesorDTO;

public class ProfesorView {
    private static final Logger LOGGER = Logger.getLogger(ProfesorView.class.getName());
    private TableView<Profesor> table;
    private TextField nombresField, apellidosField, emailField, tipoContratoField;
    private ProfesorDAO profesorDAO;

    public void show(Stage stage) {
        Connection conn = ConnectionDb.getConnection();
        try {
            profesorDAO = new ProfesorDAO(conn);
            Label title = new Label("Gestión de Profesores");
            title.getStyleClass().add("title-label");

            table = createTable();
            reloadTable();

            GridPane form = createForm();
            HBox botones = createButtons(stage);

            VBox root = new VBox(12, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(18));
            root.getStyleClass().add("menu-container");

            Scene scene = new Scene(root, 760, 520);
            java.net.URL cssUrl = ProfesorView.class.getResource("/styles/main.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("Gestión de Profesores");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al inicializar ProfesorView", ex);
            new Alert(Alert.AlertType.ERROR, "Error al conectar con la BD: " + ex.getMessage()).showAndWait();
        }
    }

    private TableView<Profesor> createTable() {
        TableView<Profesor> tv = new TableView<>();
        TableColumn<Profesor, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getID()));
        TableColumn<Profesor, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombres"));
        TableColumn<Profesor, String> apellidosCol = new TableColumn<>("Apellidos");
        apellidosCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("apellidos"));
        TableColumn<Profesor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("email"));
        TableColumn<Profesor, String> tipoCol = new TableColumn<>("Contrato");
        tipoCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<String>(c.getValue().getTipoContrato()));

        java.util.List<TableColumn<Profesor, ?>> cols = new java.util.ArrayList<>();
        cols.add(idCol); cols.add(nombresCol); cols.add(apellidosCol); cols.add(emailCol); cols.add(tipoCol);
        tv.getColumns().addAll(cols);
        tv.setColumnResizePolicy(param -> true);
        return tv;
    }

    private GridPane createForm() {
        nombresField = new TextField(); apellidosField = new TextField(); emailField = new TextField(); tipoContratoField = new TextField();
        nombresField.setPromptText("Nombres"); apellidosField.setPromptText("Apellidos"); emailField.setPromptText("Email"); tipoContratoField.setPromptText("Tipo de contrato");
        GridPane g = new GridPane(); g.setHgap(10); g.setVgap(8);
        g.add(new Label("Nombres:"), 0, 0); g.add(nombresField, 1, 0);
        g.add(new Label("Apellidos:"), 0, 1); g.add(apellidosField, 1, 1);
        g.add(new Label("Email:"), 2, 0); g.add(emailField, 3, 0);
        g.add(new Label("Contrato:"), 2, 1); g.add(tipoContratoField, 3, 1);
        return g;
    }

    private HBox createButtons(Stage stage) {
        Button add = new Button("Agregar"); Button upd = new Button("Actualizar"); Button del = new Button("Eliminar"); Button back = new Button("Volver");
        add.getStyleClass().add("menu-button"); upd.getStyleClass().add("menu-button"); del.getStyleClass().add("menu-button"); back.getStyleClass().add("menu-button");
        add.setOnAction(e -> onAdd()); upd.setOnAction(e -> onUpdate()); del.setOnAction(e -> onDelete()); back.setOnAction(e -> new MainMenuView().show(stage));
        HBox h = new HBox(8, add, upd, del, back); h.setAlignment(Pos.CENTER_LEFT); h.setPadding(new Insets(6,0,0,0)); return h;
    }

    private void reloadTable() {
        try {
            java.util.List<Profesor> list = profesorDAO.listar();
            table.getItems().setAll(list);
            LOGGER.log(Level.INFO, "Profesores cargados: {0}", list.size());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudieron recargar profesores", ex);
        }
    }

    private void onAdd() {
        if (nombresField.getText().trim().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nombres obligatorios").showAndWait(); return; }
        try {
            ProfesorDTO dto = new ProfesorDTO(null, nombresField.getText().trim(), apellidosField.getText().trim(), emailField.getText().trim(), tipoContratoField.getText().trim());
            profesorDAO.insertar(dto); reloadTable(); new Alert(Alert.AlertType.INFORMATION, "Profesor agregado").showAndWait();
        } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al agregar profesor", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onUpdate() {
        Profesor sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un profesor").showAndWait(); return; }
        try { ProfesorDTO dto = new ProfesorDTO(sel.getID(), nombresField.getText().trim(), apellidosField.getText().trim(), emailField.getText().trim(), tipoContratoField.getText().trim()); profesorDAO.actualizar(dto); reloadTable(); new Alert(Alert.AlertType.INFORMATION, "Profesor actualizado").showAndWait(); } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al actualizar profesor", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
    }

    private void onDelete() { Profesor sel = table.getSelectionModel().getSelectedItem(); if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un profesor").showAndWait(); return; } try { profesorDAO.eliminar(sel.getID()); reloadTable(); new Alert(Alert.AlertType.INFORMATION, "Profesor eliminado").showAndWait(); } catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al eliminar profesor", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); } }
}
