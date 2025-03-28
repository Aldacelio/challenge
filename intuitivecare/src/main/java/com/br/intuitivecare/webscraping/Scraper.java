package com.br.intuitivecare.webscraping;

import java.io.IOException;
import java.util.List;

import com.br.intuitivecare.utils.Dialog;
import com.br.intuitivecare.webscraping.services.DownloadFiles;
import com.br.intuitivecare.webscraping.services.ZipFiles;

public class Scraper {

    public static void main(String[] args) {
        try {
            DownloadFiles downloader = new DownloadFiles();
            ZipFiles zipper = new ZipFiles();

            List<String> downloadedFiles = downloader.downloadAnexos();
            zipper.createZip(downloadedFiles);
            Dialog.showMessage("Processo concluído!", "Sucesso");
        } catch (IOException e) {
            Dialog.showError("Erro: " + e.getMessage());
        }
    }

}
