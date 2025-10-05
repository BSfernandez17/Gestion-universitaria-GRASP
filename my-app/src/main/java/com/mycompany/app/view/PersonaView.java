package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
// ...existing code...
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;

import java.sql.Connection;
import java.util.Optional;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mycompany.app.Model.Persona;
import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Persistence.DAO.PersonaDAO;
import com.mycompany.app.DTO.PersonaDTO;

public class PersonaView {

    private final javafx.collections.ObservableList<Persona> data = javafx.collections.FXCollections.observableArrayList();
    private Persona editing = null;
    private PersonaDAO personaDAO;
    private Connection conn;
    private TableView<Persona> table;
    private TextField nombresField, apellidosField, emailField;
    private CheckBox estudianteChk, profesorChk, decanoChk;
    private Button addBtn, updateBtn, deleteBtn, backBtn;

    private static final Logger LOGGER = Logger.getLogger(PersonaView.class.getName());
    private static final Pattern EMAIL_PATTERN =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public void show(Stage stage) {
    // Use the shared singleton connection; don't close it here.
    this.conn = ConnectionDb.getConnection();
    personaDAO = new PersonaDAO(this.conn);
        try {
            Label title = new Label("Gestión de Personas");
            title.getStyleClass().add("title-label");

            table = createTable();
            reloadTable();

            GridPane form = createForm();
            HBox botones = createButtons(stage);

            VBox root = new VBox(15, title, table, new Separator(), form, botones);
            root.setAlignment(Pos.CENTER);
            root.setPadding(new Insets(20));
            root.getStyleClass().add("menu-container");

            Scene scene = new Scene(root, 750, 500);
            java.net.URL cssUrl = PersonaView.class.getResource("/styles/main.css");
            if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

            stage.setTitle("Gestión de Personas");
            stage.setScene(scene);
            stage.show();
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error al inicializar PersonaView", ex);
            showAlert("Error al conectar con la base de datos: " + ex.getMessage(), Alert.AlertType.ERROR);
        }
    }

    /** ------------------------------ CREACIÓN DE COMPONENTES ------------------------------ */

