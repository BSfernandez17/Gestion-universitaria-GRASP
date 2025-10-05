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

import com.mycompany.app.Model.Facultad;
import com.mycompany.app.Controller.FacultadController;
import com.mycompany.app.Controller.PersonaController;
import com.mycompany.app.DTO.FacultadDTO;
import com.mycompany.app.DTO.PersonaDTO;
import com.mycompany.app.Controller.Mappers.FacultadMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;

public class FacultadesView {
    public void show(Stage stage) {
        Label title = new Label("Gestión de Facultades");
    title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button backBtn = new Button("← Volver al menú");
        backBtn.setOnAction(ev -> new MainMenuView().show(stage));
        HBox topBar = new HBox(backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Facultad> table = new TableView<>();
        TableColumn<Facultad, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Facultad, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Facultad, String> decanoCol = new TableColumn<>("Decano");
        decanoCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
            cell.getValue().getDecano() != null ? cell.getValue().getDecano().getNombres() + " " + cell.getValue().getDecano().getApellidos() : ""
        ));
        table.getColumns().add(idCol);
        table.getColumns().add(nombreCol);
        table.getColumns().add(decanoCol);

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre de la facultad");
        ComboBox<PersonaDTO> decanoBox = new ComboBox<>();
        decanoBox.setPromptText("Decano");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        HBox fieldsRow = new HBox(10, nombreField, decanoBox);
        fieldsRow.setAlignment(Pos.CENTER);
        fieldsRow.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

        VBox vbox = new VBox(15, topBar, title, table, fieldsRow, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 900, 500);
        stage.setTitle("Gestión de Facultades");
        stage.setScene(scene);
        stage.show();

        FacultadController facCtrl = UiContext.facultadController();
        PersonaController perCtrl = UiContext.personaController();
        try {
            table.setItems(loadData(facCtrl));
            decanoBox.setItems(FXCollections.observableArrayList(perCtrl.listar()));
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar datos iniciales.\n" + ex.getMessage());
        }

        addBtn.setOnAction(e -> {
            if (isBlank(nombreField) || decanoBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre y selecciona Decano.");
                return;
            }
            try {
                double id = Math.abs(nombreField.getText().hashCode());
                FacultadDTO dto = new FacultadDTO(id, nombreField.getText(), decanoBox.getValue());
                facCtrl.insertar(dto);
                table.setItems(loadData(facCtrl));
                clearForm(nombreField, decanoBox);
                showAlert("Agregado", "Facultad agregada correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo agregar: " + ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            nombreField.setText(sel.getNombre());
            if (sel.getDecano() != null) {
                PersonaDTO match = decanoBox.getItems().stream()
                    .filter(p -> p.getID().equals(sel.getDecano().getID()))
                    .findFirst().orElse(null);
                decanoBox.setValue(match);
            }
        });

        updateBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona una facultad de la tabla para actualizar.");
                return;
            }
            if (isBlank(nombreField) || decanoBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre y selecciona Decano.");
                return;
            }
            try {
                FacultadDTO dto = new FacultadDTO(selected.getID(), nombreField.getText(), decanoBox.getValue());
                facCtrl.actualizar(dto);
                table.setItems(loadData(facCtrl));
                clearForm(nombreField, decanoBox);
                showAlert("Actualizado", "Facultad actualizada correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona una facultad de la tabla para eliminar.");
                return;
            }
            facCtrl.eliminar(selected.getID());
            table.setItems(loadData(facCtrl));
            clearForm(nombreField, decanoBox);
            showAlert("Eliminado", "Facultad eliminada correctamente.");
        });

        clearBtn.setOnAction(e -> clearForm(nombreField, decanoBox));
    }

    private boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().isBlank();
    }

    private ObservableList<Facultad> loadData(FacultadController controller) {
        return FXCollections.observableArrayList(
            FacultadMapper.toEntityList(controller.listar())
        );
    }

    private void clearForm(TextField nombreField, ComboBox<PersonaDTO> decanoBox) {
        nombreField.clear();
        decanoBox.setValue(null);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
