package com.br.intuitivecare.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import com.br.intuitivecare.utils.ui.Dialog;
import com.br.intuitivecare.utils.ui.ProgressDialog;

public class ZipFiles {

    public void createZip(List<String> files, String ZIP_FILE) throws IOException {
        if (new File(ZIP_FILE).exists()
                && !Dialog.confirm("O arquivo ZIP já existe. Deseja substituir?")) {
            return;
        }

        ProgressDialog.show("Compactando arquivos...", "Criando ZIP");
        try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(ZIP_FILE))) {
            for (String file : files) {
                zipOut.putNextEntry(new ZipEntry(new File(file).getName()));
                Files.copy(Paths.get(file), zipOut);
            }
        } finally {
            ProgressDialog.close("Criando ZIP");
        }
    }

}
