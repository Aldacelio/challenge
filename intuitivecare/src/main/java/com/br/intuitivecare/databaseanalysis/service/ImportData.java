package com.br.intuitivecare.databaseanalysis.service;

import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.Duration;
import java.time.Instant;

import com.br.intuitivecare.databaseanalysis.util.ScriptExecute;
import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.utils.ui.ProgressDialog;

public class ImportData {

    private static final String IMPORT_FUNCTIONS_PATH = "src/main/resources/db/import_data.sql";
    private static final String DATA_PATH = "src/main/resources/data";

    public void executeImport(Connection conn) throws Exception {
        Instant start = Instant.now();
        String resourcesPath = Paths.get(DATA_PATH).toAbsolutePath().toString().replace("\\", "/");

        ProgressDialog.show("Importando dados...", "Importando Dados");
        execute(conn);

        int totalOperators = importOperators(conn, resourcesPath);
        int totalDemonstration = importDemonstration(conn, resourcesPath);

        ProgressDialog.close("Importando Dados");
        Duration duration = Duration.between(start, Instant.now());

        Dialog.showMessage(String.format(
                "Importação concluída!\n\nTotal importado:\n- %d operadoras\n- %d demonstrações\n\nTempo: %d min e %d seg",
                totalOperators, totalDemonstration, duration.toMinutes(), duration.getSeconds() % 60
        ), "Concluído");
    }

    private void execute(Connection conn) throws Exception {
        String resourcesPath = Paths.get(DATA_PATH).toAbsolutePath().toString().replace("\\", "/");
        ScriptExecute.execute(conn, IMPORT_FUNCTIONS_PATH, resourcesPath);
    }

    public int importOperators(Connection conn, String basePath) throws Exception {
        String sql = "SELECT import_operadoras(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, basePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    public int importDemonstration(Connection conn, String basePath) throws Exception {
        String sql = "SELECT import_demonstracoes(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, basePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }
}
