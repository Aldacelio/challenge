package com.br.intuitivecare.webscraping;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Scraper {

    private static final String URL_FILES = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_DIR = "downloads/";
    private static final String ZIP_FILE = DOWNLOAD_DIR + "anexos.zip";
    private static final Map<String, JDialog> dialogMap = new HashMap<>();

    public static void main(String[] args) {
        try {
            new File(DOWNLOAD_DIR).mkdirs();
            List<String> pdfFiles = downloadAnexos();
            
            if (continueZip()) {
                if (!pdfFiles.isEmpty()) {
                    createZip(pdfFiles);
                    showMessage("Processo concluído!", "Sucesso");
                } else {
                    showMessage("Nenhum arquivo foi baixado", "Informação");
                }
            } else {
                showMessage("Compactação cancelada pelo usuário", "Operação cancelada");
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

                String action = targetFile.exists() ? "Substituindo: " : "Baixando: ";
                String title = "Progresso do Download";
                showProgressMessage(action + fileName, title);
                
                try (InputStream in = new URL(pdfUrl).openStream()) {
                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(fileName);
                } finally {
                    closeProgressMessage(title);
                }
            }
        }
        return downloadedFiles;
    }

    private static void showProgressMessage(String message, String title) {
        SwingUtilities.invokeLater(() -> {
            JProgressBar progressBar = new JProgressBar();
            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString(message);

            JPanel panel = new JPanel(new BorderLayout());
            panel.add(progressBar, BorderLayout.CENTER);
            panel.setPreferredSize(new Dimension(300, 70));

            JOptionPane optionPane = new JOptionPane(panel, JOptionPane.INFORMATION_MESSAGE);
            optionPane.setOptions(new Object[0]);
            JDialog dialog = optionPane.createDialog(title);
            dialog.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
            dialogMap.put(title, dialog);
            dialog.setVisible(true);
        });
    }

    private static void closeProgressMessage(String title) {
        SwingUtilities.invokeLater(() -> {
            JDialog dialog = dialogMap.remove(title);
            if (dialog != null) {
                dialog.dispose();
            }
        });
    }

    private static boolean continueZip() {
        File zipFile = new File(ZIP_FILE);
        if (zipFile.exists()) {
            return confirm("O arquivo " + zipFile.getName() + " já existe. Deseja substituir?");
        }
        return true;
    }

    private static void createZip(List<String> files) throws IOException {
        showProgressMessage("Compactando arquivos...", "Criando ZIP");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(ZIP_FILE))) {
            for (String file : files) {
                zipOut.putNextEntry(new ZipEntry(new File(file).getName()));
                Files.copy(Paths.get(file), zipOut);
            }
        } finally {
            closeProgressMessage("Criando ZIP");
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