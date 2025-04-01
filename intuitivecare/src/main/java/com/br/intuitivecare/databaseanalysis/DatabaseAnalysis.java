package com.br.intuitivecare.databaseanalysis;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.Instant;

import javax.swing.JOptionPane;

import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.utils.ui.ProgressDialog;

import io.github.cdimascio.dotenv.Dotenv;

public class DatabaseAnalysis {

    private static final String SCHEMA_PATH = "src/main/resources/db/schema.sql";
    private static final String IMPORT_FUNCTIONS_PATH = "src/main/resources/db/import_data.sql";
    private static final String ANALYTICAL_QUERIES_PATH = "src/main/resources/db/analytical_queries.sql";
    private static final String DATA_PATH = "src/main/resources/data";

    public static void main(String[] args) {
        try (Connection conn = getConnection()) {
            while (true) {
                String[] options = {
                    "Criar tabelas",
                    "Importar dados",
                    "Top 10 maiores despesas",
                    "Sair"
                };
                
                int choice = JOptionPane.showOptionDialog(
                    null,
                    "Selecione uma operação:",
                    "Análise de Dados",
                    JOptionPane.DEFAULT_OPTION,
                    JOptionPane.INFORMATION_MESSAGE,
                    null,
                    options,
                    options[0]
                );

                switch (choice) {
                    case 0: criarTabelas(conn); break;
                    case 1: importarDados(conn); break;
                    case 2: mostrarTopDespesas(conn); break;
                    default: return;
                }
            }
        } catch (Exception e) {
            Dialog.showError("Erro: " + e.getMessage());
        }
    }

    private static void criarTabelas(Connection conn) throws Exception {
        Dialog.showMessage("Criando tabelas", "Criando estrutura do banco");
        
        executarScript(conn, SCHEMA_PATH);
        Dialog.showMessage("Tabelas criadas com sucesso!", "Concluído");
    }

    private static void importarDados(Connection conn) throws Exception {
        limparTabelas(conn);
        Instant inicio = Instant.now();
        String resourcesPath = Paths.get(DATA_PATH).toAbsolutePath().toString().replace("\\", "/");
        
        executarScript(conn, IMPORT_FUNCTIONS_PATH, resourcesPath);
        ProgressDialog.show("Importando dados...", "Importando Dados");

        int totalOperadoras = importarOperadoras(conn, resourcesPath);
        int totalDemonstracoes = importarDemonstracoes(conn, resourcesPath);

        ProgressDialog.close("Importando Dados");
        Duration duracao = Duration.between(inicio, Instant.now());
        
        Dialog.showMessage(String.format(
            "Importação concluída!\n\nTotal importado:\n- %d operadoras\n- %d demonstrações\n\nTempo: %d min e %d seg",
            totalOperadoras, totalDemonstracoes, duracao.toMinutes(), duracao.getSeconds() % 60
        ), "Concluído");
    }

    private static void mostrarTopDespesas(Connection conn) throws Exception {
        String[] options = {"Último Trimestre", "Último Ano", "Cancelar"};
        int choice = JOptionPane.showOptionDialog(
            null,
            "Selecione o período:",
            "Top 10 Despesas",
            JOptionPane.DEFAULT_OPTION,
            JOptionPane.INFORMATION_MESSAGE,
            null,
            options,
            options[0]
        );

        if (choice == 2) return;

        String sql = Files.readString(Paths.get(ANALYTICAL_QUERIES_PATH));
        String[] queries = sql.split(";");
        String query = queries[choice].trim();

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            
            StringBuilder result = new StringBuilder();
            result.append(String.format("Top 10 Maiores Despesas - %s\n\n", options[choice]));
            result.append(String.format("%-10s %-50s %15s\n", "ANS", "Razão Social", "Despesa"));
            result.append("-".repeat(75)).append("\n");
            
            while (rs.next()) {
                result.append(String.format("%-10s %-50s %,15.2f\n",
                    rs.getString(1),
                    rs.getString(2),
                    rs.getDouble(3)
                ));
            }

            JOptionPane.showMessageDialog(
                null,
                new javax.swing.JTextArea(result.toString()),
                "Resultado da Consulta",
                JOptionPane.INFORMATION_MESSAGE
            );
        }
    }

    private static Connection getConnection() throws Exception {
        Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
        String dbUrl = dotenv.get("DB_URL");
        String dbUser = dotenv.get("DB_USERNAME");
        String dbPass = dotenv.get("DB_PASSWORD");

        if (dbUrl == null || dbUser == null || dbPass == null) {
            throw new RuntimeException("Credenciais do banco não encontradas no .env");
        }

        return DriverManager.getConnection(dbUrl, dbUser, dbPass);
    }

    private static void limparTabelas(Connection conn) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE demonstracoes_contabeis, operadoras CASCADE");
        }
    }

    private static int importarOperadoras(Connection conn, String basePath) throws Exception {
        String sql = "SELECT import_operadoras(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, basePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private static int importarDemonstracoes(Connection conn, String basePath) throws Exception {
        String sql = "SELECT import_demonstracoes(?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, basePath);
            try (ResultSet rs = pstmt.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        }
    }

    private static void executarScript(Connection conn, String scriptPath, String... params) throws Exception {
        try (Statement stmt = conn.createStatement()) {
            String sql = Files.readString(Paths.get(scriptPath));

            // Substitui parâmetros se houver
            if (params.length > 0) {
                sql = sql.replace("[BASE_PATH]", params[0]);
            }
            stmt.execute(sql);
        }
    }
}
