package com.br.intuitivecare.utils;

import java.io.File;

import com.br.intuitivecare.utils.ui.Dialog;

public class FileUtils {

    private static boolean isTestMode = false;

    private FileUtils() {
        throw new UnsupportedOperationException("Classe utilitária não pode ser instanciada");
    }

    public static boolean shouldProcess(File targetFile) {
        if (isTestMode) {
            return true;
        }
        return !targetFile.exists()
                || Dialog.confirm("O arquivo " + targetFile.getName() + " já existe. Deseja substituir?");
    }

    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
    }

    public static String extractName(String filePath) {
        File file = new File(filePath);
        String name = file.getName();
        int dotIndex = name.lastIndexOf('.');
        return (dotIndex == -1) ? name : name.substring(0, dotIndex);
    }

}