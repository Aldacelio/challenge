package com.br.intuitivecare.databaseanalysis;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

public class DatabaseAnalysis {
    
    public static void main(String[] args) {
        try {
            // 1. Carrega variáveis de ambiente
            Dotenv dotenv = Dotenv.configure().ignoreIfMissing().load();
            
            String dbUrl = dotenv.get("DB_URL");
            String dbUser = dotenv.get("DB_USERNAME");
            String dbPass = dotenv.get("DB_PASSWORD");
            
            // 2. Validação básica das credenciais
            if (dbUrl == null || dbUser == null || dbPass == null) {
                throw new RuntimeException("Variáveis DB_URL, DB_USERNAME ou DB_PASSWORD não encontradas no .env");
            }
            
            // 3. Conexão e execução do schema
            try (Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPass);
                 Statement stmt = conn.createStatement()) {
                
                // Caminho corrigido (assumindo estrutura Maven padrão)
                String schemaPath = "src/main/resources/db/schema.sql";
                String createTables = Files.readString(Paths.get(schemaPath));
                
                stmt.execute(createTables);
                System.out.println("Tabelas criadas com sucesso!");
            }
            
        } catch (DotenvException e) {
            System.err.println("Arquivo .env não encontrado ou inválido");
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Erro durante a execução");
            e.printStackTrace();
        }
    }
}