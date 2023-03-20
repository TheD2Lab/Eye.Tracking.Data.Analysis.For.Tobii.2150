package wekaext;

import java.io.*;
import java.util.*;

import weka.core.Instances;
import weka.core.converters.CSVSaver;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Normalize;
import weka.core.converters.CSVLoader;

public class NormalizeCSVData {
// I will come back later to this - peter

	public static void main(String[] args) throws Exception {
		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		processCSVFilesInDirectory(rootDirectory);

	}

	private static void processCSVFilesInDirectory(String directoryPath) throws Exception {
		// set directory path
		File directory = new File(directoryPath);

		// filter CSV files
		File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

		// iterate over CSV files
		for (File csvFile : csvFiles) {
			// call convertCVSToARFF or convertCVSToARFFNominal for each file
			normalizeData(csvFile);

		}
	}

	private static void normalizeData(File csvFile) throws Exception {
		// set file path
		File filePath = csvFile;

		CSVLoader loader = new CSVLoader();
		loader.setSource(filePath);
		Instances data = loader.getDataSet();

		// normalize
		final Normalize normalizeFilter = new Normalize();
		normalizeFilter.setInputFormat(data);
		data = Filter.useFilter(data, normalizeFilter);

		CSVSaver saver = new CSVSaver();
		saver.setInstances(data);
		saver.setFile(filePath);
		saver.writeBatch();
	}
}
