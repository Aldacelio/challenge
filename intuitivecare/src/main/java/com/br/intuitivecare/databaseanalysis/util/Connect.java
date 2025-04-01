package com.br.intuitivecare.databaseanalysis.util;

import java.sql.Connection;
import java.sql.DriverManager;

import io.github.cdimascio.dotenv.Dotenv;

public class Connect {

    public static Connection getConnection() throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USERNAME");
        String dbPass = dotenv.get("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPass == null) {
            throw new RuntimeException("Credenciais do banco não encontradas no .env");
        }

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }
}
