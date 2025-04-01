package com.br.intuitivecare.databaseanalysis.util;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class CheckExist {
    
    public static boolean tables(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.executeQuery("SELECT 1 FROM operadoras LIMIT 1");
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static boolean data(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT EXISTS (SELECT 1 FROM demonstracoes)");
            rs.next();
            return rs.getBoolean(1);
        }
    }
}