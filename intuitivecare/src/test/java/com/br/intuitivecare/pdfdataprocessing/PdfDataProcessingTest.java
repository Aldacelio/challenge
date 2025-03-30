package com.br.intuitivecare.pdfdataprocessing;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.AfterClass;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.BeforeClass;
import org.junit.Test;

import com.br.intuitivecare.pdfdataprocessing.services.CsvWriter;
import com.br.intuitivecare.pdfdataprocessing.services.PdfProcessor;
import com.br.intuitivecare.utils.ConfigManager;
import com.br.intuitivecare.utils.FileUtils;

public class PdfDataProcessingTest {

    private static PdfProcessor pdfProcessor;
    private static CsvWriter csvWriter;
    private static final String TEST_DIR = "test-pdf-processing/";
    private static final String TEST_PDF = "downloads/Anexo I.pdf";
    private static final String TEST_CSV = TEST_DIR + "test.csv";
    
    @BeforeClass
    public static void setUpClass() throws IOException {
        FileUtils.setTestMode(true);
        ConfigManager.setProperty("pdfprocessing.csv.dir", TEST_DIR);
        
        File dir = new File(TEST_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        
        pdfProcessor = new PdfProcessor();
        csvWriter = new CsvWriter();
    }

    @Test
    public void testPdfDataExtraction() throws IOException {
        List<String[]> data = pdfProcessor.extractTableData(TEST_PDF);
        
        assertNotNull("Dados extraídos não devem ser nulos", data);
        assertFalse("Lista de dados não deve estar vazia", data.isEmpty());
        assertTrue("Deve ter mais de uma linha", data.size() > 1);
    }

    @Test
    public void testAbbreviationReplacement() {
        List<String[]> testData = Arrays.asList(
            new String[]{"OD", "AMB", "Teste"},
            new String[]{"Outro", "OD AMB", "Dados"}
        );
        
        PdfDataProcessing.replaceColumnAbbreviations(testData);
        
        assertEquals("Seg. Odontológica", testData.get(0)[0]);
        assertEquals("Seg. Ambulatorial", testData.get(0)[1]);
        assertTrue(testData.get(1)[1].contains("Seg. Odontológica"));
        assertTrue(testData.get(1)[1].contains("Seg. Ambulatorial"));
    }

    @Test
    public void testCsvWriting() throws IOException {
        List<String[]> testData = Arrays.asList(
            new String[]{"Coluna1", "Coluna2"},
            new String[]{"Valor1", "Valor2"}
        );
        
        try {
            csvWriter.writeToCsv(testData, TEST_CSV);
            
            File csvFile = new File(TEST_CSV);
            assertTrue("Arquivo CSV deve existir", csvFile.exists());
            assertTrue("Arquivo CSV deve ter tamanho maior que 0", csvFile.length() > 0);
        } catch (IOException e) {
            fail("Erro ao escrever arquivo CSV: " + e.getMessage());
        }
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
