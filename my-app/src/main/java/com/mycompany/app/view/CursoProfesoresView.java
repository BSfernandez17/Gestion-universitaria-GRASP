package com.mycompany.app.view;

import java.util.List;

import com.mycompany.app.Controller.CursoController;
import com.mycompany.app.Controller.ProfesorController;
import com.mycompany.app.Controller.CursoProfesorController;
import com.mycompany.app.DTO.CursoDTO;
import com.mycompany.app.DTO.CursoProfesorDTO;
import com.mycompany.app.DTO.ProfesorDTO;

import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CursoProfesoresView {
    private final CursoProfesorController cpController = UiContext.cursoProfesorController();
    private final CursoController cursoController = UiContext.cursoController();
    private final ProfesorController profesorController = UiContext.profesorController();

    private final TableView<CursoProfesorDTO> table = new TableView<>();
    private final ObservableList<CursoProfesorDTO> data = FXCollections.observableArrayList();

    private final ComboBox<CursoDTO> cursoCombo = new ComboBox<>();
    private final ComboBox<ProfesorDTO> profesorCombo = new ComboBox<>();
    private final TextField anioField = new TextField();
    private final TextField semestreField = new TextField();

    public void show(Stage stage) {
        stage.setTitle("Asignación Curso–Profesor");

        // Tabla
        TableColumn<CursoProfesorDTO, String> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getID() == null ? "" : String.valueOf(cell.getValue().getID())));

        TableColumn<CursoProfesorDTO, String> cursoCol = new TableColumn<>("Curso");
        cursoCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getCursoDTO() != null ? cell.getValue().getCursoDTO().getNombre() : ""));

        TableColumn<CursoProfesorDTO, String> profesorCol = new TableColumn<>("Profesor");
        profesorCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getProfesorDTO() != null
                        ? (cell.getValue().getProfesorDTO().getNombres() + " "
                                + cell.getValue().getProfesorDTO().getApellidos())
                        : ""));

        TableColumn<CursoProfesorDTO, String> anioCol = new TableColumn<>("Año");
        anioCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getAño() == null ? "" : String.valueOf(cell.getValue().getAño())));

        TableColumn<CursoProfesorDTO, String> semestreCol = new TableColumn<>("Semestre");
        semestreCol.setCellValueFactory(cell -> new ReadOnlyStringWrapper(
                cell.getValue().getSemestre() == null ? "" : String.valueOf(cell.getValue().getSemestre())));

    table.getColumns().add(idCol);
    table.getColumns().add(cursoCol);
    table.getColumns().add(profesorCol);
    table.getColumns().add(anioCol);
    table.getColumns().add(semestreCol);
        table.setItems(data);
        table.setPrefHeight(260);
        table.getSelectionModel().selectedItemProperty().addListener((obs, old, sel) -> {
            if (sel != null) {
                selectCursoById(sel.getCursoDTO() != null ? sel.getCursoDTO().getID() : null);
                selectProfesorById(sel.getProfesorDTO() != null ? sel.getProfesorDTO().getID() : null);
                anioField.setText(sel.getAño() == null ? "" : String.valueOf(sel.getAño()));
                semestreField.setText(sel.getSemestre() == null ? "" : String.valueOf(sel.getSemestre()));
            }
        });

        // Double click to edit
        table.setRowFactory(tv -> {
            TableRow<CursoProfesorDTO> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && !row.isEmpty()) {
                    CursoProfesorDTO sel = row.getItem();
                    selectCursoById(sel.getCursoDTO() != null ? sel.getCursoDTO().getID() : null);
                    selectProfesorById(sel.getProfesorDTO() != null ? sel.getProfesorDTO().getID() : null);
                    anioField.setText(sel.getAño() == null ? "" : String.valueOf(sel.getAño()));
                    semestreField.setText(sel.getSemestre() == null ? "" : String.valueOf(sel.getSemestre()));
                }
            });
            return row;
        });

        // Formulario
    // Formulario
        List<CursoDTO> cursos = cursoController.listar();
        List<ProfesorDTO> profesores = profesorController.listar();
        cursoCombo.setItems(FXCollections.observableArrayList(cursos));
        cursoCombo.setPromptText("Seleccione un curso");
        profesorCombo.setItems(FXCollections.observableArrayList(profesores));
        profesorCombo.setPromptText("Seleccione un profesor");
        anioField.setPromptText("Año (>= 2000)");
        semestreField.setPromptText("Semestre (1 o 2)");

        GridPane form = new GridPane();
        form.setHgap(10);
        form.setVgap(8);
        form.add(new Label("Curso:"), 0, 0);
        form.add(cursoCombo, 1, 0);
        form.add(new Label("Profesor:"), 0, 1);
        form.add(profesorCombo, 1, 1);
        form.add(new Label("Año:"), 0, 2);
        form.add(anioField, 1, 2);
        form.add(new Label("Semestre:"), 0, 3);
        form.add(semestreField, 1, 3);

        // Botones
    Button crearBtn = new Button("Asignar");
    Button actualizarBtn = new Button("Actualizar");
    Button eliminarBtn = new Button("Eliminar");
        Button volverBtn = new Button("← Volver al menú");

    // Tooltips
    crearBtn.setTooltip(new Tooltip("Crear nueva asignación"));
    actualizarBtn.setTooltip(new Tooltip("Actualizar asignación seleccionada"));
    eliminarBtn.setTooltip(new Tooltip("Eliminar asignación seleccionada"));

        crearBtn.setOnAction(e -> onCrear());
        actualizarBtn.setOnAction(e -> onActualizar());
        eliminarBtn.setOnAction(e -> onEliminar());
        volverBtn.setOnAction(e -> new MainMenuView().show(stage));

        HBox buttons = new HBox(10, crearBtn, actualizarBtn, eliminarBtn);
        buttons.setAlignment(Pos.CENTER_LEFT);

        VBox right = new VBox(12, form, buttons, volverBtn);
        right.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setPadding(new Insets(15));
        root.setCenter(table);
        root.setRight(right);

        loadData();

        Scene scene = new Scene(root, 900, 420);
        stage.setScene(scene);
        stage.show();
    }

    private void selectCursoById(Double id) {
        if (id == null) return;
        for (CursoDTO c : cursoCombo.getItems()) {
            if (c != null && c.getID() != null && c.getID().doubleValue() == id.doubleValue()) {
                cursoCombo.getSelectionModel().select(c);
                break;
            }
        }
    }

    private void selectProfesorById(Double id) {
        if (id == null) return;
        for (ProfesorDTO p : profesorCombo.getItems()) {
            if (p != null && p.getID() != null && p.getID().doubleValue() == id.doubleValue()) {
                profesorCombo.getSelectionModel().select(p);
                break;
            }
        }
    }

    private void loadData() {
        data.setAll(cpController.listar());
    }

    private void onCrear() {
        if (!validateForm()) {
            return;
        }
        CursoDTO curso = cursoCombo.getValue();
        ProfesorDTO prof = profesorCombo.getValue();
        Integer anio = Integer.parseInt(anioField.getText().trim());
        Integer semestre = Integer.parseInt(semestreField.getText().trim());

        CursoProfesorDTO dto = new CursoProfesorDTO(null, prof, anio, semestre, curso);
        try {
            cpController.insertar(dto);
            loadData();
            clearForm();
            showInfo("Asignación creada correctamente.");
        } catch (Exception ex) {
            showError("Error al crear la asignación: " + ex.getMessage());
        }
    }

    private void onActualizar() {
        CursoProfesorDTO sel = table.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showWarning("Seleccione una fila para actualizar.");
            return;
        }
        if (!validateForm()) {
            return;
        }
        CursoDTO curso = cursoCombo.getValue();
        ProfesorDTO prof = profesorCombo.getValue();
        Integer anio = Integer.parseInt(anioField.getText().trim());
        Integer semestre = Integer.parseInt(semestreField.getText().trim());

        CursoProfesorDTO dto = new CursoProfesorDTO(sel.getID(), prof, anio, semestre, curso);
        try {
            cpController.actualizar(dto);
            loadData();
            showInfo("Asignación actualizada.");
        } catch (Exception ex) {
            showError("Error al actualizar: " + ex.getMessage());
        }
    }

    private void onEliminar() {
        CursoProfesorDTO sel = table.getSelectionModel().getSelectedItem();
        if (sel == null || sel.getID() == null) {
            showWarning("Seleccione una fila para eliminar.");
            return;
        }
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,
                "¿Eliminar la asignación seleccionada?", ButtonType.YES, ButtonType.NO);
        confirm.setHeaderText("Confirmación");
        confirm.showAndWait();
        if (confirm.getResult() == ButtonType.YES) {
            try {
                cpController.eliminar(sel.getID());
                loadData();
                clearForm();
            } catch (Exception ex) {
                showError("Error al eliminar: " + ex.getMessage());
            }
        }
    }

    private boolean validateForm() {
        if (cursoCombo.getValue() == null) {
            showWarning("Seleccione un curso.");
            return false;
        }
        if (profesorCombo.getValue() == null) {
            showWarning("Seleccione un profesor.");
            return false;
        }
        String anioTxt = anioField.getText() == null ? "" : anioField.getText().trim();
        String semTxt = semestreField.getText() == null ? "" : semestreField.getText().trim();
        try {
            int anio = Integer.parseInt(anioTxt);
            if (anio < 2000) {
                showWarning("El año debe ser >= 2000.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showWarning("Ingrese un año válido.");
            return false;
        }
        try {
            int sem = Integer.parseInt(semTxt);
            if (sem != 1 && sem != 2) {
                showWarning("El semestre debe ser 1 o 2.");
                return false;
            }
        } catch (NumberFormatException ex) {
            showWarning("Ingrese un semestre válido.");
            return false;
        }
        return true;
    }

    private void clearForm() {
        cursoCombo.getSelectionModel().clearSelection();
        profesorCombo.getSelectionModel().clearSelection();
        anioField.clear();
        semestreField.clear();
    }

    private void showInfo(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText("Información");
        a.showAndWait();
    }

    private void showWarning(String msg) {
        Alert a = new Alert(Alert.AlertType.WARNING, msg, ButtonType.OK);
        a.setHeaderText("Atención");
        a.showAndWait();
    }

    private void showError(String msg) {
        Alert a = new Alert(Alert.AlertType.ERROR, msg, ButtonType.OK);
        a.setHeaderText("Error");
        a.showAndWait();
    }
}
