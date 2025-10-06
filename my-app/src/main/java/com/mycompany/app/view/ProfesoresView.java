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

import com.mycompany.app.Model.Profesor;
import java.util.regex.Pattern;
import com.mycompany.app.Controller.ProfesorController;
import com.mycompany.app.DTO.ProfesorDTO;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ProfesoresView {
    public void show(Stage stage) {
    Label title = new Label("Gestión de Profesores");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

    Button backBtn = new Button("← Volver al menú");
    backBtn.setOnAction(ev -> new MainMenuView().show(stage));
    HBox topBar = new HBox(backBtn);
    topBar.setAlignment(Pos.CENTER_LEFT);
    topBar.setPadding(new Insets(10, 10, 0, 10));

        TableView<Profesor> table = new TableView<>();
        TableColumn<Profesor, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<Profesor, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        TableColumn<Profesor, String> apellidosCol = new TableColumn<>("Apellidos");
        apellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        TableColumn<Profesor, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        TableColumn<Profesor, String> tipoCol = new TableColumn<>("Tipo Contrato");
        tipoCol.setCellValueFactory(new PropertyValueFactory<>("tipoContrato"));
    table.getColumns().add(idCol);
    table.getColumns().add(nombresCol);
    table.getColumns().add(apellidosCol);
    table.getColumns().add(emailCol);
    table.getColumns().add(tipoCol);

        TextField nombresField = new TextField();
        nombresField.setPromptText("Nombres");
        TextField apellidosField = new TextField();
        apellidosField.setPromptText("Apellidos");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");

        ComboBox<String> tipoContratoBox = new ComboBox<>();
        tipoContratoBox.setPromptText("Tipo contrato");
        tipoContratoBox.getItems().addAll("planta", "ocasional", "catedra");

        Button addBtn = new Button("Agregar");
        Button updateBtn = new Button("Actualizar");
        Button deleteBtn = new Button("Eliminar");
        Button clearBtn = new Button("Limpiar");

        addBtn.setMinWidth(110);
        updateBtn.setMinWidth(110);
        deleteBtn.setMinWidth(110);
        clearBtn.setMinWidth(110);

        addBtn.setTooltip(new Tooltip("Agregar profesor"));
        updateBtn.setTooltip(new Tooltip("Actualizar profesor seleccionado"));
        deleteBtn.setTooltip(new Tooltip("Eliminar profesor seleccionado"));
        clearBtn.setTooltip(new Tooltip("Limpiar formulario"));

        HBox fieldsRow1 = new HBox(10, nombresField, apellidosField, emailField);
        fieldsRow1.setAlignment(Pos.CENTER);
        fieldsRow1.setPadding(new Insets(10));

        HBox fieldsRow2 = new HBox(10, tipoContratoBox);
        fieldsRow2.setAlignment(Pos.CENTER);
        fieldsRow2.setPadding(new Insets(10));

        HBox actionsRow = new HBox(12, addBtn, updateBtn, deleteBtn, clearBtn);
        actionsRow.setAlignment(Pos.CENTER);
        actionsRow.setPadding(new Insets(10));

    VBox vbox = new VBox(15, topBar, title, table, fieldsRow1, fieldsRow2, actionsRow);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 900, 500);
        stage.setTitle("Gestión de Profesores");
        stage.setScene(scene);
        stage.show();

        ProfesorController controller = UiContext.profesorController();
        try {
            table.setItems(loadData(controller));
        } catch (Exception ex) {
            showAlert("Error", "No se pudieron cargar los profesores.\n" + ex.getMessage());
        }

        addBtn.setOnAction(e -> {
            if (isBlank(nombresField) || isBlank(apellidosField) || isBlank(emailField) || tipoContratoBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombres, Apellidos, Email y el Tipo de contrato.");
                return;
            }

            // Validación de formato de correo
            if (!isValidEmail(emailField.getText())) {
                showAlert("Email inválido", "Por favor ingresa un correo electrónico con formato válido.");
                return;
            }

            // Usamos el email como base de ID si fuera necesario, pero el DAO creará IDs consistentes.
            // Creamos el DTO con un ID provisional igual al hash del email para mantener la convención de Persona
            double provisionalId = Math.abs(emailField.getText().hashCode());
            ProfesorDTO dto = new ProfesorDTO(
                provisionalId,
                nombresField.getText(),
                apellidosField.getText(),
                emailField.getText(),
                tipoContratoBox.getValue()
            );
            controller.insertar(dto);
            table.setItems(loadData(controller));
            clearForm(nombresField, apellidosField, emailField, tipoContratoBox);
            showAlert("Agregado", "Profesor agregado correctamente.");
        });

        table.getSelectionModel().selectedItemProperty().addListener((obs, oldSel, sel) -> {
            if (sel == null) return;
            nombresField.setText(sel.getNombres());
            apellidosField.setText(sel.getApellidos());
            emailField.setText(sel.getEmail());
            tipoContratoBox.setValue(sel.getTipoContrato());
        });

        updateBtn.setOnAction(e -> {
            Profesor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un profesor de la tabla para actualizar.");
                return;
            }
            if (isBlank(nombresField) || isBlank(apellidosField) || isBlank(emailField) || tipoContratoBox.getValue() == null) {
                showAlert("Campos incompletos", "Completa Nombres, Apellidos, Email y el Tipo de contrato.");
                return;
            }
            // Validación de formato de correo antes de actualizar
            if (!isValidEmail(emailField.getText())) {
                showAlert("Email inválido", "Por favor ingresa un correo electrónico con formato válido.");
                return;
            }
            try {
                ProfesorDTO dto = new ProfesorDTO(
                    selected.getID(),
                    nombresField.getText(),
                    apellidosField.getText(),
                    emailField.getText(),
                    tipoContratoBox.getValue()
                );
                controller.actualizar(dto);
                table.setItems(loadData(controller));
                clearForm(nombresField, apellidosField, emailField, tipoContratoBox);
                showAlert("Actualizado", "Profesor actualizado correctamente.");
            } catch (Exception ex) {
                showAlert("Error", "No se pudo actualizar: " + ex.getMessage());
            }
        });

        deleteBtn.setOnAction(e -> {
            Profesor selected = table.getSelectionModel().getSelectedItem();
            if (selected == null) {
                showAlert("Sin selección", "Selecciona un profesor de la tabla para eliminar.");
                return;
            }
            controller.eliminar(selected.getID());
            table.setItems(loadData(controller));
            clearForm(nombresField, apellidosField, emailField, tipoContratoBox);
            showAlert("Eliminado", "Profesor eliminado correctamente.");
        });

        clearBtn.setOnAction(e -> clearForm(nombresField, apellidosField, emailField, tipoContratoBox));
    }

    private boolean isBlank(TextField tf) {
        return tf.getText() == null || tf.getText().isBlank();
    }

    private ObservableList<Profesor> loadData(ProfesorController controller) {
        return FXCollections.observableArrayList(
            com.mycompany.app.Controller.Mappers.ProfesorMapper.toEntityList(controller.listar())
        );
    }

    private void clearForm(TextField nombresField, TextField apellidosField, TextField emailField, ComboBox<String> tipoContratoBox) {
        nombresField.clear();
        apellidosField.clear();
        emailField.clear();
        tipoContratoBox.setValue(null);
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Información");
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private static boolean isValidEmail(String email) {
        if (email == null) return false;
        String regex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        return Pattern.compile(regex).matcher(email).matches();
    }
}
