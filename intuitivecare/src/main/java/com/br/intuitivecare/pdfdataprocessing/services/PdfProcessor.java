package com.br.intuitivecare.pdfdataprocessing.services;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.apache.pdfbox.pdmodel.PDDocument;

import technology.tabula.ObjectExtractor;
import technology.tabula.Page;
import technology.tabula.PageIterator;
import technology.tabula.RectangularTextContainer;
import technology.tabula.Table;
import technology.tabula.extractors.SpreadsheetExtractionAlgorithm;

public class PdfProcessor {

    public List<String[]> extractTableData(String pdfPath) throws IOException {
        List<String[]> tableData = new ArrayList<>();
        
        PDDocument document = PDDocument.load(new File(pdfPath));
        try (ObjectExtractor oe = new ObjectExtractor(document)) {
            PageIterator pages = oe.extract();
            
            while (pages.hasNext()) {
                Page page = pages.next();
                SpreadsheetExtractionAlgorithm sea = new SpreadsheetExtractionAlgorithm();
                List<Table> tables = sea.extract(page);
                
                for (Table table : tables) {
                    for (List<RectangularTextContainer> row : table.getRows()) {
                        String[] rowData = new String[row.size()];
                        for (int i = 0; i < row.size(); i++) {
                            rowData[i] = row.get(i).getText();
                        }
                        tableData.add(rowData);
                    }
                }
            }
        } finally {
            document.close();
        }
        return tableData;
    }
}