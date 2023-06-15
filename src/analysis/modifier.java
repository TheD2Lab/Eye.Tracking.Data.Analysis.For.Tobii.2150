package analysis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvValidationException;

import weka.core.Instances;
import weka.core.converters.ArffSaver;
import weka.core.converters.CSVLoader;

public class modifier {

	/**
	 * converts CSV file to an ARFF file
	 * 
	 * @param outputCSVPath		path of the CSV file
	 */
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
				System.out.println("Successfully converted CSV to ARFF " + outputARFFPath);
				systemLogger.writeToSystemLog(Level.INFO, WindowOperations.class.getName(), "Successfully converted CSV to ARFF " + outputARFFPath);
			}
			else
			{
				System.out.println("File Exists");
			}
		}
		catch(IOException e)
		{
			System.out.println("Error coverting CSV to ARFF " + outputARFFPath + "\n" + e.toString());
			systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + e.toString());

		}
		catch(IllegalArgumentException ia)
		{
			System.out.println("Error coverting CSV to ARFF " + outputARFFPath + "\n" + ia.toString());
			systemLogger.writeToSystemLog(Level.WARNING, WindowOperations.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + ia.toString());

		}
	}
	
	/**
	 * Merges all the result files into one CSV file
	 * 
	 * @param FXD			the path of the fixation file
	 * @param EVD			the path of the event file
	 * @param GZD			the path of the gaze file
	 * @param outputFile	the path of the output location
	 */
	public static void mergingResultFiles(String FXD, String EVD, String GZD, String outputFile) throws IOException
	{

 		FileWriter outputFileWriter = new FileWriter(new File (outputFile));
        CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
        
        FileReader fileReaderFXD = new FileReader(FXD);
        CSVReader csvReaderFXD = new CSVReader(fileReaderFXD);
        FileReader fileReaderEVD = new FileReader(EVD);
        CSVReader csvReaderEVD = new CSVReader(fileReaderEVD);
        FileReader fileReaderGZD = new FileReader(GZD);
        CSVReader csvReaderGZD = new CSVReader(fileReaderGZD);

        try
        {
    		Iterator<String[]> iterFXD = csvReaderFXD.iterator();
    		Iterator<String[]> iterEVD = csvReaderEVD.iterator();
    		Iterator<String[]> iterGZD = csvReaderGZD.iterator();
    		String[] rowFXD= new String[0];
    		String[] rowEVD = new String[0];
    		String[] rowGZD = new String[0];

        	while(iterFXD.hasNext())
        	{
        		rowGZD = iterGZD.next();
        		rowEVD = iterEVD.next();
        		rowFXD = iterFXD.next();
            	String[]results = new String[rowGZD.length + rowEVD.length + rowFXD.length];
            	System.arraycopy(rowGZD, 0, results, 0, rowGZD.length);
            	System.arraycopy(rowEVD, 0, results, rowGZD.length, rowEVD.length);
            	System.arraycopy(rowFXD, 0, results, rowEVD.length + rowGZD.length, rowFXD.length);
            	outputCSVWriter.writeNext(results);
        	}

        }
        catch(Exception e)
        {
        	System.out.println(e);
			systemLogger.writeToSystemLog(Level.WARNING, main.class.getName(), "");
        }
        finally
        {
        	outputCSVWriter.close();
        	csvReaderFXD.close();
        	csvReaderEVD.close();
        	csvReaderGZD.close();
        }
	}



	/**
	 * create baseline file by grabbing the first two minutes of the file and averaging it
	 * 
	 * @param filePath
	 * @param outputFolder
	 */
	public static void createBaselineFile(String filePath, String outputFolder) throws IOException, CsvValidationException
	{
		FileWriter outputFileWriter = new FileWriter(new File (outputFolder + "/baselineModifiedFile.csv"));
		CSVWriter outputCSVWriter = new CSVWriter(outputFileWriter);
		FileReader fileReader = new FileReader(filePath);
		CSVReader csvReader = new CSVReader(fileReader);
		String[]nextLine = csvReader.readNext();//header
		outputCSVWriter.writeNext(nextLine);
		
		try 
		{
			
			while((nextLine = csvReader.readNext()) != null)
			{
				if(Double.valueOf(nextLine[3]) <= 120) //two minutes
				{
					outputCSVWriter.writeNext(nextLine);
				}
			}
		}
		catch(Exception e)
		{
			System.out.println(e);
		}
		finally
		{
			outputCSVWriter.close();
			csvReader.close();

		}
		gaze.processGaze(outputFolder + "/baselineModifiedFile.csv", outputFolder + "/baseline.csv");
		csvToARFF(outputFolder + "/baseline.csv");
	}
	
	/**
	 * UI for users to select the file they want to use
	 * 
	 * @param	dialogTitle		title of the window
	 * @param 	directory		directory to choose file from relative to project directory		
	 */
	public static String fileChooser(String dialogTitle, String directory)
	{
		//Initializes the user to a set directory
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir")  + directory);

		//ensures that only CSV files will be able to be selected
		jfc.setFileFilter(new FileNameExtensionFilter("CSV", "csv"));
		jfc.setDialogTitle(dialogTitle);
		int returnValue = jfc.showOpenDialog(null);

		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			File selectedFile = jfc.getSelectedFile();
			return selectedFile.getAbsolutePath();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return "";
	}


	/**
	 * UI for users to select the folder they would want to use to place files in
	 * 
	 * @param	dialogTitle		title of the window
	 */
	public static String folderChooser(String dialogTitle)
	{
		//Initializes the user to a set directory
		JFileChooser jfc = new JFileChooser(System.getProperty("user.dir") + "/results/");
		jfc.setDialogTitle("Choose a directory to save your file: ");

		//only directories can be selected
		jfc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int returnValue = jfc.showSaveDialog(null);
		if (returnValue == JFileChooser.APPROVE_OPTION) 
		{
			if (jfc.getSelectedFile().isDirectory()) 
				return jfc.getSelectedFile().toString();
		}
		else
		{
			JOptionPane.showMessageDialog(null, "Must pick a location to output the file", "Error Message", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}
		return "";
	}
	
	/**
	 * Modifies input data files by cleansing the data and calculating the saccade velocity as an additional column
	 * 
	 * @param	inputFiles	Array of size 2 containing the path to the all_gaze and fixation data files
	 * @param	outputPath	String of the output path
	 * @return	Array of size 2 containing the path to the cleansed data files
	 */
	public static String[] processData(String[] inputFiles, String dir, int SCREEN_WIDTH, int SCREEN_HEIGHT) {
		String participantName = dir.substring(dir.lastIndexOf("/"));
		String dirPrefix = dir + "/inputFiles/" + participantName + "_cleansed";
		String[] outputFiles = new String[] {dirPrefix + "_all_gaze.csv", dirPrefix + "_fixation.csv"};
		File folder = new File(dir + "/inputFiles");
		
		// Create a folder to store the input files if it doesn't already exist
		if(!folder.exists()) {
			boolean folderCreated = folder.mkdir();
			if(!folderCreated)
				System.err.println("Unable to create modified data files folder.");
		}
		
		// Parse through the input files and remove any entries that are off screen or invalid
		// then calculate the saccade velocity and append it as a new column
		try {
			
			HashMap<String, String> peakVelocities =  new HashMap<String, String>();
			
			for (int i = 0; i < inputFiles.length; i++) {
				CSVReader reader = new CSVReader(new FileReader(new File(inputFiles[i])));
				CSVWriter writer = new CSVWriter(new FileWriter(new File(outputFiles[i])));
				Iterator<String[]> iter = reader.iterator();
				
				// Write the headers to the file
				ArrayList<String> headers = new ArrayList<String>(Arrays.asList(iter.next()));
				headers.add("SACCADE_VEL");
				headers.add("SACCADE_PV");
				headers.add("SACCADE_AMPL");
				writer.writeNext(headers.toArray(new String[headers.size()]));
				
				// Find the indexes of the all required data fields
				int validityIndex = headers.indexOf("FPOGV");
				int fixationID = headers.indexOf("FPOGID");
				int sacDirIndex = headers.indexOf("SACCADE_DIR");
				int xIndex = headers.indexOf("FPOGX");
				int yIndex = headers.indexOf("FPOGY");
				int timeIndex = -1;
	            int pupilLeftValidityIndex = headers.indexOf("LPMMV");
	            int pupilRightValidityIndex = headers.indexOf("RPMMV");
	            int pupilLeftDiameterIndex = headers.indexOf("LPMM");
	            int pupilRightDiameterIndex = headers.indexOf("RPMM");
	            int saccadeDistanceIndex = headers.indexOf("SACCADE_MAG");
				
				// Two columns contain "TIME" and the name of the time column is dynamic, therefore search for it
				for (int j = 0; j < headers.size(); j++) {
					String header = headers.get(j);
					if (header.contains("TIME") && !header.contains("TIMETICK")) {
						timeIndex = j;
						break;
					}
				}
				
				if (timeIndex == -1 || sacDirIndex == -1) {
					JOptionPane.showMessageDialog(null, "Data file does not contain required columns", "Error Message", JOptionPane.ERROR_MESSAGE);
					System.exit(0);
				}
				
				String[] prevRow = iter.next();
				ArrayList<String> row = new ArrayList<String>();
				while (iter.hasNext()) {
					String[]currRow = iter.next();
					double x = Double.valueOf(currRow[xIndex]);
					double y = Double.valueOf(currRow[yIndex]);
					boolean onScreen = (x <= 1.0 && x >= 0 && y <= 1.0 && y >= 0) ? true : false;
			
					// Checks the pupils validity
					boolean pupilLeftValid = Integer.valueOf(currRow[pupilLeftValidityIndex]) == 1 ? true : false;
					boolean pupilRightValid = Integer.valueOf(currRow[pupilRightValidityIndex]) == 1 ? true : false;
					boolean pupilsDimensionValid = false;
                	double pupilLeft = Double.parseDouble(currRow[pupilLeftDiameterIndex]);
                	double pupilRight = Double.parseDouble(currRow[pupilRightDiameterIndex]);
                	
                
                	// Checks if pupil sizes are possible (between 2mm to 8mm)
                	if(pupilLeft >=2 && pupilLeft <=8 && pupilRight >=2 && pupilRight <=8)
                	{
                		// Checks if the difference in size between the left and right is 1mm or less
                		if(Math.abs(pupilRight - pupilLeft) <= 1)
                		{
                			pupilsDimensionValid = true;
                		}
                	}
                	if (onScreen && pupilLeftValid && pupilRightValid && pupilsDimensionValid) {
					row = new ArrayList<String>(Arrays.asList(currRow));
					row.add(0 + "");
					row.add(0 + "");
					row.add(0 + "");
					writer.writeNext(row.toArray(new String[row.size()]));
					break;
                	}
				}
				
				

				
				ArrayList<Double[]> saccadePoints = new ArrayList<Double[]>();
				
				while (iter.hasNext()) {
					String[]currRow = iter.next();
					double x = Double.valueOf(currRow[xIndex]);
					double y = Double.valueOf(currRow[yIndex]);
					boolean onScreen = (x <= 1.0 && x >= 0 && y <= 1.0 && y >= 0) ? true : false;
			
					// Checks the pupils validity
					boolean pupilLeftValid = Integer.valueOf(currRow[pupilLeftValidityIndex]) == 1 ? true : false;
					boolean pupilRightValid = Integer.valueOf(currRow[pupilRightValidityIndex]) == 1 ? true : false;
					boolean pupilsDimensionValid = false;
                	double pupilLeft = Double.parseDouble(currRow[pupilLeftDiameterIndex]);
                	double pupilRight = Double.parseDouble(currRow[pupilRightDiameterIndex]);
                	
                
                	// Checks if pupil sizes are possible (between 2mm to 8mm)
                	if(pupilLeft >=2 && pupilLeft <=8 && pupilRight >=2 && pupilRight <=8)
                	{
                		// Checks if the difference in size between the left and right is 1mm or less
                		if(Math.abs(pupilRight - pupilLeft) <= 1)
                		{
                			pupilsDimensionValid = true;
                		}
                	}
					
					row = new ArrayList<String>(Arrays.asList(currRow));
					
					// Check to see if these are concurrent fixations
					if (Integer.valueOf(prevRow[fixationID]) == (Integer.valueOf(currRow[fixationID]) - 1))
						row.add(Double.toString(Double.valueOf(currRow[sacDirIndex])/Math.abs(Double.valueOf(currRow[timeIndex]) - Double.valueOf(prevRow[timeIndex]))));
					else
						row.add(0 + "");
										
					if (Double.valueOf(currRow[validityIndex]) == 0) {
						if (saccadePoints.size() == 0) {
							double prevX = Double.parseDouble(prevRow[xIndex]) * SCREEN_WIDTH;
							double prevY = Double.parseDouble(prevRow[yIndex]) * SCREEN_HEIGHT;
							double prevTime = Double.parseDouble(prevRow[timeIndex]);
							saccadePoints.add(new Double[] {prevX, prevY, prevTime});
						}
						
						double currTime = Double.parseDouble(currRow[timeIndex]);
						saccadePoints.add(new Double[] {x * SCREEN_WIDTH, y * SCREEN_HEIGHT, currTime});
					}
					
					if (onScreen && pupilLeftValid && pupilRightValid && pupilsDimensionValid) {
						if (Double.valueOf(currRow[sacDirIndex]) != 0) {
							String amplitude = 180/Math.PI * Math.atan((Double.parseDouble(currRow[saccadeDistanceIndex]) * 0.0264583333)/65) + "";
							prevRow = currRow;
							String time = currRow[timeIndex];
							if (peakVelocities.containsKey(time)) {
								row.add(peakVelocities.get(time));
							}
							else {
								String peakVelocity = saccade.getPeakVelocity(saccadePoints) + "";
								peakVelocities.put(time, peakVelocity);
								row.add(peakVelocity);
								saccadePoints.clear();
							}
							row.add(amplitude);
						}
						else {
							row.add(0 + "");
							row.add(0 + "");
						}
						
						writer.writeNext(row.toArray(new String[row.size()]));
					}
					
					// For the very first fixation, find the last valid point
					if (Double.valueOf(currRow[fixationID]) == 1) {
						if (Integer.valueOf(currRow[validityIndex]) == 1)
							prevRow = currRow;
					}
				}
				
				reader.close();
				writer.close();
			}
		}
		catch (Exception e) {
			System.err.println(e);
			System.exit(0);
		}

		return outputFiles;
	}


	/**
	 * create multiple folders within the chosen location
	 * 
	 * @param	outLocation	where the folders will reside
	 * @param	partInfo All the folder names in which it will be named after
	 * @return	returns true if the folders are successfully created, false otherwise
	 */
	public static boolean createFolders(String outputLocation, HashMap<String, String>names)
	{
		String message = "";
		for(String name: names.keySet())
		{
			File folder = new File(outputLocation + "/" + name);
			
			// Create a folder to store the input files if it doesn't already exist
			if(!folder.exists()) {
				boolean folderCreated = folder.mkdir();
				if(!folderCreated)
				{
					message += name + ": Unable to create modified data files folder.";
					return false;
				}
			}
		}
		
		if(!message.equals(""))
		{
			return false;
		}
		return true;		
	}
}
