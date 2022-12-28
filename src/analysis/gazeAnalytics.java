package analysis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

/*
 * Output files based on the gaze calculations that are done
 */
public class gazeAnalytics {

	public static void continuousWindow(String inputFile, String outputFolder, int windowSize)
	{
		
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = outputFolder + "/continuous_" + endTime + ".csv";
		try {
			while(snapshot(inputFile,outputFile,initialTime,endTime))
			{
				initialTime += windowSize;
				endTime += windowSize;
				outputFile = outputFolder + "/continuous_" + endTime + ".csv";
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error with continuous window output " + outputFile + "\n" + e.toString());
		}

	}
	
	public static void cumulativeWindow(String inputFile, String outputFolder, int windowSize)
	{
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = outputFolder + "/cumulative_" + endTime + ".csv";
		try 
		{
			while(snapshot(inputFile,outputFile,initialTime,endTime))
			{
				endTime += windowSize;
				outputFile = outputFolder + "/cumulative_" + endTime + ".csv";
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error with cumulative window output " + outputFile + "\n" + e.toString());
		}
	}
	
	public static void overlappingWindow(String inputFile, String outputFolder, int windowSize, int overlap)
	{
		int endTime = windowSize;
		int initialTime = 0;
		String outputFile = outputFolder + "/overlap_" + endTime + ".csv";
		try {
			while(snapshot(inputFile,outputFile,initialTime,endTime))
			{
				initialTime = endTime - overlap;
				endTime += windowSize;
				outputFile = outputFolder + "/overlap_" + endTime + ".csv";
				
			}
		} 
		catch (IOException e) 
		{
			 systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error with overlapping window output " + outputFile + "\n" + e.toString());
		}
	}
	
	public static void eventWindow(String inputFilePath, String outputFolderPath, String baselineFilePath, int baselineHeaderIndex, int inputHeaderIndex, int maxDur) throws IOException
	{
//		baselineFilePath = "C:\\Users\\kayla\\Desktop\\School\\Direct Studies\\graphGZDResults.csv";
//		outputFolderPath = "C:\\Users\\kayla\\Desktop\\School\\Direct Studies";
//		inputFilePath = "C:\\Users\\kayla\\Desktop\\Eye.Tracking.Data.Analysis.For.Tobii.2150\\data\\User 1_all_gaze.csv";
//		baselineHeaderIndex = 1;
//		inputHeaderIndex = 31;
		//Seconds
//		maxDur = 3;
		int index = 0;
		double baseline = -1;
		String outputFile = outputFolderPath + "/event_" + index + ".csv";
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
    		systemLogger.writeToSystemLog(Level.SEVERE, gazeAnalytics.class.getName(), "Error with event window baseline reading " + outputFile + "\n" + e.toString());
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
        		if(eventStart)
        		{
        			//checks if it is greater than the baseline and the duration is within the accepted range
	        		if(Double.valueOf(nextLine[inputHeaderIndex]) > baseline && !(Double.valueOf(nextLine[timeIndex]) - startTime > maxDur))
	        		{
	        			outputCSVWriter.writeNext(nextLine);
	        		}
	        		else
	        		{
	        			eventStart = false;
	        			index++;
	        			outputFile = outputFolderPath + "\\event_" + index + ".csv";
	        			outputCSVWriter.close();
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
        	System.out.println(e + "H");
    		systemLogger.writeToSystemLog(Level.SEVERE, gazeAnalytics.class.getName(), "Error with event window  " + outputFile + "\n" + e.toString());
    		System.exit(0);
        }
        finally
        {
        	outputCSVWriter.close();
        	inputCSVReader.close();
        }
        
        

	}
	
	private static boolean snapshot(String inputFile, String outputFile, int start, int end) throws IOException
	{
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
             
     		systemLogger.writeToSystemLog(Level.INFO, gazeAnalytics.class.getName(), "Successfully created file " + outputFile );
        }
        catch(NullPointerException ne)
        {
        	System.out.println("done writing file: " + outputFile);
        	outputCSVWriter.close();
        	csvToARFF(outputFile);
        	return false;
        }
        catch(Exception e)
        {
    		systemLogger.writeToSystemLog(Level.SEVERE, gazeAnalytics.class.getName(), "Error with window method  " + outputFile + "\n" + e.toString());
    		System.exit(0);

        }
        finally
        {
            outputCSVWriter.close();
            csvReader.close();
        }
        
        csvToARFF(outputFile);
        return true;
	}
	
	
	public static void csvToARFF(String outputCSVPath)
	{
		String outputARFFPath = outputCSVPath.replace(".csv", ".arff");

		try 
		{

			CSVLoader loader = new CSVLoader();
			loader.setSource(new File(outputCSVPath));
			Instances data = loader.getDataSet();

			File arffFile = new File(outputARFFPath);
			if(!arffFile.exists())
			{
				ArffSaver saver = new ArffSaver();
				saver.setInstances(data);
				saver.setFile(arffFile);
				saver.writeBatch();
				systemLogger.writeToSystemLog(Level.INFO, gazeAnalytics.class.getName(), "Successfully converted CSV to ARFF " + outputARFFPath);
			}
			else
			{
				System.out.println("File Exists");
			}
		}
		catch(IOException e)
		{
			systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + e.toString());

		}
		catch(IllegalArgumentException ia)
		{
			systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + ia.toString());

		}
	}
	
}
