package com.mycompany.app;

import javafx.application.Application;
import javafx.stage.Stage;
import com.mycompany.app.view.MainMenuView;
import com.mycompany.app.view.UiContext;

public class MainFX extends Application {
    @Override
    public void start(Stage stage) {
        // Asegura datos mínimos para operar la UI (Programa existente)
        UiContext.ensureSeedForUi();
        // Abre el menú principal
        new MainMenuView().show(stage);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
