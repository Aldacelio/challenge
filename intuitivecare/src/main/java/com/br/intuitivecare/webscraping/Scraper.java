package com.br.intuitivecare.webscraping;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

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
            downloadAnexos();
            System.out.println("Processo concluído!");
        } catch (IOException e) {
            System.err.println("Erro: " + e.getMessage());
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
                System.out.println("Baixando: " + fileName);
                try (InputStream in = new URL(pdfUrl).openStream()) {
                    Files.copy(in, Paths.get(fileName));
                    downloadedFiles.add(fileName);
                }
            }
        }
        return downloadedFiles;
    }

}
