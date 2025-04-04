package com.br.intuitivecare.pdfdataprocessing.services;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class CsvWriter {

    public void writeToCsv(List<String[]> data, String csvPath) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Paths.get(csvPath), StandardCharsets.UTF_8)) {
            for (String[] row : data) {
                writer.write(String.join(";", row) + "\n");
            }
        }
    }
}