    private TableView<Persona> createTable() {
        TableView<Persona> tableView = new TableView<>();

    TableColumn<Persona, Double> idCol = new TableColumn<>("ID");
    idCol.setCellValueFactory(c -> new ReadOnlyObjectWrapper<Double>(c.getValue().getID()));

    TableColumn<Persona, String> nombresCol = new TableColumn<>("Nombres");
    nombresCol.setCellValueFactory(new PropertyValueFactory<Persona, String>("nombres"));

    TableColumn<Persona, String> apellidosCol = new TableColumn<>("Apellidos");
    apellidosCol.setCellValueFactory(new PropertyValueFactory<Persona, String>("apellidos"));

    TableColumn<Persona, String> emailCol = new TableColumn<>("Email");
    emailCol.setCellValueFactory(new PropertyValueFactory<Persona, String>("email"));

    TableColumn<Persona, String> tipoCol = new TableColumn<>("Tipo");
    tipoCol.setCellValueFactory(cell -> {
        Persona p = cell.getValue();
        String tipo = "";
        try {
            tipo = getRolesForPersona(p.getID());
        } catch (Exception ex) {
            tipo = "?";
        }
        return new javafx.beans.property.ReadOnlyStringWrapper(tipo);
    });

    java.util.List<TableColumn<Persona, ?>> cols = new java.util.ArrayList<>();
    cols.add(idCol);
    cols.add(nombresCol);
    cols.add(apellidosCol);
    cols.add(emailCol);
    cols.add(tipoCol);
    tableView.getColumns().addAll(cols);
    tableView.setItems(data);
    // Use a lambda-based policy to avoid deprecated constant
    tableView.setColumnResizePolicy(param -> true);

        // Doble clic para editar
        tableView.setRowFactory(tv -> {
            TableRow<Persona> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getButton() == MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Persona clicked = row.getItem();
                    editing = clicked;
                    nombresField.setText(clicked.getNombres());
                    apellidosField.setText(clicked.getApellidos());
                    emailField.setText(clicked.getEmail());
                }
            });
            return row;
        });

        return tableView;
    }

    private GridPane createForm() {
        nombresField = new TextField();
        apellidosField = new TextField();
        emailField = new TextField();

        nombresField.setPromptText("Nombres");
        apellidosField.setPromptText("Apellidos");
        emailField.setPromptText("Email");

    nombresField.setPrefWidth(180);
    apellidosField.setPrefWidth(180);
    emailField.setPrefWidth(220);

    estudianteChk = new CheckBox("Estudiante");
    profesorChk = new CheckBox("Profesor");
    decanoChk = new CheckBox("Decano");

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.add(new Label("Nombres:"), 0, 0);
        grid.add(nombresField, 1, 0);
        grid.add(new Label("Apellidos:"), 0, 1);
        grid.add(apellidosField, 1, 1);
        grid.add(new Label("Email:"), 2, 0);
        grid.add(emailField, 3, 0);
        HBox roles = new HBox(8, estudianteChk, profesorChk, decanoChk);
        grid.add(new Label("Roles:"), 0, 2);
        grid.add(roles, 1, 2, 3, 1);
        return grid;
    }

    private HBox createButtons(Stage stage) {
        addBtn = new Button("Agregar");
        updateBtn = new Button("Actualizar");
        deleteBtn = new Button("Eliminar");
        backBtn = new Button("Volver");

        addBtn.setPrefWidth(100);
        updateBtn.setPrefWidth(110);
        deleteBtn.setPrefWidth(100);
        backBtn.setPrefWidth(90);

        addBtn.getStyleClass().add("menu-button");
        updateBtn.getStyleClass().add("menu-button");
        deleteBtn.getStyleClass().add("menu-button");
        backBtn.getStyleClass().add("menu-button");

        addBtn.setOnAction(e -> onAdd());
        updateBtn.setOnAction(e -> onUpdate());
        deleteBtn.setOnAction(e -> onDelete());

        // Desactivar update/delete cuando no hay selección
        updateBtn.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));
        deleteBtn.disableProperty().bind(Bindings.isNull(table.getSelectionModel().selectedItemProperty()));

        backBtn.setOnAction(e -> {
            MainMenuView menu = new MainMenuView();
            menu.show(stage);
        });

        HBox botonera = new HBox(8, addBtn, updateBtn, deleteBtn, backBtn);
        botonera.setAlignment(Pos.CENTER_LEFT);
        botonera.setPadding(new Insets(6, 0, 0, 0));
        return botonera;
    }

    /** ------------------------------ ACCIONES CRUD ------------------------------ */

    private void onAdd() {
        if (!validateInputs()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas agregar esta persona?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> opt = confirm.showAndWait();
        if (opt.isPresent() && opt.get() == ButtonType.YES) {
            try {
                PersonaDTO dto = new PersonaDTO(null,
                        nombresField.getText().trim(),
                        apellidosField.getText().trim(),
                        emailField.getText().trim());
                // Insert persona and get id
                Double personaId = personaDAO.insertar(dto);
                if (personaId == null) throw new Exception("No se pudo insertar persona");

                // create profesor/estudiante rows if requested
                if (profesorChk.isSelected()) {
                    com.mycompany.app.Persistence.DAO.ProfesorDAO prDao = new com.mycompany.app.Persistence.DAO.ProfesorDAO(conn);
                    prDao.insertarForPersona(personaId, "Sin especificar");
                }
                if (estudianteChk.isSelected()) {
                    com.mycompany.app.Persistence.DAO.EstudianteDAO esDao = new com.mycompany.app.Persistence.DAO.EstudianteDAO(conn);
                    esDao.insertarForPersona(personaId, null, null);
                }

                reloadTable();
                clearFields();
                showAlert("Persona agregada correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error al insertar persona", ex);
                showAlert("No fue posible insertar en la BD: " + ex.getMessage(), Alert.AlertType.WARNING);
            }
        }
    }

    private void onUpdate() {
        Persona selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Selecciona una persona para actualizar.", Alert.AlertType.WARNING);
            return;
        }
        if (!validateInputs()) return;

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas actualizar esta persona?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> r = confirm.showAndWait();
        if (r.isPresent() && r.get() == ButtonType.YES) {
            try {
                PersonaDTO dto = new PersonaDTO(selected.getID(),
                        nombresField.getText().trim(),
                        apellidosField.getText().trim(),
                        emailField.getText().trim());
                personaDAO.actualizar(dto);
                reloadTable();
                clearFields();
                showAlert("Persona actualizada correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error al actualizar persona", ex);
                showAlert("Error al actualizar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    private void onDelete() {
        Persona selected = table.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar la persona seleccionada?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> res = confirmDel.showAndWait();
        if (res.isPresent() && res.get() == ButtonType.YES) {
            try {
                personaDAO.eliminar(selected.getID());
                reloadTable();
                showAlert("Persona eliminada correctamente.", Alert.AlertType.INFORMATION);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, "Error al eliminar persona", ex);
                showAlert("Error al eliminar: " + ex.getMessage(), Alert.AlertType.ERROR);
            }
        }
    }

    /** ------------------------------ MÉTODOS AUXILIARES ------------------------------ */

    private void reloadTable() {
        try {
            data.setAll(personaDAO.listar());
        } catch (Exception ex) {
            LOGGER.log(Level.WARNING, "No se pudieron recargar los datos", ex);
        }
    }

    private void clearFields() {
        nombresField.clear();
        apellidosField.clear();
        emailField.clear();
        editing = null;
    }

    // Return roles for persona: Estudiante, Profesor, Decano (Facultad), or Persona
    private String getRolesForPersona(double personaId) {
        StringBuilder sb = new StringBuilder();
        try {
            // estudiante: estudiantes.id == personas.id
            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM estudiantes WHERE id = ?")) {
                ps.setDouble(1, personaId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { if (sb.length()>0) sb.append(", "); sb.append("Estudiante"); } }
            }
            // profesor: profesores.persona_id
            try (PreparedStatement ps = conn.prepareStatement("SELECT 1 FROM profesores WHERE persona_id = ?")) {
                ps.setDouble(1, personaId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { if (sb.length()>0) sb.append(", "); sb.append("Profesor"); } }
            }
            // decano: facultades.decano_id
            try (PreparedStatement ps = conn.prepareStatement("SELECT nombre FROM facultades WHERE decano_id = ?")) {
                ps.setDouble(1, personaId);
                try (ResultSet rs = ps.executeQuery()) { if (rs.next()) { if (sb.length()>0) sb.append(", "); sb.append("Decano"); } }
            }
        } catch (SQLException ex) {
            // ignore - return whatever we found
        }
        return sb.length() == 0 ? "Persona" : sb.toString();
    }

    private boolean validateInputs() {
        boolean valid = true;

        nombresField.setStyle(null);
        apellidosField.setStyle(null);
        emailField.setStyle(null);

        if (nombresField.getText().trim().isEmpty()) {
            nombresField.setStyle("-fx-border-color: red;");
            valid = false;
        }
        if (apellidosField.getText().trim().isEmpty()) {
            apellidosField.setStyle("-fx-border-color: red;");
            valid = false;
        }

        String email = emailField.getText().trim();
        if (!email.isEmpty() && !EMAIL_PATTERN.matcher(email).matches()) {
            emailField.setStyle("-fx-border-color: red;");
            showAlert("El correo ingresado no tiene un formato válido.", Alert.AlertType.WARNING);
            valid = false;
        }

        if (!valid) showAlert("Por favor, corrige los campos resaltados.", Alert.AlertType.WARNING);
        return valid;
    }

    private void showAlert(String msg, Alert.AlertType type) {
        new Alert(type, msg).showAndWait();
    }
}
