package com.br.intuitivecare.webscraping.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.br.intuitivecare.utils.Dialog;
import com.br.intuitivecare.utils.ProgressDialog;

public class DownloadFiles {

    private static final String URL_FILES = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
    private static final String DOWNLOAD_DIR = "downloads/";

    public DownloadFiles() {
        downloadDirExists();
    }

    private void downloadDirExists() {
        File dir = new File(DOWNLOAD_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    public List<String> downloadAnexos() throws IOException {
        List<String> downloadedFiles = new ArrayList<>();
        Document doc = Jsoup.connect(URL_FILES).get();
        Elements pdfLinks = doc.select("a[href$=.pdf]");

        for (Element link : pdfLinks) {
            String pdfUrl = link.attr("abs:href");
            String fileName = DOWNLOAD_DIR + link.text().trim() + ".pdf";

            if (fileName.contains("Anexo I") || fileName.contains("Anexo II")) {
                File targetFile = new File(fileName);

                if (shouldDownload(targetFile)) {
                    downloadFile(pdfUrl, targetFile);
                    downloadedFiles.add(fileName);
                }
            }
        }
        return downloadedFiles;
    }

    private boolean shouldDownload(File targetFile) {
        return !targetFile.exists()
                || Dialog.confirm("O arquivo " + targetFile.getName() + " já existe. Deseja substituir?");
    }

    private void downloadFile(String pdfUrl, File targetFile) throws IOException {
        String action = targetFile.exists() ? "Substituindo: " : "Baixando: ";
        ProgressDialog.show(action + targetFile.getName(), "Progresso do Download");

        try (InputStream in = new URL(pdfUrl).openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } finally {
            ProgressDialog.close("Progresso do Download");
        }
    }
}
