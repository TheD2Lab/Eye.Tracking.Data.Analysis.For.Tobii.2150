package wekaext;

import java.io.*;
import java.nio.file.Files;
import java.util.*;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

public class CSVFilterData {
	
	private int DATA_LENGTH;
	
	private int MEDIAN;

	public CSVFilterData() {}

	public void filterCSVData(String fileLocation, ArrayList<String> filter, String featureNameForMedianSplit) throws Exception {
		// set file path
		File filePath = new File(fileLocation);

		// create reader
		FileReader reader = new FileReader(fileLocation);

		// create csv reader
		CSVReader csvReader = new CSVReader(reader);

		String[] nextRecord = csvReader.readNext();
		
		// set headers without filtered data
		DATA_LENGTH = nextRecord.length - filter.size() + 1;

		String[] header = new String[DATA_LENGTH];
		
		// maps feature to original column number
		LinkedHashMap<String, Integer> columnFeature = new LinkedHashMap<String, Integer>();

		int j = 0;

		for (int i = 0; i < nextRecord.length; i++) {
			if (!filter.contains(nextRecord[i].toLowerCase())) {
				header[j] = nextRecord[i];
				
				// maps feature to original column number
				columnFeature.put(nextRecord[i].toLowerCase(), i);
				j++;
			}
		}
		
		// makes new column for binary success
		header[j] = "Binary_Task_Success";
		
		
		// calculates median from column and returns median
		MEDIAN = getMedianFromCSV(filePath.getAbsolutePath(), columnFeature.get(featureNameForMedianSplit));
		
		
		File file = new File(String.format("%s/filtered_data-%s.csv", filePath.getParent(), filePath.getName()));
		try {
			// create FileWriter object with file as parameter
			FileWriter outputfile = new FileWriter(file);

			// create CSVWriter object filewriter object as parameter
			CSVWriter writer = new CSVWriter(outputfile);

			// adding header to csv

			writer.writeNext(header);

			// add data to csv

			while ((nextRecord = csvReader.readNext()) != null) {
				String[] data = new String[DATA_LENGTH];
				int i = 0;
				for (String k : columnFeature.keySet()) {
					if (k.equalsIgnoreCase(featureNameForMedianSplit)) {
						if (Integer.parseInt(nextRecord[columnFeature.get(k)]) >= MEDIAN) {
							data[j] = "1";
						} else {
							data[j] = "0";
						}

					}

					data[i] = nextRecord[columnFeature.get(k)];
					i++;
				}

				writer.writeNext(data);

			}

			// closing writer connection
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private static int getMedianFromCSV(String fileLocation, int columnID) throws Exception {

		// create csv reader
		CSVReader csvReader = new CSVReader(new FileReader(fileLocation));

		String[] row;

		ArrayList<Integer> taskScores = new ArrayList<>();

		while ((row = csvReader.readNext()) != null) {
			try {
				taskScores.add(Integer.parseInt(row[columnID]));

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		Collections.sort(taskScores);

		if (taskScores.size() % 2 == 0) {
			return (int) (taskScores.get(taskScores.size() / 2) + taskScores.get(taskScores.size() / 2) - 1) / 2;
		}

		return (int) (taskScores.get(taskScores.size() / 2));

	}

}
