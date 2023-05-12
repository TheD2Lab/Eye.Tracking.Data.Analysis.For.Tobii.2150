package wekaext;

import java.io.*;
import java.util.*;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class ConvertToBinarySuccess {
//	This program will make a new CSV file excluding the 
//	feature name specified and add binary success instead
	
//	This program requires a CSV file with participant id and feature name (e.g. "Xplane Score")

	public static void main(String[] args) throws Exception {

		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		System.out.print("Enter feature name: ");
		String featureName = in.nextLine();

		System.out.print("Enter CSV file to compute median: ");
		String csvFile = in.nextLine(); // change this to your CSV file's name

		ArrayList<Integer> columnValues = getColumnValues(csvFile, featureName);
		int median = computeMedian(columnValues);
		System.out.println("The median for column " + featureName + " is: " + median);

		processCSVFilesInDirectory(rootDirectory, featureName, median);

		in.close();

	}

	private static void processCSVFilesInDirectory(String directoryPath, String featureName, int median)
			throws Exception {
		// set directory path
		File directory = new File(directoryPath);

		// filter CSV files
		File[] csvFiles = directory.listFiles((dir, name) -> name.toLowerCase().endsWith(".csv"));

		// iterate over CSV files
		for (File csvFile : csvFiles) {
			// call CSVDataBinarySuccess for each file
			CSVDataBinarySuccess(csvFile.getAbsolutePath(), featureName, median);
		}
	}

	private static void CSVDataBinarySuccess(String fileLocation, String featureName, int median) throws Exception {

		// set file path
		File filePath = new File(fileLocation);

		// create reader
		FileReader reader = new FileReader(fileLocation);

		// create csv reader
		CSVReader csvReader = new CSVReader(reader);

		String[] nextRecord = csvReader.readNext();

		// set headers without filtered data
		String[] header = new String[nextRecord.length];

		// maps feature to original column number
		LinkedHashMap<String, Integer> oldColumnFeature = new LinkedHashMap<String, Integer>();

		// maps feature to new column number
		LinkedHashMap<String, Integer> columnFeature = new LinkedHashMap<String, Integer>();

		int j = 0;

		for (int i = 0; i < nextRecord.length; i++) {
			if (!nextRecord[i].equalsIgnoreCase(featureName)) {
				header[j] = nextRecord[i];
				columnFeature.put(nextRecord[i].toLowerCase(), i);
				j++;
			}
			oldColumnFeature.put(nextRecord[i].toLowerCase(), i);
		}

		// adds binary success header
		header[j] = "Binary_Success";

		// set new file name
		String fileName = filePath.getName().replaceFirst("[.][^.]+$", "");
		fileName = fileName.replace("Score_", "Binary_Success_");

		// create new file with updated name
		File file = new File(String.format("%s/%s.csv", filePath.getParent(), fileName));

		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv
			writer.writeNext(header);

			// add data to csv
			while ((nextRecord = csvReader.readNext()) != null) {
				String[] data = new String[header.length];
				int i = 0;
				for (String k : oldColumnFeature.keySet()) {
					if (k.equalsIgnoreCase(featureName)) {
						if (Integer.parseInt(nextRecord[oldColumnFeature.get(k)]) >= median) {
							data[j] = "1";
						} else {
							data[j] = "0";
						}
					} else {
						data[i] = nextRecord[columnFeature.get(k)];
						i++;
					}
				}
				writer.writeNext(data);
			}

			// closing writer connection
			writer.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static ArrayList<Integer> getColumnValues(String csvFile, String columnName) {
		ArrayList<Integer> columnValues = new ArrayList<>();
		String line = "";
		String cvsSplitBy = ",";
		int columnIndex = -1;
		try (BufferedReader br = new BufferedReader(new FileReader(csvFile))) {
			// Read the first line to find the index of the column with the given column
			// name
			if ((line = br.readLine()) != null) {
				String[] columns = line.split(cvsSplitBy);
				for (int i = 0; i < columns.length; i++) {
					if (columns[i].equals(columnName)) {
						columnIndex = i;
						break;
					}
				}
			}
			// Read the remaining lines and extract the values for the given column
			while ((line = br.readLine()) != null) {
				String[] values = line.split(cvsSplitBy);
				if (columnIndex >= 0 && columnIndex < values.length) {
					columnValues.add(Integer.parseInt(values[columnIndex]));
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return columnValues;
	}

	private static int computeMedian(ArrayList<Integer> values) {
		Collections.sort(values);
		int size = values.size();
		if (size % 2 == 0) {
			int sum = values.get(size / 2) + values.get((size / 2) - 1);
			return sum / 2;
		} else {
			return values.get(size / 2);
		}
	}

}
