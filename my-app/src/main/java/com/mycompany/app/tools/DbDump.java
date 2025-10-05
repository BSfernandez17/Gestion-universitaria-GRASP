package com.mycompany.app.tools;

import com.mycompany.app.ConnectionDb;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DbDump {
    public static void main(String[] args) throws Exception {
        try (Connection conn = ConnectionDb.getConnection()) {
            String sql = "SELECT e.id, p.nombre as persona, e.codigo, e.programa_id, pr.nombre as programa FROM estudiantes e LEFT JOIN personas p ON e.id = p.id LEFT JOIN programas pr ON e.programa_id = pr.id";
            try (PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
                System.out.println("id | persona | codigo | programa_id | programa");
                while (rs.next()) {
                    System.out.printf("%s | %s | %s | %s | %s\n", rs.getObject("id"), rs.getString("persona"), rs.getObject("codigo"), rs.getObject("programa_id"), rs.getString("programa"));
                }
            }
        }
    }
}
