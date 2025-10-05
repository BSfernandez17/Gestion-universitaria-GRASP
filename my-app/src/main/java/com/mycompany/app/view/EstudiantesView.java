package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.mycompany.app.Model.Estudiante;
import javafx.scene.control.cell.PropertyValueFactory;
import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Persistence.DAO.EstudianteDAO;
import com.mycompany.app.Persistence.DAO.ProgramaDAO;
import com.mycompany.app.Model.Programa;
import javafx.scene.control.ComboBox;
import java.util.regex.Pattern;
import com.mycompany.app.DTO.EstudianteDTO;
import java.sql.Connection;
import java.util.Optional;

public class EstudiantesView {
    private EstudianteDAO estudianteDAO = null;
    private Estudiante editing = null;
    // RFC-like loose validation but stricter than previous: local@domain.tld with allowed chars
    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,}$", Pattern.CASE_INSENSITIVE);

    public void show(Stage stage) {
    Label title = new Label("Gestión de Estudiantes");
    title.getStyleClass().add("title-label");

        TableView<Estudiante> table = new TableView<>();
        TableColumn<Estudiante, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<Estudiante, Double>("id"));
        TableColumn<Estudiante, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new PropertyValueFactory<Estudiante, String>("nombres"));
        TableColumn<Estudiante, String> apellidosCol = new TableColumn<>("Apellidos");
        apellidosCol.setCellValueFactory(new PropertyValueFactory<Estudiante, String>("apellidos"));
        TableColumn<Estudiante, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<Estudiante, String>("email"));
        TableColumn<Estudiante, String> programaCol = new TableColumn<>("Programa");
        // Use a cell value factory that reads the Programa name safely
        programaCol.setCellValueFactory(cell -> {
            Programa p = cell.getValue().getPrograma();
            String name = p == null ? "-" : p.getNombre();
            return new javafx.beans.property.ReadOnlyStringWrapper(name);
        });
    java.util.List<TableColumn<Estudiante, ?>> cols = new java.util.ArrayList<>();
    cols.add(idCol);
    cols.add(nombresCol);
    cols.add(apellidosCol);
    cols.add(emailCol);
    cols.add(programaCol);
    table.getColumns().addAll(cols);
    // Use a simple lambda-based resize policy to avoid deprecated constants
    table.setColumnResizePolicy(param -> true);

    // UI fields
    ComboBox<Programa> programaBox = new ComboBox<>();
    programaBox.setPrefWidth(220);
    // show meaningful label for Programa
    programaBox.setConverter(new javafx.util.StringConverter<Programa>() {
        @Override
        public String toString(Programa p) { return p == null ? "" : p.getNombre(); }
        @Override
        public Programa fromString(String s) { return null; }
    });

    // Conexión y DAO (con fallback si DB no disponible)
    Connection conn = null;
        try {
            conn = ConnectionDb.getConnection();
            if (conn != null) {
                this.estudianteDAO = new EstudianteDAO(conn);
                table.getItems().addAll(this.estudianteDAO.listar());
                // cargar programas
                ProgramaDAO programaDAO = new ProgramaDAO(conn);
                programaBox.getItems().addAll(programaDAO.listar());
                
            } else {
                new Alert(Alert.AlertType.WARNING, "Base de datos no disponible; trabajando en modo local.").showAndWait();
            }
        } catch (Exception ex) {
            new Alert(Alert.AlertType.WARNING, "No se pudo conectar a la BD: " + ex.getMessage()).showAndWait();
        }

    TextField nombresField = new TextField();
        nombresField.setPromptText("Nombres");
        TextField apellidosField = new TextField();
        apellidosField.setPromptText("Apellidos");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        // Ajustes de ancho para mejorar visibilidad de texto en botones y campos
        nombresField.setPrefWidth(140);
        apellidosField.setPrefWidth(140);
        emailField.setPrefWidth(180);
    Button addBtn = new Button("Agregar");
    Button updateBtn = new Button("Actualizar");
    Button deleteBtn = new Button("Eliminar");
        addBtn.setPrefWidth(100);
        updateBtn.setPrefWidth(110);
        deleteBtn.setPrefWidth(100);
        addBtn.getStyleClass().add("menu-button");
        updateBtn.getStyleClass().add("menu-button");
        deleteBtn.getStyleClass().add("menu-button");
        addBtn.setTooltip(new javafx.scene.control.Tooltip("Alt+A - Agregar estudiante"));
        updateBtn.setTooltip(new javafx.scene.control.Tooltip("Actualizar estudiante seleccionado"));
        deleteBtn.setTooltip(new javafx.scene.control.Tooltip("Eliminar estudiante seleccionado"));
        addBtn.setMnemonicParsing(true);
        addBtn.setText("_Agregar");
        addBtn.setOnAction(e -> {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas agregar este estudiante?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> opt = confirm.showAndWait();
            if (opt.isPresent() && opt.get() == ButtonType.YES) {
                try {
                    // Valores por defecto sencillos para inserción
                    Double id = null;
                    Double codigo = (double) (table.getItems().size() + 1000);
                    Programa selectedPrograma = programaBox.getValue();
                    // Programa y demás se dejan null/por defecto
                    EstudianteDTO dto = new EstudianteDTO(id, nombresField.getText(), apellidosField.getText(), emailField.getText(), codigo, selectedPrograma == null ? null : new com.mycompany.app.DTO.ProgramaDTO(selectedPrograma.getID(), selectedPrograma.getNombre(), selectedPrograma.getDuracion(), selectedPrograma.getRegistro(), null), true, 0.0);
                    if (nombresField.getText().isBlank() || apellidosField.getText().isBlank()) {
                        new Alert(Alert.AlertType.WARNING, "Nombres y apellidos son obligatorios.").showAndWait();
                        return;
                    }
                    // Validación de formato de email: si no está vacío, debe ser válido
                    String emailInput = emailField.getText() == null ? "" : emailField.getText().trim();
                    if (!emailInput.isBlank() && !isValidEmail(emailInput)) {
                        new Alert(Alert.AlertType.WARNING, "El correo ingresado no tiene un formato válido.").showAndWait();
                        return;
                    }

                    if (this.estudianteDAO != null) {
                        estudianteDAO.insertar(dto);
                        table.getItems().clear();
                        table.getItems().addAll(estudianteDAO.listar());
                    } else {
                        // Fallback local: crear Estudiante en memoria
                        Double fakeId = table.getItems().stream().mapToDouble(Estudiante::getID).max().orElse(1000) + 1;
                        Estudiante local = new Estudiante(fakeId, nombresField.getText(), apellidosField.getText(), emailField.getText(), codigo, selectedPrograma, true, 0.0);
                        table.getItems().add(local);
                        new Alert(Alert.AlertType.INFORMATION, "BD no disponible: estudiante agregado en memoria.").showAndWait();
                    }
                    nombresField.clear();
                    apellidosField.clear();
                    emailField.clear();
                    new Alert(Alert.AlertType.INFORMATION, "Estudiante agregado.").showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    // Mostrar alerta con stacktrace para ayudar a diagnosticar
                    Alert err = new Alert(Alert.AlertType.ERROR);
                    err.setTitle("Error al agregar estudiante");
                    err.setHeaderText("Se produjo un error al intentar agregar el estudiante.");
                    err.setContentText(ex.getMessage());
                    // Agregamos el stacktrace en un TextArea expandible
                    StringBuilder sb = new StringBuilder();
                    for (StackTraceElement ste : ex.getStackTrace()) {
                        sb.append(ste.toString()).append("\n");
                    }
                    javafx.scene.control.TextArea ta = new javafx.scene.control.TextArea(sb.toString());
                    ta.setEditable(false);
                    ta.setWrapText(true);
                    ta.setMaxWidth(Double.MAX_VALUE);
                    ta.setMaxHeight(Double.MAX_VALUE);
                    err.getDialogPane().setExpandableContent(ta);
                    err.showAndWait();
                }
            }
        });

        // Actualizar estudiante seleccionado
        updateBtn.setOnAction(e -> {
            if (editing == null) {
                new Alert(Alert.AlertType.WARNING, "Selecciona un estudiante (doble clic) para actualizar.").showAndWait();
                return;
            }
            if (nombresField.getText().isBlank() || apellidosField.getText().isBlank()) {
                new Alert(Alert.AlertType.WARNING, "Nombres y apellidos son obligatorios.").showAndWait();
                return;
            }
            String emailInput = emailField.getText() == null ? "" : emailField.getText().trim();
            if (!emailInput.isBlank() && !isValidEmail(emailInput)) {
                new Alert(Alert.AlertType.WARNING, "El correo ingresado no tiene un formato válido.").showAndWait();
                return;
            }
            Alert confirmUp = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas actualizar este estudiante?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> ru = confirmUp.showAndWait();
            if (ru.isPresent() && ru.get() == ButtonType.YES) {
                try {
                    Double idToUpdate = editing.getID();
                    Programa selectedPrograma = programaBox.getValue();
                    com.mycompany.app.DTO.ProgramaDTO progDto = selectedPrograma == null ? null : new com.mycompany.app.DTO.ProgramaDTO(selectedPrograma.getID(), selectedPrograma.getNombre(), selectedPrograma.getDuracion(), selectedPrograma.getRegistro(), null);
                    EstudianteDTO dto = new EstudianteDTO(idToUpdate, nombresField.getText(), apellidosField.getText(), emailField.getText(), editing.getCodigo(), progDto, editing.getActivo(), editing.getPromedio());
                    if (this.estudianteDAO != null) {
                        estudianteDAO.actualizar(dto);
                        table.getItems().clear();
                        table.getItems().addAll(estudianteDAO.listar());
                    } else {
                        // fallback: update in memory
                        editing = new Estudiante(editing.getID(), nombresField.getText(), apellidosField.getText(), emailField.getText(), editing.getCodigo(), programaBox.getValue(), editing.getActivo(), editing.getPromedio());
                        // replace in table
                        int idx = -1;
                        for (int i=0;i<table.getItems().size();i++) {
                            if (table.getItems().get(i).getID() == editing.getID()) { idx = i; break; }
                        }
                        if (idx >= 0) table.getItems().set(idx, editing);
                    }
                    editing = null;
                    nombresField.clear(); apellidosField.clear(); emailField.clear();
                    new Alert(Alert.AlertType.INFORMATION, "Estudiante actualizado.").showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error al actualizar: " + ex.getMessage()).showAndWait();
                }
            }
        });

        // Eliminar estudiante seleccionado
        deleteBtn.setOnAction(e -> {
            Estudiante sel = table.getSelectionModel().getSelectedItem();
            if (sel == null) { new Alert(Alert.AlertType.WARNING, "Selecciona un estudiante para eliminar.").showAndWait(); return; }
            Alert confirmDel = new Alert(Alert.AlertType.CONFIRMATION, "¿Confirmas eliminar el estudiante seleccionado?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> rd = confirmDel.showAndWait();
            if (rd.isPresent() && rd.get() == ButtonType.YES) {
                try {
                    if (this.estudianteDAO != null) {
                        estudianteDAO.eliminar(sel.getID());
                        table.getItems().clear();
                        table.getItems().addAll(estudianteDAO.listar());
                    } else {
                        table.getItems().remove(sel);
                    }
                    new Alert(Alert.AlertType.INFORMATION, "Estudiante eliminado.").showAndWait();
                } catch (Exception ex) {
                    ex.printStackTrace();
                    new Alert(Alert.AlertType.ERROR, "Error al eliminar: " + ex.getMessage()).showAndWait();
                }
            }
        });

    Button backBtn = new Button("Volver");
    backBtn.setPrefWidth(90);
    backBtn.getStyleClass().add("menu-button");
        backBtn.setOnAction(e -> {
            MainMenuView menu = new MainMenuView();
            menu.show(stage);
        });

        // Mejor layout: GridPane para etiquetas + campos, y HBox para botones
        javafx.scene.layout.GridPane grid = new javafx.scene.layout.GridPane();
        grid.setHgap(10);
        grid.setVgap(8);
        grid.add(new Label("Nombres:"), 0, 0);
        grid.add(nombresField, 1, 0);
        grid.add(new Label("Apellidos:"), 0, 1);
        grid.add(apellidosField, 1, 1);
        grid.add(new Label("Email:"), 2, 0);
        grid.add(emailField, 3, 0);
        grid.add(new Label("Programa:"), 2, 1);
        grid.add(programaBox, 3, 1);

        HBox botonera = new HBox(8, addBtn, updateBtn, deleteBtn, backBtn);
        botonera.setAlignment(Pos.CENTER_LEFT);
        botonera.setPadding(new Insets(6,0,0,0));

    VBox formContainer = new VBox(6, grid, botonera);
    formContainer.setAlignment(Pos.CENTER);
    formContainer.setPadding(new Insets(10));

        // Doble clic en fila para cargar en formulario y activar edición
        table.setRowFactory(tv -> {
            javafx.scene.control.TableRow<Estudiante> row = new javafx.scene.control.TableRow<>();
            row.setOnMouseClicked(event -> {
                if (! row.isEmpty() && event.getButton()== javafx.scene.input.MouseButton.PRIMARY && event.getClickCount() == 2) {
                    Estudiante clicked = row.getItem();
                    editing = clicked;
                    nombresField.setText(clicked.getNombres());
                    apellidosField.setText(clicked.getApellidos());
                    emailField.setText(clicked.getEmail());
                    programaBox.setValue(clicked.getPrograma());
                }
            });
            return row;
        });

    VBox vbox = new VBox(15, title, table, formContainer);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(18));
        vbox.getStyleClass().add("menu-container");

        Scene scene = new Scene(vbox, 760, 520);
        java.net.URL cssUrl = EstudiantesView.class.getResource("/styles/main.css");
        if (cssUrl != null) scene.getStylesheets().add(cssUrl.toExternalForm());

        stage.setTitle("Gestión de Estudiantes");
        stage.setScene(scene);
        stage.show();
    }

    private static boolean isValidEmail(String email) {
        if (email == null) return false;
        return EMAIL_PATTERN.matcher(email).matches();
    }
}
