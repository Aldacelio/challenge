package com.br.intuitivecare.databaseanalysis;

import java.sql.Connection;

import javax.swing.JOptionPane;

import com.br.intuitivecare.databaseanalysis.service.AnalysisData;
import com.br.intuitivecare.databaseanalysis.service.CreateTable;
import com.br.intuitivecare.databaseanalysis.service.ImportData;
import com.br.intuitivecare.databaseanalysis.util.Connect;
import com.br.intuitivecare.utils.ui.Dialog;

public class DatabaseAnalysis {

    public static void main(String[] args) {
        try (Connection conn = Connect.getConnection()) {
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
                    case 0:
                        new CreateTable().create(conn);
                        break;
                    case 1:
                        new ImportData().executeImport(conn);
                        break;
                    case 2:
                        new AnalysisData().showTopExpenses(conn);
                        break;
                    default:
                        return;
                }
            }
        } catch (Exception e) {
            Dialog.showError("Erro: " + e.getMessage());
        }
    }
}
