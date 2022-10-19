package analysis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

public class gazeAnalytics {
//String inputFile, String outputFile, int timelength
	public static void continueousWindow() throws IOException
	{
		String inputFile = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Data\\gazepoint.GZD.csv";
		String outputFolder = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Results\\Window";
		int timeLength = 2;
		int currentTime = timeLength;
		String outputFile = outputFolder + "\\continueous_" + currentTime + ".csv";
		
				
				
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);

        try {
        	 FileReader fileReader = new FileReader(inputFile);
             CSVReader csvReader = new CSVReader(fileReader);
             String[]nextLine = csvReader.readNext();
             double initialTime = 0;
    
             int timestampIndex = -1;
             for(int i = 0; i < nextLine.length; i++)
             {
            	 if(nextLine[i].contains("TIME("))
            	 {
            		 timestampIndex = i;
            	 }
             }
             	
             while((nextLine = csvReader.readNext()) != null) 
             {
            	 if(withinSelectedTime(initialTime, Double.valueOf(nextLine[timestampIndex]), timeLength))
            	 {
            		 outputCSVWriter.writeNext(nextLine);
            	 }
            	 else
            	 {
            		 currentTime += timeLength;
            		 initialTime += timeLength;
            		 outputCSVWriter = new CSVWriter(new FileWriter(new File (outputFolder + "\\continueous_" + currentTime + ".csv")));
            		 outputCSVWriter.writeNext(nextLine);
            	 }
             }

            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing event data to: " + outputFile);
        }
        catch(Exception e)
        {
        	
        }
	}
	
	public static void cumulativeWindow() throws IOException
	{
		String inputFile = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Data\\gazepoint.GZD.csv";
		String outputFolder = "C:\\Users\\kayla\\OneDrive\\Desktop\\Boeing Test Folder\\Results\\Window";
		int timeLength = 2;
		int currentTime = timeLength;
		String outputFile = outputFolder + "\\cumulative_" + currentTime + ".csv";
		
				
				
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);

        try {
        	 FileReader fileReader = new FileReader(inputFile);
             CSVReader csvReader = new CSVReader(fileReader);
             String[]nextLine = csvReader.readNext();
             double initialTime = 0;
    
             int timestampIndex = -1;
             for(int i = 0; i < nextLine.length; i++)
             {
            	 if(nextLine[i].contains("TIME("))
            	 {
            		 timestampIndex = i;
            	 }
             }
             	
             while((nextLine = csvReader.readNext()) != null) 
             {
            	 if(withinSelectedTime(initialTime, Double.valueOf(nextLine[timestampIndex]), timeLength))
            	 {
            		 outputCSVWriter.writeNext(nextLine);
            	 }
            	 else
            	 {
            		 currentTime += timeLength;
            		 initialTime += timeLength;
                     outputCSVWriter.close();
            		 outputCSVWriter = new CSVWriter(new FileWriter(new File (outputFolder + "\\cumulative_" + currentTime + ".csv")));
            		 FileReader copy= new FileReader(outputFolder + "\\cumulative_" + initialTime + ".csv");
            		 CSVReader copyReader = new CSVReader(copy);
            		 List allData = copyReader.readAll();
            		 outputCSVWriter.writeAll(allData, false);
            		 outputCSVWriter.writeNext(nextLine);
            		 
            	 }
             }

            outputCSVWriter.close();
            csvReader.close();
            System.out.println("done writing event data to: " + outputFile);
        }
        catch(Exception e)
        {
        	
        }
	}
	
	private static boolean withinSelectedTime(double timeOne, double timeTwo, int timeLength)
	{
		if(Math.abs(timeOne - timeTwo) < timeLength)
		{
			return true;
		}
		return false;
	}
}
