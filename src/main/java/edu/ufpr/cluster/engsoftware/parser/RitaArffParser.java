/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.cluster.engsoftware.parser;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import au.com.bytecode.opencsv.CSVWriter;
import edu.ufpr.jmetal.problem.old.impl.DataInstanceReader;

/**
 *
 *
 * @author Vidal
 */
public class RitaArffParser {

    public static void doParser(String fileName, String outputFile) throws IOException {

        try (
            BufferedReader br =
                new BufferedReader(new InputStreamReader(DataInstanceReader.class.getResourceAsStream(fileName)));
            CSVWriter csvWriter = new CSVWriter(new FileWriter(outputFile))) {

            String line = null;
            boolean isDataLine = false;
            while ((line = br.readLine()) != null) {
                if (line.contains("@data")) {
                    isDataLine = true;
                    continue;
                }
                String[] newValues = new String[17];
                if (isDataLine) {
                    String[] values = line.split(",");
                    for (int i = 1; i < 17; i++) {
                        newValues[i - 1] = values[i];
                    }
                    newValues[16] = values[0];
                    csvWriter.writeNext(newValues);
                }
            }
        }

    }

    public static void main(String[] args) throws IOException {

        RitaArffParser.doParser("/bbsort/tabela_resultado_arrumada.arff", "tabela_resultado_arrumada_filtrada.data");
    }

}
