package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.sql.Connection;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.app.Model.Programa;
import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.DTO.ProgramaDTO;

public class ProgramaView {

    private static final Logger LOGGER = Logger.getLogger(ProgramaView.class.getName());
    private TableView<Programa> table;
    private TextField nombreField, duracionField;
    private Button addBtn, updateBtn, deleteBtn, backBtn;
    private ProgramaDAO programaDAO;

    public void show(Stage stage) {
        try (Connection conn = ConnectionDb.getConnection()) {
            programaDAO = new ProgramaDAO(conn);

            Label title = new Label("Gestión de Programas");
            title.getStyleClass().add("title-label");

            table = createTable();
            reloadTable();

            GridPane form = createForm();
            HBox botones = createButtons(stage);

            VBox root = new VBox(12, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(18));
            root.getStyleClass().add("menu-container");

            Scene scene = new Scene(root, 700, 480);
            java.net.URL cssUrl = ProgramaView.class.getResource("/styles/main.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("Gestión de Programas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al inicializar ProgramaView", ex);
            new Alert(Alert.AlertType.ERROR, "Error al conectar con la BD: " + ex.getMessage()).showAndWait();
        }
    }

    private TableView<Programa> createTable() {
        TableView<Programa> tv = new TableView<>();

        TableColumn<Programa, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getID()));

        TableColumn<Programa, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new javafx.scene.control.cell.PropertyValueFactory<>("nombre"));

        TableColumn<Programa, Double> durCol = new TableColumn<>("Duración");
        durCol.setCellValueFactory(c -> new javafx.beans.property.ReadOnlyObjectWrapper<Double>(c.getValue().getDuracion()));

        java.util.List<TableColumn<Programa, ?>> cols = new java.util.ArrayList<>();
        cols.add(idCol); cols.add(nombreCol); cols.add(durCol);
        tv.getColumns().addAll(cols);
        tv.setColumnResizePolicy(param -> true);

        tv.setRowFactory(r -> {
            TableRow<Programa> row = new TableRow<>();
            row.setOnMouseClicked(evt -> {
                if (!row.isEmpty() && evt.getClickCount() == 2 && evt.getButton() == javafx.scene.input.MouseButton.PRIMARY) {
                    Programa p = row.getItem();
                    nombreField.setText(p.getNombre());
                    duracionField.setText(p.getDuracion() == null ? "" : String.valueOf(p.getDuracion()));
                }
            });
            return row;
        });

        return tv;
    }

    private GridPane createForm() {
        nombreField = new TextField();
        duracionField = new TextField();

        nombreField.setPromptText("Nombre");
        duracionField.setPromptText("Duración (meses)");

        nombreField.setPrefWidth(260);
        duracionField.setPrefWidth(120);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.add(new Label("Nombre:"), 0, 0);
        grid.add(nombreField, 1, 0);
        grid.add(new Label("Duración:"), 0, 1);
        grid.add(duracionField, 1, 1);
        return grid;
    }

    private HBox createButtons(Stage stage) {
        addBtn = new Button("Agregar");
        updateBtn = new Button("Actualizar");
        deleteBtn = new Button("Eliminar");
        backBtn = new Button("Volver");

        addBtn.getStyleClass().add("menu-button");
        updateBtn.getStyleClass().add("menu-button");
        deleteBtn.getStyleClass().add("menu-button");
        backBtn.getStyleClass().add("menu-button");

        addBtn.setOnAction(e -> onAdd());
        updateBtn.setOnAction(e -> onUpdate());
        deleteBtn.setOnAction(e -> onDelete());

        backBtn.setOnAction(e -> new MainMenuView().show(stage));

        HBox h = new HBox(8, addBtn, updateBtn, deleteBtn, backBtn);
        h.setAlignment(Pos.CENTER_LEFT);
        h.setPadding(new Insets(6,0,0,0));
        return h;
    }

    private void onAdd() {
        if (nombreField.getText().trim().isEmpty()) { new Alert(Alert.AlertType.WARNING, "Nombre es obligatorio").showAndWait(); return; }
        try {
            Double dur = null;
            String d = duracionField.getText().trim();
            if (!d.isEmpty()) dur = Double.parseDouble(d);
            ProgramaDTO dto = new ProgramaDTO(null, nombreField.getText().trim(), dur, new java.util.Date(), null);
            programaDAO.insertar(dto);
            reloadTable(); clearForm();
            new Alert(Alert.AlertType.INFORMATION, "Programa agregado").showAndWait();
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "Error al agregar programa", ex);
            new Alert(Alert.AlertType.ERROR, "Error al agregar: " + ex.getMessage()).showAndWait();
        }
    }

    private void onUpdate() {
        Programa sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un programa").showAndWait(); return; }
        try {
            Double dur = null; String d = duracionField.getText().trim(); if (!d.isEmpty()) dur = Double.parseDouble(d);
            ProgramaDTO dto = new ProgramaDTO(sel.getID(), nombreField.getText().trim(), dur, sel.getRegistro(), null);
            programaDAO.actualizar(dto);
            reloadTable(); clearForm();
            new Alert(Alert.AlertType.INFORMATION, "Programa actualizado").showAndWait();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al actualizar programa", ex);
            new Alert(Alert.AlertType.ERROR, "Error al actualizar: " + ex.getMessage()).showAndWait();
        }
    }

    private void onDelete() {
        Programa sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un programa").showAndWait(); return; }
        Alert c = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> r = c.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            try { programaDAO.eliminar(sel.getID()); reloadTable(); new Alert(Alert.AlertType.INFORMATION, "Programa eliminado").showAndWait(); }
            catch (Exception ex) { LOGGER.log(Level.SEVERE, "Error al eliminar programa", ex); new Alert(Alert.AlertType.ERROR, "Error: " + ex.getMessage()).showAndWait(); }
        }
    }

    private void reloadTable() {
        try { table.getItems().setAll(programaDAO.listar()); }
        catch (Exception ex) { LOGGER.log(Level.WARNING, "No se pudieron recargar programas", ex); }
    }

    private void clearForm() { nombreField.clear(); duracionField.clear(); }
}
