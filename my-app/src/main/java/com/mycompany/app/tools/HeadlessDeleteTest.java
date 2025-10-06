package com.mycompany.app.tools;

import com.mycompany.app.ConnectionDb;
import com.mycompany.app.Persistence.DAO.CursoDAO;

public class HeadlessDeleteTest {
    public static void main(String[] args) {
        try {
            System.out.println("HeadlessDeleteTest starting");
            var conn = ConnectionDb.createConnection();
            CursoDAO dao = new CursoDAO(conn);
            System.out.println("Deleting curso id=1");
            dao.eliminar(1.0);
            System.out.println("Delete call returned");
            conn.close();
            System.out.println("HeadlessDeleteTest finished");
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(2);
        }
    }
}
