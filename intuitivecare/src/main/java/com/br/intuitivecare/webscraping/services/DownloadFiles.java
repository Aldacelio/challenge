package com.br.intuitivecare.webscraping.services;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.br.intuitivecare.utils.ConfigManager;
import com.br.intuitivecare.utils.FileUtils;
import com.br.intuitivecare.utils.ui.ProgressDialog;

public class DownloadFiles {

    private final String URL_FILES;
    private final String DOWNLOAD_DIR;

    public DownloadFiles() {
        this.URL_FILES = ConfigManager.get("scraping.source.url");
        this.DOWNLOAD_DIR = ConfigManager.get("scraping.download.dir");
        this.downloadDirExists();
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
            String fileName = DOWNLOAD_DIR + link.text().trim() + "pdf";

            if (fileName.contains("Anexo I") || fileName.contains("Anexo II")) {
                File targetFile = new File(fileName);

                if (FileUtils.shouldProcess(targetFile)) {
                    downloadFile(pdfUrl, targetFile);
                    downloadedFiles.add(fileName);
                }
            }
        }
        return downloadedFiles;
    }

    private void downloadFile(String pdfUrl, File targetFile) throws IOException {
        String action = targetFile.exists() ? "Substituindo: " : "Baixando: ";
        ProgressDialog.show(action + targetFile.getName(), "Progresso do Download");

        try (InputStream in = new URI(pdfUrl).toURL().openStream()) {
            Files.copy(in, targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (URISyntaxException e) {
            throw new IOException("URL inválida: " + pdfUrl, e);
        } finally {
            ProgressDialog.close("Progresso do Download");
        }
    }
}
