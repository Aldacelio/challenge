package com.br.intuitivecare.webscraping.services;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.junit.AfterClass;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import org.junit.BeforeClass;
import org.junit.Test;

import com.br.intuitivecare.utils.ConfigManager;
import com.br.intuitivecare.utils.ZipFiles;

public class DownloadFilesTest {
    
    private static DownloadFiles downloader;
    private static ZipFiles zipper;
    private static String TEST_DIR = "test-downloads/";
    private static String zipFile;
    private static List<String> downloadedFiles;

    @BeforeClass
    public static void setUpClass() throws IOException {
        DownloadFiles.setTestMode(true); 
        ConfigManager.setProperty("scraping.download.dir", TEST_DIR);
        ConfigManager.setProperty("scraping.zip.file", TEST_DIR + "anexos.zip");
        
        downloader = new DownloadFiles();
        zipper = new ZipFiles();
        zipFile = TEST_DIR + "anexos.zip";
        downloadedFiles = downloader.downloadAnexos();
    }

    @Test
    public void testDownloadAnexos() {
        assertNotNull("Lista de arquivos não deve ser nula", downloadedFiles);
        assertFalse("Lista de arquivos não deve estar vazia", downloadedFiles.isEmpty());
        
        for (String filePath : downloadedFiles) {
            File file = new File(filePath);
            assertTrue("Arquivo deve existir: " + filePath, file.exists());
            assertTrue("Arquivo deve ter tamanho maior que 0", file.length() > 0);
        }
    }

    @Test
    public void testCreateZip() throws IOException {
        zipper.createZip(downloadedFiles, zipFile);
        
        File zipFileObj = new File(zipFile);
        assertTrue("Arquivo ZIP deve existir", zipFileObj.exists());
        assertTrue("Arquivo ZIP deve ter tamanho maior que 0", zipFileObj.length() > 0);
    }

    @Test
    public void testDownloadDirCreation() {
        File dir = new File(TEST_DIR);
        assertTrue("Diretório de downloads deve existir", dir.exists());
        assertTrue("Path deve ser um diretório", dir.isDirectory());
    }

    @AfterClass
    public static void tearDownClass() {
        File dir = new File(TEST_DIR);
        if (dir.exists()) {
            File[] files = dir.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
            dir.delete();
        }
    }
}
