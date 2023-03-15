package wekaext;

import java.io.*;
import java.util.*;

public class MergeCSVFiles {
//	This program will merge csv files in a given, 
//	the csv files names are hardcoded but can be updated to be something else

//	Using this program after CopyCSVFiles is what I made it for

	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the directory path: ");
		String dirPath = scanner.nextLine();

		// Create a File object for the input directory
		File dir = new File(dirPath);

		// Check if the input directory exists and is a directory
		if (!dir.exists() || !dir.isDirectory()) {
			System.out.println("Invalid directory path.");
			return;
		}

		// Get all CSV files with the format of "p#_combineResults" in the input
		// directory
		File[] csvFiles = dir.listFiles(new FilenameFilter() {
			public boolean accept(File dir, String name) {
				return name.matches("^p\\d+_combineResults\\.csv$");
			}
		});

		// Sort the CSV files by the number in their file name in ascending order
		Arrays.sort(csvFiles, new Comparator<File>() {
			public int compare(File f1, File f2) {
				int num1 = extractNumber(f1.getName());
				int num2 = extractNumber(f2.getName());
				return Integer.compare(num1, num2);
			}
		});

		// Create a new CSV file for the merged data
		String mergedFileName = dir.getName() + "_merged.csv";
		File mergedFile = new File(dir, mergedFileName);

		try {
			FileWriter writer = new FileWriter(mergedFile);
			boolean isFirstFile = true; // flag to check if it's the first file

			// Write the data from each CSV file to the merged CSV file
			for (File csvFile : csvFiles) {
				BufferedReader reader = new BufferedReader(new FileReader(csvFile));
				String line = reader.readLine();

				// Skip the header row if it's not the first file
				if (!isFirstFile) {
					line = reader.readLine();
				}

				// Write the data rows to the merged CSV file
				while (line != null) {
					writer.write(line + "\n");
					line = reader.readLine();
				}

				reader.close();
				isFirstFile = false; // set the flag to false after the first file
			}

			writer.close();
			System.out.println("Merged CSV file created: " + mergedFile.getAbsolutePath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// Extract the number from the file name using regular expressions
	private static int extractNumber(String fileName) {
		String[] parts = fileName.split("_");
		String numStr = parts[0].substring(1);
		return Integer.parseInt(numStr);
	}

}
