package com.mycompany.app.tools;

import com.mycompany.app.ConnectionDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ProfDump {
    public static void main(String[] args) throws Exception {
        try (Connection conn = ConnectionDb.getConnection()) {
            // The profesores table stores a persona_id; join with personas to get name/email
            String sql = "SELECT pr.id as id, p.nombre as nombre, p.apellido as apellido, p.email as email, pr.tipoContrato as tipoContrato FROM profesores pr JOIN personas p ON pr.persona_id = p.id";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("id | nombre | apellido | email | tipoContrato");
                while (rs.next()) {
                    System.out.printf("%s | %s | %s | %s | %s\n",
                            rs.getObject("id"),
                            rs.getString("nombre"),
                            rs.getString("apellido"),
                            rs.getString("email"),
                            rs.getString("tipoContrato")
                    );
                }
            }
        }
    }
}
