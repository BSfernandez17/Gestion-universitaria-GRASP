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
import com.mycompany.app.Model.Estudiante;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import com.mycompany.app.Controller.EstudianteController;
import com.mycompany.app.Controller.ProgramaController;
import com.mycompany.app.DTO.EstudianteDTO;
import com.mycompany.app.DTO.ProgramaDTO;

public class EstudiantesView {
    public void show(Stage stage) {
        Label title = new Label("Gestión de Estudiantes");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        // Barra superior con botón de regreso
        Button backBtn = new Button("← Volver al menú");
        backBtn.setOnAction(ev -> new MainMenuView().show(stage));
        HBox topBar = new HBox(backBtn);
        topBar.setAlignment(Pos.CENTER_LEFT);
        topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Estudiante> table = new TableView<>();
        TableColumn<Estudiante, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Estudiante, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        TableColumn<Estudiante, String> apellidosCol = new TableColumn<>("Apellidos");
        apellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        TableColumn<Estudiante, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
    table.getColumns().add(idCol);
    table.getColumns().add(nombresCol);
    table.getColumns().add(apellidosCol);
    table.getColumns().add(emailCol);

        TextField nombresField = new TextField();
        nombresField.setPromptText("Nombres");
        TextField apellidosField = new TextField();
        apellidosField.setPromptText("Apellidos");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        TextField codigoField = new TextField();
        codigoField.setPromptText("Código");
        TextField promedioField = new TextField();
        promedioField.setPromptText("Promedio");
        CheckBox activoCheck = new CheckBox("Activo");
        ComboBox<ProgramaDTO> programaBox = new ComboBox<>();
        programaBox.setPromptText("Programa");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        addBtn.setTooltip(new Tooltip("Agregar estudiante"));
        updateBtn.setTooltip(new Tooltip("Actualizar estudiante seleccionado"));
        deleteBtn.setTooltip(new Tooltip("Eliminar estudiante seleccionado"));
        clearBtn.setTooltip(new Tooltip("Limpiar formulario"));

        HBox fieldsRow1 = new HBox(10, nombresField, apellidosField, emailField);
        fieldsRow1.setAlignment(Pos.CENTER);
        fieldsRow1.setPadding(new Insets(10));

        HBox fieldsRow2 = new HBox(10, codigoField, promedioField, activoCheck, programaBox);
        fieldsRow2.setAlignment(Pos.CENTER);
        fieldsRow2.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

        VBox vbox = new VBox(15, topBar, title, table, fieldsRow1, fieldsRow2, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 1000, 500);
        stage.setTitle("Gestión de Estudiantes");
        stage.setScene(scene);
        stage.show();

        EstudianteController controller = UiContext.estudianteController();
        try {
            table.setItems(loadData(controller));
        } catch (Exception ex) {
            System.out.println("[UI] Error cargando estudiantes: " + ex.getMessage());
            showAlert("Error", "No se pudieron cargar los estudiantes.\n" + ex.getMessage());
        }

        try {
            ProgramaController programaCtrl = UiContext.programaController();
            programaBox.setItems(FXCollections.observableArrayList(programaCtrl.listar()));
        } catch (Exception ex) {
            System.out.println("[UI] Error cargando programas: " + ex.getMessage());
            showAlert("Error", "No se pudieron cargar los programas.\n" + ex.getMessage());
        }

        // Agregar
        addBtn.setOnAction(e -> {
            String nombres = nombresField.getText();
            String apellidos = apellidosField.getText();
            String email = emailField.getText();
            ProgramaDTO programa = programaBox.getValue();
            if (nombres == null || nombres.isBlank() || apellidos == null || apellidos.isBlank() || email == null || email.isBlank() || programa == null) {
                showAlert("Campos incompletos", "Completa Nombres, Apellidos, Email y selecciona un Programa.");
                return;
            }
            if (!isValidEmail(email)) {
                showAlert("Email inválido", "Introduce una dirección de correo válida.");
                return;
            }
            double codigo;
            double promedio;
            try {
                codigo = codigoField.getText().isBlank() ? Math.floor(Math.random() * 9000) + 1000 : Double.parseDouble(codigoField.getText());
                promedio = promedioField.getText().isBlank() ? 3.5 : Double.parseDouble(promedioField.getText());
            } catch (NumberFormatException ex) {
                showAlert("Valores inválidos", "Código y Promedio deben ser numéricos.");
                return;
            }
            boolean activo = activoCheck.isSelected();
            double newId = codigo;
            EstudianteDTO nuevo = new EstudianteDTO(newId, nombres, apellidos, email, codigo, programa, activo, promedio);
            controller.insertar(nuevo);
            table.setItems(loadData(controller));
            clearForm(nombresField, apellidosField, emailField, codigoField, promedioField, activoCheck, programaBox);
            showAlert("Agregado", "Estudiante agregado correctamente.");
        });

        // Selección
        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            nombresField.setText(sel.getNombres());
            apellidosField.setText(sel.getApellidos());
            emailField.setText(sel.getEmail());
            codigoField.setText(sel.getCodigo() != null ? sel.getCodigo().toString() : "");
            promedioField.setText(sel.getPromedio() != null ? sel.getPromedio().toString() : "");
            activoCheck.setSelected(Boolean.TRUE.equals(sel.getActivo()));
            if (sel.getPrograma() != null) {
                ProgramaDTO match = programaBox.getItems().stream()
                        .filter(p -> p.getID().equals(sel.getPrograma().getID()))
                        .findFirst().orElse(null);
                programaBox.setValue(match);
            }
        });

        // Actualizar
        updateBtn.setOnAction(e -> {
            Estudiante selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un estudiante de la tabla para actualizar.");
                return;
            }
            ProgramaDTO programa = programaBox.getValue();
            if (programa == null) {
                showAlert("Programa requerido", "Selecciona un Programa.");
                return;
            }
            try {
                double id = selected.getID();
                double codigo = Double.parseDouble(codigoField.getText());
                double promedio = Double.parseDouble(promedioField.getText());
                boolean activo = activoCheck.isSelected();
                if (!isValidEmail(emailField.getText())) {
                    showAlert("Email inválido", "Introduce una dirección de correo válida.");
                    return;
                }
                EstudianteDTO dto = new EstudianteDTO(id, nombresField.getText(), apellidosField.getText(), emailField.getText(), codigo, programa, activo, promedio);
                controller.actualizar(dto);
                table.setItems(loadData(controller));
                clearForm(nombresField, apellidosField, emailField, codigoField, promedioField, activoCheck, programaBox);
                showAlert("Actualizado", "Estudiante actualizado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        // Eliminar
        deleteBtn.setOnAction(e -> {
            Estudiante selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un estudiante de la tabla para eliminar.");
                return;
            }
            controller.eliminar(selected.getID());
            table.setItems(loadData(controller));
            clearForm(nombresField, apellidosField, emailField, codigoField, promedioField, activoCheck, programaBox);
            showAlert("Eliminado", "Estudiante eliminado correctamente.");
        });

        // Limpiar
        clearBtn.setOnAction(e -> clearForm(nombresField, apellidosField, emailField, codigoField, promedioField, activoCheck, programaBox));
    }

    private ObservableList<Estudiante> loadData(EstudianteController controller) {
        return FXCollections.observableArrayList(
                com.mycompany.app.Controller.Mappers.EstudianteMapper.toEntityList(controller.listar()));
    }

    private void clearForm(TextField nombresField, TextField apellidosField, TextField emailField,
                           TextField codigoField, TextField promedioField, CheckBox activoCheck,
                           ComboBox<ProgramaDTO> programaBox) {
        nombresField.clear();
        apellidosField.clear();
        emailField.clear();
        codigoField.clear();
        promedioField.clear();
        activoCheck.setSelected(false);
        programaBox.setValue(null);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private boolean isValidEmail(String email) {
        if (email == null) return false;
        return email.matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    }
}
