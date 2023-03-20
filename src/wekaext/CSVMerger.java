package wekaext;

import java.io.*;
import java.util.*;

public class CSVMerger {
//	This program will compile all the snapshots in Expanding_Windows to 1 CSV File
//	It will do this for all subdirectories (e.g. Expanding_Windows_by_Xplane_Score_#min)
	
	public static void main(String[] args) throws IOException {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter directory: ");
		// Get the root directory path from the command line argument
		String rootDirectoryPath = in.nextLine();

		// Get all subdirectories of the root directory
		File rootDirectory = new File(rootDirectoryPath);
		File[] subDirectories = rootDirectory.listFiles(File::isDirectory);

		// Iterate through all subdirectories
		for (File subDirectory : subDirectories) {
			// Get all CSV files in the subdirectory
			File[] csvFiles = subDirectory.listFiles((dir, name) -> name.endsWith(".csv"));

			// Check if there are any CSV files in the subdirectory
			if (csvFiles.length == 0) {
				continue;
			}

			// Sort the CSV files by their initial name
			Arrays.sort(csvFiles, Comparator.comparing(CSVMerger::getCSVFileNameIndex));

			// Create the output file in the root directory
			String outputFilePath = rootDirectoryPath + File.separator + subDirectory.getName() + ".csv";
			FileWriter outputFileWriter = new FileWriter(outputFilePath);

			// Write the header line to the output file
			BufferedReader headerReader = new BufferedReader(new FileReader(csvFiles[0]));
			String headerLine = headerReader.readLine();
			outputFileWriter.write(headerLine);
			outputFileWriter.write("\n");
			headerReader.close();

			// Merge the CSV files into the output file
			for (File csvFile : csvFiles) {
				BufferedReader csvFileReader = new BufferedReader(new FileReader(csvFile));

				// Skip the header line in the CSV file
				csvFileReader.readLine();

				// Copy the contents of the CSV file to the output file
				String line;
				while ((line = csvFileReader.readLine()) != null) {
					outputFileWriter.write(line);
					outputFileWriter.write("\n");
				}

				csvFileReader.close();
			}

			outputFileWriter.close();
		}
	}

	// Helper method to extract the index from the CSV file name
	private static int getCSVFileNameIndex(File csvFile) {
		String fileName = csvFile.getName();
		int indexEnd = fileName.indexOf("_");
		return Integer.parseInt(fileName.substring(1, indexEnd));
	}
}
