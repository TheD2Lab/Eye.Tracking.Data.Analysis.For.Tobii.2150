package wekaext;
import java.io.File;
import java.util.Scanner;

public class CSVFileRenamer {
//	This program will rename files to the participant id for faster processing

    public static void main(String[] args) {
    	Scanner in = new Scanner(System.in);
    	System.out.print("Enter directory: " );
        String rootDirectory = in.nextLine();
        renameCSVFiles(rootDirectory);
    }

    public static void renameCSVFiles(String directoryPath) {
        File directory = new File(directoryPath);
        File[] files = directory.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                renameCSVFiles(file.getAbsolutePath());
            } else if (file.isFile() && file.getName().endsWith(".csv") && file.getName().contains("combineResults")) {
                String directoryName = file.getParentFile().getName();
                String newFileName = directoryName + "_" + file.getName();
                File newFile = new File(file.getParent(), newFileName);
                if (file.renameTo(newFile)) {
                    System.out.println("Renamed file " + file.getName() + " to " + newFileName);
                } else {
                    System.out.println("Failed to rename file " + file.getName());
                }
            }
        }
    }

}
