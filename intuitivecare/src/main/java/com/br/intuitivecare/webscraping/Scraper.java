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

    public static void main(String[] args) {
        try {
            new File(DOWNLOAD_DIR).mkdirs();
            List<String> pdfFiles = downloadAnexos();
            if (!pdfFiles.isEmpty()) {
                createZip(pdfFiles);
                System.out.println("Processo concluído!");
            } else {
                System.out.println("Download cancelado pelo usuário");
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null,
                    "Erro: " + e.getMessage(),
                    "Erro",
                    JOptionPane.ERROR_MESSAGE);
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
                    int resposta = JOptionPane.showConfirmDialog(null,
                            "O arquivo " + targetFile.getName() + " já existe. Deseja substituir?",
                            "Arquivo existente",
                            JOptionPane.YES_NO_OPTION);

                    if (resposta != JOptionPane.YES_OPTION) {
                        continue;
                    }
                }

                System.out.println((targetFile.exists() ? "Substituindo: " : "Baixando: ") + fileName);
                try (InputStream in = new URL(pdfUrl).openStream()) {
                    Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    downloadedFiles.add(fileName);
                }
            }
        }
        return downloadedFiles;
    }

    private static void createZip(List<String> files) throws IOException {
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream("downloads/anexos.zip"))) {
            for (String file : files) {
                zipOut.putNextEntry(new ZipEntry(new File(file).getName()));
                Files.copy(Paths.get(file), zipOut);
            }
        }
    }

}
