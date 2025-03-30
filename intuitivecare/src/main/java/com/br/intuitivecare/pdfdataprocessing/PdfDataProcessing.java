package com.br.intuitivecare.pdfdataprocessing;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.br.intuitivecare.pdfdataprocessing.services.CsvWriter;
import com.br.intuitivecare.pdfdataprocessing.services.PdfProcessor;
import com.br.intuitivecare.utils.ConfigManager;
import com.br.intuitivecare.utils.FileUtils;
import com.br.intuitivecare.utils.ZipFiles;
import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.utils.ui.ProgressDialog;

public class PdfDataProcessing {

    public static void main(String[] args) {

        String pdfPath = Paths.get("downloads/Anexo I.pdf").toString();
        String outputDir = ConfigManager.get("pdfprocessing.csv.dir");
        String baseName = "Teste_" + FileUtils.extractName(pdfPath);
        
        try {
            Files.createDirectories(Paths.get(outputDir));
        } catch (IOException e) {
            Dialog.showError("Erro ao criar diretório de saída: " + outputDir);
            return;
        }
        
        String csvPath = Paths.get(outputDir, baseName + ".csv").toString();
        String zipPath = Paths.get(outputDir, baseName + ".zip").toString();

        try {
            if (!Files.exists(Paths.get(pdfPath))) {
                Dialog.showError("Arquivo Anexo_I.pdf não encontrado em: " + pdfPath);
                return;
            }

            ProgressDialog.show("Extraindo dados do PDF...", "Processando Anexo I");
            PdfProcessor processor = new PdfProcessor();
            List<String[]> tableData = processor.extractTableData(pdfPath);
            ProgressDialog.close("Processando Anexo I");

            replaceColumnAbbreviations(tableData);

            ProgressDialog.show("Gerando arquivo CSV...", "Salvando Dados");
            CsvWriter csvWriter = new CsvWriter();
            csvWriter.writeToCsv(tableData, csvPath);
            ProgressDialog.close("Salvando Dados");

            List<String> filesToZip = new ArrayList<>();
            filesToZip.add(csvPath);
            new ZipFiles().createZip(filesToZip, zipPath);

            Dialog.showMessage(
                    "Processo concluído! Arquivo ZIP gerado em: " + zipPath,
                    "Sucesso"
            );

        } catch (IOException e) {
            Dialog.showError("Erro durante o processamento: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static void replaceColumnAbbreviations(List<String[]> tableData) {
        for (String[] row : tableData) {
            for (int i = 0; i < row.length; i++) {
                if (row[i] != null) {
                    row[i] = row[i]
                            .replace("OD", "Seg. Odontológica")
                            .replace("AMB", "Seg. Ambulatorial");
                }
            }
        }
    }
}