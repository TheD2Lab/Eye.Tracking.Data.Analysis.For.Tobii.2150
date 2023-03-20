package wekaext;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class CopyCSVFiles {
//	This program will copy all participants overall gaze analytics into another folder
//	This is before the MergeCSVFiles class

	public static void main(String[] args) throws IOException {

		Scanner scanner = new Scanner(System.in);
		System.out.print("Enter the root directory: ");
		String rootDir = scanner.nextLine();

		System.out.print("Enter the destination directory: ");
		String destDir = scanner.nextLine();

		copyCSVFiles(rootDir, destDir);

		System.out.println("CSV files copied successfully!");
	}

	public static void copyCSVFiles(String rootDir, String destDir) throws IOException {

		Path rootPath = Paths.get(rootDir);
		Path destPath = Paths.get(destDir);

		Files.walk(rootPath).filter(path -> Files.isDirectory(path)).forEach(dirPath -> {
			try {
				Files.walk(dirPath).filter(
						path -> Files.isRegularFile(path) && path.getFileName().toString().equals("combineResults.csv"))
						.forEach(csvPath -> {
							String newFileName = dirPath.getFileName().toString() + "_"
									+ csvPath.getFileName().toString();
							Path newFilePath = destPath.resolve(newFileName);
							try {
								Files.copy(csvPath, newFilePath);
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						});
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}
}
