package wekaext;

import java.io.*;
import java.util.*;
import java.nio.file.*;
import java.nio.charset.*;

public class CSVProcessor {
//	This program will add the Xplane Score to all the participants snapshots 
//	using a CSV FIle that maps the participants ID and Score

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		System.out.print("Enter CSV file: ");
		String csvFilePath = in.nextLine();
		
		System.out.print("Enter root directory: ");
		String rootDirPath = in.nextLine();

		// Read csv file
		List<String> csvLines = null;
		try {
			csvLines = Files.readAllLines(Paths.get(csvFilePath));
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Extract participant id and Xplane Score
		String[] headers = csvLines.get(0).split(",");
		int idColIndex = -1;
		int scoreColIndex = -1;
		for (int i = 0; i < headers.length; i++) {
			if (headers[i].equals("participant id")) {
				idColIndex = i;
			} else if (headers[i].equals("Xplane Score")) {
				scoreColIndex = i;
			}
		}
		if (idColIndex == -1 || scoreColIndex == -1) {
			System.err.println("participant id or Xplane Score column not found");
			System.exit(1);
		}
		Map<String, String> idScoreMap = new HashMap<>();
		for (int i = 1; i < csvLines.size(); i++) {
			String[] cols = csvLines.get(i).split(",");
			String id = cols[idColIndex];
			String score = cols[scoreColIndex];
			idScoreMap.put(id, score);
		}

		// Append Xplane Score to csv files
		try (DirectoryStream<Path> rootDirStream = Files.newDirectoryStream(Paths.get(rootDirPath))) {
			for (Path subDirPath : rootDirStream) {
				String subDirName = subDirPath.getFileName().toString();
				if (subDirName.matches("p\\d+")) {
					String id = subDirName.substring(1);
					String score = idScoreMap.get(id);
					if (score != null) {
						try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(subDirPath,
								"*combineResults*.csv")) {
							for (Path filePath : dirStream) {
								List<String> fileLines = Files.readAllLines(filePath, StandardCharsets.UTF_8);
								for (int j = 0; j < fileLines.size(); j++) {
									String[] fileCols = fileLines.get(j).split(",");
									if (j == 0) {
										fileCols = Arrays.copyOf(fileCols, fileCols.length + 1);
										fileCols[fileCols.length - 1] = "Xplane Score";
									} else if (fileCols.length > 1) {
										fileCols = Arrays.copyOf(fileCols, fileCols.length + 1);
										fileCols[fileCols.length - 1] = score;
									}
									fileLines.set(j, String.join(",", fileCols));
								}
								Files.write(filePath, fileLines, StandardCharsets.UTF_8);
							}
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
