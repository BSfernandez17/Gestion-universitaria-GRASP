package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import com.mycompany.app.Model.Programa;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.Controller.FacultadController;
import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.Controller.Mappers.ProgramaMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProgramasView {
    public void show(Stage stage) {
    Label title = new Label("Gestión de Programas");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Button backBtn = new Button("← Volver al menú");
    backBtn.setOnAction(ev -> new MainMenuView().show(stage));
    HBox topBar = new HBox(backBtn);
    topBar.setAlignment(Pos.CENTER_LEFT);
    topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Programa> table = new TableView<>();
        TableColumn<Programa, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Programa, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Programa, Double> duracionCol = new TableColumn<>("Duración");
        duracionCol.setCellValueFactory(new PropertyValueFactory<>("duracion"));
        TableColumn<Programa, Date> registroCol = new TableColumn<>("Registro");
        registroCol.setCellValueFactory(new PropertyValueFactory<>("registro"));
        table.getColumns().add(idCol);
        table.getColumns().add(nombreCol);
        table.getColumns().add(duracionCol);
        table.getColumns().add(registroCol);

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre");
        TextField duracionField = new TextField();
        duracionField.setPromptText("Duración (en semestres)");
        DatePicker registroPicker = new DatePicker();
        registroPicker.setPromptText("Fecha registro");
        ComboBox<FacultadDTO> facultadBox = new ComboBox<>();
        facultadBox.setPromptText("Facultad");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        HBox fieldsRow1 = new HBox(10, nombreField, duracionField, registroPicker, facultadBox);
        fieldsRow1.setAlignment(Pos.CENTER);
        fieldsRow1.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

    VBox vbox = new VBox(15, topBar, title, table, fieldsRow1, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 1000, 500);
        stage.setTitle("Gestión de Programas");
        stage.setScene(scene);
        stage.show();

        ProgramaController prgCtrl = UiContext.programaController();
        FacultadController facCtrl = UiContext.facultadController();
        try {
            table.setItems(loadData(prgCtrl));
            facultadBox.setItems(FXCollections.observableArrayList(facCtrl.listar()));
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar datos iniciales.\n" + ex.getMessage());
        }

        addBtn.setOnAction(e -> {
            if (isBlank(nombreField) || isBlank(duracionField) || registroPicker.getValue() == null || facultadBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre, Duración, Registro y Facultad.");
                return;
            }
            try {
                double id = Math.abs(nombreField.getText().hashCode());
                double duracion = Double.parseDouble(duracionField.getText());
                LocalDate ld = registroPicker.getValue();
                Date fecha = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                FacultadDTO fac = facultadBox.getValue();
                ProgramaDTO dto = new ProgramaDTO(id, nombreField.getText(), duracion, fecha, fac);
                prgCtrl.insertar(dto);
                table.setItems(loadData(prgCtrl));
                clearForm(nombreField, duracionField, registroPicker, facultadBox);
                showAlert("Agregado", "Programa agregado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo agregar: " + ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            nombreField.setText(sel.getNombre());
            duracionField.setText(sel.getDuracion() != null ? sel.getDuracion().toString() : "");
            if (sel.getRegistro() != null) {
                LocalDate ld = sel.getRegistro().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
                registroPicker.setValue(ld);
            } else {
                registroPicker.setValue(null);
            }
            // Seleccionar la facultad por ID
            if (sel.getFacultad() != null) {
                FacultadDTO match = facultadBox.getItems().stream()
                    .filter(f -> f.getID().equals(sel.getFacultad().getID()))
                    .findFirst().orElse(null);
                facultadBox.setValue(match);
            }
        });

        updateBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un programa de la tabla para actualizar.");
                return;
            }
            if (isBlank(nombreField) || isBlank(duracionField) || registroPicker.getValue() == null || facultadBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre, Duración, Registro y Facultad.");
                return;
            }
            try {
                double id = selected.getID();
                double duracion = Double.parseDouble(duracionField.getText());
                LocalDate ld = registroPicker.getValue();
                Date fecha = Date.from(ld.atStartOfDay(ZoneId.systemDefault()).toInstant());
                FacultadDTO fac = facultadBox.getValue();
                ProgramaDTO dto = new ProgramaDTO(id, nombreField.getText(), duracion, fecha, fac);
                prgCtrl.actualizar(dto);
                table.setItems(loadData(prgCtrl));
                clearForm(nombreField, duracionField, registroPicker, facultadBox);
                showAlert("Actualizado", "Programa actualizado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un programa de la tabla para eliminar.");
                return;
            }
            prgCtrl.eliminar(selected.getID());
            table.setItems(loadData(prgCtrl));
            clearForm(nombreField, duracionField, registroPicker, facultadBox);
            showAlert("Eliminado", "Programa eliminado correctamente.");
        });
    }

    private boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().isBlank();
    }

    private ObservableList<Programa> loadData(ProgramaController controller) {
        return FXCollections.observableArrayList(
            ProgramaMapper.toEntityList(controller.listar())
        );
    }

    private void clearForm(TextField nombreField, TextField duracionField, DatePicker registroPicker, ComboBox<FacultadDTO> facultadBox) {
        nombreField.clear();
        duracionField.clear();
        registroPicker.setValue(null);
        facultadBox.setValue(null);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
