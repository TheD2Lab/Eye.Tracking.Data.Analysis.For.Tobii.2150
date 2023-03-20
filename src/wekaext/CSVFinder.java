package wekaext;

import java.io.*;
import java.nio.file.*;
import java.util.Scanner;
import java.util.regex.*;

public class CSVFinder {
//	This program will merge all the participants snapshots correlating to the 
//	window size must create folder where you want data stored before running this
	
//	root directory refers to the folder with all participants folders and their snapshots

	public static void main(String[] args) {
		

		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		System.out.print("Enter folder directory to copy files: ");
		String folderDirectory = in.nextLine();

		System.out.print("Enter Start #: ");
		int initialInteger = in.nextInt();
		int currentInteger = initialInteger;

		System.out.print("Enter End #: ");
		int endInteger = in.nextInt();

		File root = new File(rootDirectory);

		if (!root.exists() || !root.isDirectory()) {
			System.out.println("Root directory does not exist or is not a directory.");
			return;
		}

		File folder = new File(folderDirectory);

		if (!folder.exists() || !folder.isDirectory()) {
			System.out.println("Folder directory does not exist or is not a directory.");
			return;
		}

		while (currentInteger <= endInteger) {
			String newFolderName = folderDirectory + String.format("/%s_", folder.getName()) + currentInteger + "sec";
			File newFolder = new File(newFolderName);

			if (!newFolder.mkdir()) {
				System.out.println("Failed to create folder " + newFolderName);
				return;
			}

			Pattern pattern = Pattern.compile("p\\d+_cumulative_" + currentInteger + "_combineResults\\.csv");

			try {
				Files.walk(root.toPath()).filter(Files::isRegularFile)
						.filter(file -> file.getFileName().toString().matches(pattern.pattern())).forEach(file -> {
							try {
								Files.copy(file, new File(newFolder, file.getFileName().toString()).toPath());
							} catch (IOException e) {
								System.out.println(
										"Error copying file " + file.getFileName().toString() + ": " + e.getMessage());
							}
						});
			} catch (IOException e) {
				System.out.println("Error walking root directory: " + e.getMessage());
			}

			currentInteger += initialInteger;
		}
	}
}
