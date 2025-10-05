package com.mycompany.app.tools;

import com.mycompany.app.ConnectionDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class PersonasDump {
    public static void main(String[] args) throws Exception {
        try (Connection conn = ConnectionDb.getConnection()) {
            String sql = "SELECT id, nombre, apellido, email FROM personas";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("id | nombre | apellido | email");
                while (rs.next()) {
                    System.out.printf("%s | %s | %s | %s\n", rs.getObject("id"), rs.getString("nombre"), rs.getString("apellido"), rs.getString("email"));
                }
            }
        }
    }
}
