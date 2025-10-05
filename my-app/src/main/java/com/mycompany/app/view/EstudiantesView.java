package com.mycompany.app.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import com.mycompany.app.Model.Estudiante;
import javafx.scene.control.cell.PropertyValueFactory;

public class EstudiantesView {
    public void show(Stage stage) {
        Label title = new Label("Gestión de Estudiantes");
        title.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");

        TableView<Estudiante> table = new TableView<>();
        TableColumn<Estudiante, Double> idCol = new TableColumn<>("ID");
        idCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        TableColumn<Estudiante, String> nombresCol = new TableColumn<>("Nombres");
        nombresCol.setCellValueFactory(new PropertyValueFactory<>("nombres"));
        TableColumn<Estudiante, String> apellidosCol = new TableColumn<>("Apellidos");
        apellidosCol.setCellValueFactory(new PropertyValueFactory<>("apellidos"));
        TableColumn<Estudiante, String> emailCol = new TableColumn<>("Email");
        emailCol.setCellValueFactory(new PropertyValueFactory<>("email"));
        table.getColumns().addAll(idCol, nombresCol, apellidosCol, emailCol);

        TextField nombresField = new TextField();
        nombresField.setPromptText("Nombres");
        TextField apellidosField = new TextField();
        apellidosField.setPromptText("Apellidos");
        TextField emailField = new TextField();
        emailField.setPromptText("Email");
        Button addBtn = new Button("Agregar");
        HBox form = new HBox(10, nombresField, apellidosField, emailField, addBtn);
        form.setAlignment(Pos.CENTER);
        form.setPadding(new Insets(10));

        VBox vbox = new VBox(15, title, table, form);
        vbox.setAlignment(Pos.CENTER);
        vbox.setPadding(new Insets(20));

        Scene scene = new Scene(vbox, 600, 400);
        stage.setTitle("Gestión de Estudiantes");
        stage.setScene(scene);
        stage.show();
    }
}
