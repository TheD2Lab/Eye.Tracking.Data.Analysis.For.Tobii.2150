package wekaext;

import java.io.*;
import java.util.*;

public class FilterCSVData {
//	This program will filter the processed CSV files. This should only be used after all CSV files have been processed.

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);

		System.out.print("Enter root directory: ");
		String rootDirectory = in.nextLine();

		System.out.print("Enter destination directory: ");
		String destinationDirectory = in.nextLine();

		System.out.print("Enter features (separated by commas): ");
		String featureListStr = in.nextLine();
		String[] featureList = featureListStr.toLowerCase().split(",");
		for (int i = 0; i < featureList.length; i++) {
			featureList[i] = featureList[i].trim();
		}

		File root = new File(rootDirectory);
		File[] files = root.listFiles((dir, name) -> name.endsWith(".csv"));

		List<String> unfilteredFiles = new ArrayList<>();

		for (File file : files) {
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String line;
				String[] headers = br.readLine().toLowerCase().replaceAll("\"", "").split(",");
				List<Integer> featureIndices = new ArrayList<>();
				for (String feature : featureList) {
					featureIndices.add(Arrays.asList(headers).indexOf(feature));
				}

				if (featureIndices.contains(-1)) {
					unfilteredFiles.add(file.getName());
				} else {
					FileWriter fw = new FileWriter(destinationDirectory + File.separator + file.getName());
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < featureIndices.size(); i++) {
						sb.append(headers[featureIndices.get(i)]);
						if (i != featureIndices.size() - 1) {
							sb.append(",");
						}
					}
					fw.write(sb.toString() + "\n");

					while ((line = br.readLine()) != null) {
						String[] values = line.split(",");
						sb = new StringBuilder();
						for (int i = 0; i < featureIndices.size(); i++) {
							sb.append(values[featureIndices.get(i)]);
							if (i != featureIndices.size() - 1) {
								sb.append(",");
							}
						}
						fw.write(sb.toString() + "\n");
					}
					br.close();
					fw.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (unfilteredFiles.size() > 0) {
			System.out.println(
					"The following files were not filtered because one or more features could not be found in their headers:");
			for (String filename : unfilteredFiles) {
				System.out.println(filename);
			}
		} else {
			System.out.println("All files were filtered successfully.");
		}

		in.close();
	}
}
