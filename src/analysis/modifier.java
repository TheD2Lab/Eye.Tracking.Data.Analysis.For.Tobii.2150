package analysis;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
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
				systemLogger.writeToSystemLog(Level.INFO, gazeAnalytics.class.getName(), "Successfully converted CSV to ARFF " + outputARFFPath);
			}
			else
			{
				System.out.println("File Exists");
			}
		}
		catch(IOException e)
		{
			System.out.println("Error coverting CSV to ARFF " + outputARFFPath + "\n" + e.toString());
			systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + e.toString());

		}
		catch(IllegalArgumentException ia)
		{
			System.out.println("Error coverting CSV to ARFF " + outputARFFPath + "\n" + ia.toString());
			systemLogger.writeToSystemLog(Level.WARNING, gazeAnalytics.class.getName(), "Error coverting CSV to ARFF " + outputARFFPath + "\n" + ia.toString());

		}
	}
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




	/*
	 * create baseline file
	 * grabs the first two minutes of the file and averages it
	 * 
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
	/*
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


	/*
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
}
