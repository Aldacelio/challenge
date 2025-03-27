package com.br.intuitivecare.webscraping;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static final String URL_FILES = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_DIR = "downloads/";
    private static final String ZIP_FILE = DOWNLOAD_DIR + "anexos.zip";

    public static void main(String[] args) {
        try {
            new File(DOWNLOAD_DIR).mkdirs();
            List<String> pdfFiles = downloadAnexos();

            if (continueZip()) {
                if (!pdfFiles.isEmpty()) {
                    createZip(pdfFiles);
                    showMessage("Processo concluído!", "Sucesso");
                } else {
                    showMessage("Nenhum arquivo foi baixado, mas o ZIP foi substituído", "Informação");
                }
            } else {
                showMessage("Criação do ZIP cancelada pelo usuário", "Operação cancelada");
            }
        } catch (IOException e) {
            showError("Erro: " + e.getMessage());
        }
    }

    private static List<String> downloadAnexos() throws IOException {
        Document doc = Jsoup.connect(URL_FILES).get();
        Elements pdfLinks = doc.select("a[href$=.pdf]");
        List<String> downloadedFiles = new ArrayList<>();

        for (Element link : pdfLinks) {
            String pdfUrl = link.attr("abs:href");
            String fileName = DOWNLOAD_DIR + link.text().trim() + ".pdf";

            if (fileName.contains("Anexo I") || fileName.contains("Anexo II")) {
                File targetFile = new File(fileName);
                
                if (targetFile.exists()) {
                    if (!confirm("O arquivo " + targetFile.getName() + " já existe. Deseja substituir?")) {
                        continue;
                    }
                }

                showMessage((targetFile.exists() ? "Substituindo: " : "Baixando: ") + fileName, "Download");
                try (InputStream in = new URL(pdfUrl).openStream()) {
                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(fileName);
                }
            }
        }
        return downloadedFiles;
    }

    private static boolean continueZip() {
        File zipFile = new File(ZIP_FILE);
        if (zipFile.exists()) {
            return confirm("O arquivo " + zipFile.getName() + " já existe. Deseja substituir?");
        }
        return true;
    }

    private static void createZip(List<String> files) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(ZIP_FILE))) {
            for (String file : files) {
                zipOut.putNextEntry(new ZipEntry(new File(file).getName()));
                Files.copy(Paths.get(file), zipOut);
            }
        }
    }

    private static boolean confirm(String message) {
        int response = JOptionPane.showConfirmDialog(null, 
            message, 
            "Confirmação", 
            JOptionPane.YES_NO_OPTION);
        return response == JOptionPane.YES_OPTION;
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(null, 
            message, 
            "Erro", 
            JOptionPane.ERROR_MESSAGE);
    }

    private static void showMessage(String message, String title) {
        JOptionPane.showMessageDialog(null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE);
    }

}
