package com.mycompany.app;

import javafx.application.Application;
import javafx.stage.Stage;
import com.mycompany.app.view.MainMenuView;
import com.mycompany.app.seeders.DatabaseSeed;
import java.sql.Connection;
import com.mycompany.app.ConnectionDb;

public class MainFX extends Application {

    @Override
    public void start(Stage stage) throws Exception {
    // Application startup
        // Ensure seed data is present (run once at startup)
        try {
            Connection conn = ConnectionDb.getConnection();
            // Run database seed (if necessary)
            DatabaseSeed.seed(conn);
            try (java.sql.Statement st = conn.createStatement()) {
                java.sql.ResultSet r1 = st.executeQuery("SELECT COUNT(*) AS c FROM personas");
                if (r1.next()) System.out.println("[DB] personas=" + r1.getInt("c"));
                java.sql.ResultSet r2 = st.executeQuery("SELECT COUNT(*) AS c FROM estudiantes");
                if (r2.next()) System.out.println("[DB] estudiantes=" + r2.getInt("c"));
            } catch (Exception ex) {
                // intentionally silent during normal startup; errors will be logged if they occur
            }
        } catch (Exception e) {
            System.err.println("[ERROR] Failed to run seed: " + e.getMessage());
            e.printStackTrace();
        }

        MainMenuView menu = new MainMenuView();
        menu.show(stage);
    // Main menu shown
    }

    public static void main(String[] args) {
        launch(args);
    }
}
