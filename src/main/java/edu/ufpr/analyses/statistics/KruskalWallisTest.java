/*
 * Copyright 2015, Charter Communications, All rights reserved.
 */
package edu.ufpr.analyses.statistics;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/**
 *
 *
 * @author Vidal
 */
public class KruskalWallisTest {

	public static HashMap<String, HashMap<String, Boolean>> test(HashMap<String, double[]> values)
			throws IOException, InterruptedException {

		String script = "require(pgirmess)\n";
		script += "ARRAY <- c(";
		int size = 0;
		for (Map.Entry<String, double[]> entrySet : values.entrySet()) {
			double[] keyValues = entrySet.getValue();
			size = keyValues.length;

			for (Double value : keyValues) {
				script += value + ",";
			}
		}
		script = script.substring(0, script.lastIndexOf(",")) + ")";
		script += "\n";

		script += "categs<-as.factor(rep(c(";
		for (Map.Entry<String, double[]> entrySet : values.entrySet()) {
			String key = entrySet.getKey();
			script += "\"" + key + "\",";
		}
		script = script.substring(0, script.lastIndexOf(","));
		script += "),each=" + size + "));";
		script += "\n";
		script += "result <- kruskal.test(ARRAY,categs)\n";
		script += "m <- data.frame(result$statistic,result$p.value)\n";
		script += "pos_teste <- kruskalmc(ARRAY,categs)\n";
		script += "print(pos_teste)";

		File scriptFile = File.createTempFile("script", ".R");
		File outputFile = File.createTempFile("output", ".R");
		scriptFile.deleteOnExit();
		outputFile.deleteOnExit();

		try (FileWriter scriptWriter = new FileWriter(scriptFile)) {
			scriptWriter.append(script);
		}

		ProcessBuilder processBuilder = new ProcessBuilder("R", "--slave", "-f", scriptFile.getAbsolutePath());
		processBuilder.redirectOutput(outputFile);

		Process process = processBuilder.start();
		process.waitFor();

		ArrayList<Map.Entry<String, double[]>> entrySets = new ArrayList<>(values.entrySet());

		HashMap<String, HashMap<String, Boolean>> result = new HashMap<>();

		for (int i = 0; i < entrySets.size() - 1; i++) {
			String entry1 = entrySets.get(i).getKey();
			for (int j = i + 1; j < entrySets.size(); j++) {
				String entry2 = entrySets.get(j).getKey();

				try (Scanner scanner = new Scanner(outputFile)) {
					while (scanner.hasNextLine()) {
						String line = scanner.nextLine();
						if (line.contains(entry1 + "-" + entry2) || line.contains(entry2 + "-" + entry1)) {
							HashMap<String, Boolean> entry1Map = result.get(entry1);
							if (entry1Map == null) {
								entry1Map = new HashMap<>();
								result.put(entry1, entry1Map);
							}
							HashMap<String, Boolean> entry2Map = result.get(entry2);
							if (entry2Map == null) {
								entry2Map = new HashMap<>();
								result.put(entry2, entry2Map);
							}
							if (line.contains("TRUE")) {
								entry1Map.put(entry2, true);
								entry2Map.put(entry1, true);
								break;
							} else if (line.contains("FALSE")) {
								entry1Map.put(entry2, false);
								entry2Map.put(entry1, false);
								break;
							}
						}
					}
				}
			}
		}

		scriptFile.delete();
		outputFile.delete();

		return result;
	}

	public static void main(String[] args) throws IOException, InterruptedException {

		HashMap<String, double[]> values = new HashMap<>();

		// IRIS
		// double[] kmeansFitnessValues = new double[] { 0.667678190763838,
		// 0.667678190763838, 0.667678190763838,
		// 0.667678190763838, 0.667678190763838, 0.667678190763838,
		// 0.667678190763838, 0.667678190763838,
		// 0.667678190763838, 0.667678190763838 };
		//
		//
		// double[] geFitnessValues = new double[] { 0.640564307750583,
		// 0.667904585747882, 0.656110067902717,
		// 0.624482641105086, 0.624482641105086, 0.644584613179508,
		// 0.649367208316958, 0.6493672, 0.644584613179508,
		// 0.637376972427625 };
		// values.put("GE", geFitnessValues);

		// Heart
		// double[] geFitnessValues = new double[] { 0.44972619, 0.42558225,
		// 0.40290987, 0.40833558, 0.42706622,
		// 0.39126282, 0.41480231, 0.41349616, 0.39427040, 0.38179748 };
		// values.put("GE", geFitnessValues);
		//
		// double[] kmeansFitnessValues = new double[] { 0.382144788,
		// 0.382144788, 0.382144788, 0.385095787, 0.385095787,
		// 0.385095787, 0.382144788, 0.385095787, 0.382144788, 0.382144788 };

		// Glass
		double[] geFitnessValues = new double[] { -27, -32, -32, -28, -33, -31, -33, -30, -30, -26, -30, -29, -29, -28,
				-31, -31, -31, -29, -32, -27, -28, -31, -30, -32, -31, -33, -32, -30, -27, -30

		};
		values.put("v1", geFitnessValues);

		double[] kmeansFitnessValues = new double[] { -29, -32, -32, -30, -33, -31, -31, -31, -32, -31, -32, -31, -30,
				-32, -31, -31, -34, -31, -34, -32, -30, -30, -33, -31, -33, -31, -31, -32, -31, -30 };

		values.put("v2", kmeansFitnessValues);

		HashMap<String, HashMap<String, Boolean>> kruskalWallisTest = KruskalWallisTest.test(values);

		Boolean boolean0 = kruskalWallisTest.get("v1").get("v2");
		System.out.println(boolean0);

	}
}
