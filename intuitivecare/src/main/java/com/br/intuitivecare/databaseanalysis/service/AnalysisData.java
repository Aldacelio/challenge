package com.br.intuitivecare.databaseanalysis.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.swing.JOptionPane;

import com.br.intuitivecare.utils.ui.ProgressDialog;

public class AnalysisData {

    private static final String ANALYTICAL_QUERIES_PATH = "src/main/resources/db/analytical_queries.sql";

    public void showTopExpenses(Connection conn) throws Exception {
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

        if (choice == 2) {
            return;
        }

        String sql = Files.readString(Paths.get(ANALYTICAL_QUERIES_PATH));
        String[] queries = sql.split(";");
        String query = queries[choice].trim();

        ProgressDialog.show("Anlisando dados...", "Resultado");
        try (Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

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
            ProgressDialog.close("Resultado");

            JOptionPane.showMessageDialog(
                    null,
                    new javax.swing.JTextArea(result.toString()),
                    "Resultado da Consulta",
                    JOptionPane.INFORMATION_MESSAGE
            );
        }
    }
}
