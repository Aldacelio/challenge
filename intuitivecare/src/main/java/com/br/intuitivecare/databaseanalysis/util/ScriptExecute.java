package com.br.intuitivecare.databaseanalysis.util;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.Statement;

public class ScriptExecute {

    public static void execute(Connection conn, String scriptPath, String... params) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            String sql = Files.readString(Paths.get(scriptPath));
            if (params.length > 0) {
                sql = sql.replace("[BASE_PATH]", params[0]);
            }
            stmt.execute(sql);
        }
    }
}
