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

import com.mycompany.app.Model.Curso;
import com.mycompany.app.Controller.CursoController;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.DTO.ProgramaDTO;
import com.mycompany.app.Controller.Mappers.CursoMapper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;

public class CursosView {
    public void show(Stage stage) {
    Label title = new Label("Gestión de Cursos");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Button backBtn = new Button("← Volver al menú");
    backBtn.setOnAction(ev -> new MainMenuView().show(stage));
    HBox topBar = new HBox(backBtn);
    topBar.setAlignment(Pos.CENTER_LEFT);
    topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Curso> table = new TableView<>();
        TableColumn<Curso, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Curso, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        TableColumn<Curso, Boolean> activoCol = new TableColumn<>("Activo");
        activoCol.setCellValueFactory(new PropertyValueFactory<>("activo"));
        TableColumn<Curso, String> programaCol = new TableColumn<>("Programa");
        programaCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
            cell.getValue().getPrograma() != null ? cell.getValue().getPrograma().getNombre() : ""
        ));
        table.getColumns().add(idCol);
        table.getColumns().add(nombreCol);
        table.getColumns().add(programaCol);
        table.getColumns().add(activoCol);

        TextField nombreField = new TextField();
        nombreField.setPromptText("Nombre del curso");
        ComboBox<ProgramaDTO> programaBox = new ComboBox<>();
        programaBox.setPromptText("Programa");
        CheckBox activoCheck = new CheckBox("Activo");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        HBox fieldsRow = new HBox(10, nombreField, programaBox, activoCheck);
        fieldsRow.setAlignment(Pos.CENTER);
        fieldsRow.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

    VBox vbox = new VBox(15, topBar, title, table, fieldsRow, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 900, 500);
        stage.setTitle("Gestión de Cursos");
        stage.setScene(scene);
        stage.show();

        CursoController cursoCtrl = UiContext.cursoController();
        ProgramaController progCtrl = UiContext.programaController();
        try {
            table.setItems(loadData(cursoCtrl));
            programaBox.setItems(FXCollections.observableArrayList(progCtrl.listar()));
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar datos iniciales.\n" + ex.getMessage());
        }

        addBtn.setOnAction(e -> {
            if (isBlank(nombreField) || programaBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre y Programa.");
                return;
            }
            try {
                double id = Math.abs(nombreField.getText().hashCode());
                CursoDTO dto = new CursoDTO(id, nombreField.getText(), programaBox.getValue(), activoCheck.isSelected());
                cursoCtrl.insertar(dto);
                table.setItems(loadData(cursoCtrl));
                clearForm(nombreField, programaBox, activoCheck);
                showAlert("Agregado", "Curso agregado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo agregar: " + ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            nombreField.setText(sel.getNombre());
            activoCheck.setSelected(Boolean.TRUE.equals(sel.getActivo()));
            if (sel.getPrograma() != null) {
                ProgramaDTO match = programaBox.getItems().stream()
                    .filter(p -> p.getID().equals(sel.getPrograma().getID()))
                    .findFirst().orElse(null);
                programaBox.setValue(match);
            }
        });

        updateBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un curso de la tabla para actualizar.");
                return;
            }
            if (isBlank(nombreField) || programaBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombre y Programa.");
                return;
            }
            try {
                CursoDTO dto = new CursoDTO(selected.getID(), nombreField.getText(), programaBox.getValue(), activoCheck.isSelected());
                cursoCtrl.actualizar(dto);
                table.setItems(loadData(cursoCtrl));
                clearForm(nombreField, programaBox, activoCheck);
                showAlert("Actualizado", "Curso actualizado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un curso de la tabla para eliminar.");
                return;
            }
            cursoCtrl.eliminar(selected.getID().intValue());
            table.setItems(loadData(cursoCtrl));
            clearForm(nombreField, programaBox, activoCheck);
            showAlert("Eliminado", "Curso eliminado correctamente.");
        });

        clearBtn.setOnAction(e -> clearForm(nombreField, programaBox, activoCheck));
    }

    private boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().isBlank();
    }

    private ObservableList<Curso> loadData(CursoController controller) {
        return FXCollections.observableArrayList(
            CursoMapper.toEntityList(controller.listar())
        );
    }

    private void clearForm(TextField nombreField, ComboBox<ProgramaDTO> programaBox, CheckBox activoCheck) {
        nombreField.clear();
        programaBox.setValue(null);
        activoCheck.setSelected(false);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
