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

import com.mycompany.app.Model.Inscripcion;
import com.mycompany.app.Controller.InscripcionController;
import com.mycompany.app.Controller.CursoController;
import com.mycompany.app.Controller.EstudianteController;
import com.mycompany.app.DTO.InscripcionDTO;
import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.Controller.Mappers.InscripcionMappper;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.beans.property.ReadOnlyStringWrapper;

public class InscripcionesView {
    public void show(Stage stage) {
        Label title = new Label("Gestión de Inscripciones");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        Button backBtn = new Button("← Volver al menú");
        backBtn.setOnAction(ev -> new MainMenuView().show(stage));
        HBox topBar = new HBox(backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Inscripcion> table = new TableView<>();
        TableColumn<Inscripcion, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Inscripcion, String> estudianteCol = new TableColumn<>("Estudiante");
        estudianteCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
            cell.getValue().getEstudiante() != null ? cell.getValue().getEstudiante().getNombres() + " " + cell.getValue().getEstudiante().getApellidos() : ""
        ));
        TableColumn<Inscripcion, String> cursoCol = new TableColumn<>("Curso");
        cursoCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
            cell.getValue().getCurso() != null ? cell.getValue().getCurso().getNombre() : ""
        ));
        TableColumn<Inscripcion, Integer> anioCol = new TableColumn<>("Año");
        anioCol.setCellValueFactory(new PropertyValueFactory<>("año"));
        TableColumn<Inscripcion, Integer> semestreCol = new TableColumn<>("Semestre");
        semestreCol.setCellValueFactory(new PropertyValueFactory<>("semestre"));
    table.getColumns().add(idCol);
    table.getColumns().add(estudianteCol);
    table.getColumns().add(cursoCol);
    table.getColumns().add(anioCol);
    table.getColumns().add(semestreCol);

        ComboBox<EstudianteDTO> estudianteBox = new ComboBox<>();
        estudianteBox.setPromptText("Estudiante");
        ComboBox<CursoDTO> cursoBox = new ComboBox<>();
        cursoBox.setPromptText("Curso");
        TextField anioField = new TextField();
        anioField.setPromptText("Año");
        TextField semestreField = new TextField();
        semestreField.setPromptText("Semestre");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        HBox fieldsRow = new HBox(10, estudianteBox, cursoBox, anioField, semestreField);
        fieldsRow.setAlignment(Pos.CENTER);
        fieldsRow.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

        VBox vbox = new VBox(15, topBar, title, table, fieldsRow, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 1100, 520);
        stage.setTitle("Gestión de Inscripciones");
        stage.setScene(scene);
        stage.show();

        InscripcionController inscCtrl = UiContext.inscripcionController();
        EstudianteController estCtrl = UiContext.estudianteController();
        CursoController curCtrl = UiContext.cursoController();
        try {
            table.setItems(loadData(inscCtrl));
            estudianteBox.setItems(FXCollections.observableArrayList(estCtrl.listar()));
            cursoBox.setItems(FXCollections.observableArrayList(curCtrl.listar()));
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar datos iniciales.\n" + ex.getMessage());
        }

        addBtn.setOnAction(e -> {
            if (estudianteBox.getValue() == null || cursoBox.getValue() == null || isBlank(anioField) || isBlank(semestreField)) {
                showAlert("Campos incompletos", "Selecciona Estudiante y Curso, y completa Año y Semestre.");
                return;
            }
            try {
                double id = Math.floor(Math.random() * 900000) + 1000;
                int anio = Integer.parseInt(anioField.getText());
                int semestre = Integer.parseInt(semestreField.getText());
                InscripcionDTO dto = new InscripcionDTO(id, cursoBox.getValue(), anio, semestre, estudianteBox.getValue());
                inscCtrl.insertar(dto);
                table.setItems(loadData(inscCtrl));
                clearForm(estudianteBox, cursoBox, anioField, semestreField);
                showAlert("Agregado", "Inscripción agregada correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo agregar: " + ex.getMessage());
            }
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            // set selections
            if (sel.getEstudiante() != null) {
                EstudianteDTO matchE = estudianteBox.getItems().stream()
                    .filter(e1 -> e1.getID().equals(sel.getEstudiante().getID()))
                    .findFirst().orElse(null);
                estudianteBox.setValue(matchE);
            }
            if (sel.getCurso() != null) {
                CursoDTO matchC = cursoBox.getItems().stream()
                    .filter(c1 -> c1.getID().equals(sel.getCurso().getID()))
                    .findFirst().orElse(null);
                cursoBox.setValue(matchC);
            }
            anioField.setText(sel.getAño() != null ? sel.getAño().toString() : "");
            semestreField.setText(sel.getSemestre() != null ? sel.getSemestre().toString() : "");
        });

        updateBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona una inscripción de la tabla para actualizar.");
                return;
            }
            if (estudianteBox.getValue() == null || cursoBox.getValue() == null || isBlank(anioField) || isBlank(semestreField)) {
                showAlert("Campos incompletos", "Selecciona Estudiante y Curso, y completa Año y Semestre.");
                return;
            }
            try {
                int anio = Integer.parseInt(anioField.getText());
                int semestre = Integer.parseInt(semestreField.getText());
                InscripcionDTO dto = new InscripcionDTO(selected.getID(), cursoBox.getValue(), anio, semestre, estudianteBox.getValue());
                inscCtrl.actualizar(dto);
                table.setItems(loadData(inscCtrl));
                clearForm(estudianteBox, cursoBox, anioField, semestreField);
                showAlert("Actualizado", "Inscripción actualizada correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            var selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona una inscripción de la tabla para eliminar.");
                return;
            }
            inscCtrl.eliminar(selected.getID());
            table.setItems(loadData(inscCtrl));
            clearForm(estudianteBox, cursoBox, anioField, semestreField);
            showAlert("Eliminado", "Inscripción eliminada correctamente.");
        });

        clearBtn.setOnAction(e -> clearForm(estudianteBox, cursoBox, anioField, semestreField));
    }

    private boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().isBlank();
    }

    private ObservableList<Inscripcion> loadData(InscripcionController controller) {
        return FXCollections.observableArrayList(
            InscripcionMappper.toEntityList(controller.listar())
        );
    }

    private void clearForm(ComboBox<EstudianteDTO> estudianteBox, ComboBox<CursoDTO> cursoBox, TextField anioField, TextField semestreField) {
        estudianteBox.setValue(null);
        cursoBox.setValue(null);
        anioField.clear();
        semestreField.clear();
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
