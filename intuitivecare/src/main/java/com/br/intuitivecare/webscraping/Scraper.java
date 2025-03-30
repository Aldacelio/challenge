package com.br.intuitivecare.webscraping;

import java.io.IOException;
import java.util.List;

import com.br.intuitivecare.utils.ConfigManager;
import com.br.intuitivecare.utils.ZipFiles;
import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.webscraping.services.DownloadFiles;

public class Scraper {

    public static void main(String[] args) {
        try {
            DownloadFiles downloader = new DownloadFiles();
            ZipFiles zipper = new ZipFiles();

            List<String> downloadedFiles = downloader.downloadAnexos();
            zipper.createZip(downloadedFiles, ConfigManager.get("scraping.zip.file"));
            Dialog.showMessage("Processo concluído!", "Sucesso");
        } catch (IOException e) {
            Dialog.showError("Erro: " + e.getMessage());
        }
    }

}
