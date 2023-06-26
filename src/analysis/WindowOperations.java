package analysis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

/*
 * Output files based on the gaze calculations that are done
 */
public class WindowOperations {

	/**
	 * Creates CSV files by dividing the data into nonoverlapping, fixed-size windows
	 *  
	 * @param inputFile		path of the input file that contains the data 
	 * @param outputFolder 	path of the output folder where the windowed data will be stored
	 * @param windowSize 	value that specifies the size of the window to be used in the operation
	 * @throws CsvValidationException
	 */
	public static void continuousWindow(String inputFile, String outputFolder, int windowSize) throws CsvValidationException
	{
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = "/continuous_" + endTime + ".csv";
		try {
			while(snapshot(inputFile,outputFile, outputFolder, initialTime,endTime))
			{
				initialTime += windowSize;
				endTime += windowSize;
				outputFile = "/continuous_" + endTime + ".csv";
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error with continuous window output " + outputFile + "\n" + e.toString());
		}

	}
	
	/**
	 * Creates CSV files by utilizing a windowing method that grows until it covers the full extent of the gaze data, the user can analyze visual attention patterns. 
	 * The degree of window growth can be adjusted to the user's requirement. 
	 * 
	 * @param inputFile		path of the input file that contains the data 
	 * @param outputFolder 	path of the output folder where the windowed data will be stored
	 * @param windowSize 	value that specifies the size of the window to be used in the operation
	 * @throws CsvValidationException
	 */
	public static void cumulativeWindow(String inputFile, String outputFolder, int windowSize) throws CsvValidationException
	{
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = "/cumulative_" + endTime + ".csv";
		try 
		{
			while(snapshot(inputFile,outputFile,outputFolder,initialTime,endTime))
			{
				endTime += windowSize;
				outputFile = "/cumulative_" + endTime + ".csv";
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error with cumulative window output " + outputFile + "\n" + e.toString());
		}
	}
	
	/**
	 * Creates CSV files by dividing the data into overlapping, fixed-size windows. 
	 * The user has the ability to customize the degree of overlap and the size of the windows according to their preferences. 
	 * 
	 * @param inputFile		path of the input file that contains the data 
	 * @param outputFolder 	path of the output folder where the windowed data will be stored
	 * @param windowSize 	value that specifies the size of the window to be used in the operation
	 * @param overlap		value the specifies the size of the overlap 
	 * @throws CsvValidationException
	 */
	public static void overlappingWindow(String inputFile, String outputFolder, int windowSize, int overlap) throws CsvValidationException
	{
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = outputFolder + "/overlap_" + endTime + ".csv";
		try {
			while(snapshot(inputFile,outputFile,outputFolder,initialTime,endTime))
			{
				initialTime = endTime - overlap;
				endTime += windowSize;
				outputFile = "/overlap_" + endTime + ".csv";
				
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error with overlapping window output " + outputFile + "\n" + e.toString());
		}
	}
	
	/**
	 * Creates CSV files Analyzes the gaze data by dividing the data based on the events selected. 
	 * Currently, the events are defined by comparing values in the selected file with the values in the baseline files.
	 * 
	 * @param inputFilePath			path of the input file that contains the data
	 * @param outputFolderPath		path of the output folder where the windowed data will be stored
	 * @param baselineFilePath 		path of the baseline file
	 * @param baselineHeaderIndex	the index of the selected value in the baseline file 
	 * @param inputHeaderIndex		the index of the selected value in the selected file 
	 * @param maxDur				the max duration an event can continue
	 * @throws IOException
	 */
	public static void eventWindow(String inputFilePath, String outputFolderPath, String baselineFilePath, int baselineHeaderIndex, int inputHeaderIndex, int maxDur) throws IOException
	{
		int index = 0;
		double baseline = -1;
		String outputFile = outputFolderPath + "/event_" + index + ".csv";
		String outputCalcFile = outputFolderPath + "/eventCalc_" + index + ".csv";
		String [] header;
		double startTime = 0; 
		int timeIndex = -1;
		
		
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        FileReader inputFileReader = new FileReader(inputFilePath);
        CSVReader inputCSVReader = new CSVReader(inputFileReader);
        FileReader baselineFileReader = new FileReader(baselineFilePath);
        CSVReader baselineCSVReader = new CSVReader(baselineFileReader);
        //Grabs the baseline value based on the chosen header

        try
        {	

        	//headers
            String[]nextLine = baselineCSVReader.readNext();
            nextLine = baselineCSVReader.readNext();
            baseline = Double.valueOf(nextLine[baselineHeaderIndex]);
        }
        catch (Exception e)
        {
    		systemLogger.writeToSystemLog(Level.SEVERE, WindowOperations.class.getName(), "Error with event window baseline reading " + outputFile + "\n" + e.toString());
    		System.exit(0);
        }
        finally
        {
        	baselineCSVReader.close();
        }
        
        //Checks the baseline value with the current value
        try
        {
        	//header
        	String[]nextLine = inputCSVReader.readNext();
        	header = nextLine;
            for(int i = 0; i < header.length; i++)
            {
            	if(header[i].contains("TIME("))
            	{
            		timeIndex = i;
            	}
            }
        	
        	outputCSVWriter.writeNext(header);
        	
        	boolean eventStart = false;
        	while((nextLine = inputCSVReader.readNext())!= null)
        	{
        		//skips the first two minutes
        		if(Double.valueOf(nextLine[timeIndex]) <= 120) 
        		{
        			continue;
        		}
        		
        		if(eventStart)
        		{
        			//checks if it is greater than the baseline or the duration is within the accepted range
	        		if(Double.valueOf(nextLine[inputHeaderIndex]) > baseline || !(Double.valueOf(nextLine[timeIndex]) - startTime > maxDur))
	        		{
	        			outputCSVWriter.writeNext(nextLine);
	        			//restarts the time every time an event is discovered
	        			if(Double.valueOf(nextLine[inputHeaderIndex]) > baseline)
	        			{
	        				startTime = Double.valueOf(nextLine[timeIndex]);
	        			}
	        		}
	        		else
	        		{
	        			outputCSVWriter.close();
	        			modifier.csvToARFF(outputFile);
	        			gaze.processGaze(outputFile, outputCalcFile);
	        			modifier.csvToARFF(outputCalcFile);
	        			eventStart = false;
	        			index++;
	        			outputFile = outputFolderPath + "/event_" + index + ".csv";
	        			outputCalcFile = outputFolderPath + "/eventCalc_" + index + ".csv";
	        			outputFileWriter = new FileWriter(new File (outputFile));
	        	        outputCSVWriter = new CSVWriter(outputFileWriter);
	        	        outputCSVWriter.writeNext(header);
	        		}
        		}
        		else
        		{
        			if(Double.valueOf(nextLine[inputHeaderIndex]) > baseline)
	        		{
	        			outputCSVWriter.writeNext(nextLine);
	        			eventStart = true;
	        			startTime = Double.valueOf(nextLine[timeIndex]);
	        		}
        		}
        	}
        }
        catch(Exception e)
        {
        	System.out.println("Error with event window  " + outputFile + "\n" + e.toString());
    		systemLogger.writeToSystemLog(Level.SEVERE, WindowOperations.class.getName(), "Error with event window  " + outputFile + "\n" + e.toString());
    		System.exit(0);
        }
        finally
        {
        	outputCSVWriter.close();
        	inputCSVReader.close();
        }
        
        

	}
	
	/**
	 * creates a CSV file containing all of the data points within the given start and end times based on the input file
	 * 
	 * @param inputFile		the CSV file containing the data points that will be copied
	 * @param fileName		the name of the input CSV file
	 * @param outputFolder	the path of the output location
	 * @param start			the start time
	 * @param end			the end time
	 * @return	boolean		true if the program was able to successfully execute it, false otherwise
	 */
	private static boolean snapshot(String inputFile, String fileName, String outputFolder, int start, int end) throws IOException, CsvValidationException
	{
		String outputFile = outputFolder + fileName;
		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        FileReader fileReader = new FileReader(inputFile);
        CSVReader csvReader = new CSVReader(fileReader);

        try 
        {
        	//header
             String[]nextLine = csvReader.readNext();
             outputCSVWriter.writeNext(nextLine);
             
             int timestampIndex = -1;
             for(int i = 0; i < nextLine.length; i++)
             {
            	 if(nextLine[i].contains("TIME("))
            	 {
            		 timestampIndex = i;
            		 break;
            	 }
             }
             
             while((nextLine = csvReader.readNext()) != null) 
             {
            	 if(Double.valueOf(nextLine[timestampIndex]) < start)
            	 {
            		 continue;
            	 }
            	 else if(Double.valueOf(nextLine[timestampIndex]) > end)
            	 {
            		 break;
            	 }
            	 else
            	 {
            		 outputCSVWriter.writeNext(nextLine);
            	 }
             }

             if((nextLine = csvReader.readNext()).equals(null))
             {
            	 return false;
             }
             
     		systemLogger.writeToSystemLog(Level.INFO, WindowOperations.class.getName(), "Successfully created file " + outputFile );
        }
        catch(NullPointerException ne)
        {
        	System.out.println("done writing file: " + outputFile);
        	outputCSVWriter.close();
        	return false;
        }
        catch(Exception e)
        {
    		systemLogger.writeToSystemLog(Level.SEVERE, WindowOperations.class.getName(), "Error with window method  " + outputFile + "\n" + e.toString());
    		System.exit(0);

        }
        finally
        {
            outputCSVWriter.close();
            csvReader.close();
        }
        return true;
	}
	
	
	
}
