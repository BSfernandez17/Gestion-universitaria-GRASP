package com.mycompany.app.Persistence.DAO;

public class IdGenerator {
    private static double counter = 1000; // valor inicial

    public static synchronized double generateId() {
        counter += 1; // incrementa en 1 cada vez
        return counter;
    }
}
