package wekaext;

import java.io.*;
import java.util.*;

import weka.core.Instances;

import weka.core.converters.CSVLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.NumericToNominal;
import weka.core.converters.ArffSaver;

public class ConvertCVSToArff {
//	This program will convert CSV files specified in a directory to ARFF Files

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		boolean nominal = false;
		System.out.print("Enter Y if class attribute is Nominal or N if class attribute is NOT Nominal: ");
		String isNominal = in.nextLine();

		if (isNominal.equalsIgnoreCase("Y")) {
			nominal = true;
		}

		try {
			processCSVFilesInDirectory(rootDirectory, nominal);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		in.close();

	}

	private static void processCSVFilesInDirectory(String directoryPath, boolean nominal) throws Exception {
		// set directory path
		File directory = new File(directoryPath);

		// filter CSV files
		File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

		// iterate over CSV files
		for (File csvFile : csvFiles) {
			// call convertCVSToARFF or convertCVSToARFFNominal for each file
			if (nominal) {
				convertCSVToARFFNominal(csvFile.getAbsolutePath());
			} else {
				convertCSVToARFF(csvFile.getAbsolutePath());
			}

		}
	}

	private static void convertCSVToARFF(String fileLocation) throws IOException {

		// set file path
		File filePath = new File(fileLocation);

		CSVLoader loader = new CSVLoader();
		loader.setSource(filePath);
		Instances data = loader.getDataSet();

		// save ARFF file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(data);
		saver.setFile(new File(
				String.format("%s/%s.arff", filePath.getParent(), filePath.getName().replaceFirst("[.][^.]+$", ""))));
		saver.writeBatch();

	}

	private static void convertCSVToARFFNominal(String fileLocation) throws Exception {

		// set file path
		File filePath = new File(fileLocation);

		CSVLoader loader = new CSVLoader();
		loader.setSource(filePath);
		Instances data = loader.getDataSet();

//	    This is needed to change the last attribute  by default the (class) from numeric to nominal

		NumericToNominal convert = new NumericToNominal();

		String[] options = new String[2];
		options[0] = "-R";
		options[1] = Integer.toString(data.numAttributes());

		convert.setOptions(options);
		convert.setInputFormat(data);

		Instances newData = Filter.useFilter(data, convert);

		// save ARFF file
		ArffSaver saver = new ArffSaver();
		saver.setInstances(newData);
		saver.setFile(new File(
				String.format("%s/%s.arff", filePath.getParent(), filePath.getName().replaceFirst("[.][^.]+$", ""))));
		saver.writeBatch();

	}

}
